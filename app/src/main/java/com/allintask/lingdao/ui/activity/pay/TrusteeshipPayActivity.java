package com.allintask.lingdao.ui.activity.pay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.pay.TrusteeshipPayPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.demand.IntelligentMatchInformServiceProviderActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.adapter.pay.PaymentMethodAdapter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.pay.ITrusteeshipPayView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.PayDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.sina.weibo.sdk.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.DialogUtils;

/**
 * Created by TanJiaJun on 2018/5/12.
 */

public class TrusteeshipPayActivity extends BaseActivity<ITrusteeshipPayView, TrusteeshipPayPresenter> implements ITrusteeshipPayView {

    private static final int MESSAGE_CODE_ALIPAY = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_trusteeship_amount)
    TextView trusteeshipAmountTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.btn_confirm)
    Button confirmBtn;

    private int userId;
    private int publishDemandType;
    private int recommendMoreStatus = -1;
    private int demandId;
    private String demandName;
    private int trusteeshipAmount;
    private String serviceBidNumber;
    private boolean mIsUpdateDemand = false;

    private int paymentMethod = -1;

    private PaymentMethodAdapter paymentMethodAdapter;
    private PayDialog payDialog;

    private final AlipayHandler alipayHandler = new AlipayHandler(this);

    private static class AlipayHandler extends Handler {

        private final WeakReference<Activity> activityWeakReference;

        public AlipayHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TrusteeshipPayActivity trusteeshipPayActivity = (TrusteeshipPayActivity) activityWeakReference.get();

            if (null != trusteeshipPayActivity) {
                if (msg.what == MESSAGE_CODE_ALIPAY) {
                    Map<String, String> result = (Map<String, String>) msg.obj;
                    String resultStatus = TypeUtils.getString(result.get(CommonConstant.ALIPAY_RESULT_STATUS), "");

                    switch (resultStatus) {
                        case CommonConstant.ALIPAY_RESULT_STATUS_SUCCESS:
                            if (trusteeshipPayActivity.publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                                if (trusteeshipPayActivity.mIsUpdateDemand) {
                                    Intent intent = new Intent();
                                    intent.setAction(CommonConstant.ACTION_REFRESH_COMPILE_DEMAND);
                                    AllintaskApplication.getInstance().getLocalBroadcastManager().sendBroadcast(intent);
                                } else {
                                    Intent intent = new Intent(trusteeshipPayActivity.getParentContext(), IntelligentMatchInformServiceProviderActivity.class);
                                    intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, trusteeshipPayActivity.demandId);
                                    intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, trusteeshipPayActivity.demandName);
                                    trusteeshipPayActivity.startActivity(intent);
                                }

                                trusteeshipPayActivity.finish();
                            } else if (trusteeshipPayActivity.publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                                if (trusteeshipPayActivity.recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                                    trusteeshipPayActivity.sendEmploySuccessMessage(trusteeshipPayActivity.userId);

                                    Intent intent = new Intent(trusteeshipPayActivity.getParentContext(), MainActivity.class);
                                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
                                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
                                    trusteeshipPayActivity.startActivity(intent);
                                } else {
                                    if (trusteeshipPayActivity.demandId != -1) {
                                        Intent intent = new Intent(trusteeshipPayActivity.getParentContext(), IntelligentMatchInformServiceProviderActivity.class);
                                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, trusteeshipPayActivity.demandId);
                                        intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, trusteeshipPayActivity.demandName);
                                        trusteeshipPayActivity.startActivity(intent);

                                        trusteeshipPayActivity.finish();
                                    }
                                }
                            }
                            break;

                        case CommonConstant.ALIPAY_RESULT_STATUS_CANCEL:
                            trusteeshipPayActivity.showToast("用户取消支付");
                            break;
                    }
                }
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_trusteeship_pay;
    }

    @Override
    protected TrusteeshipPayPresenter CreatePresenter() {
        return new TrusteeshipPayPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            publishDemandType = intent.getIntExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON);
            recommendMoreStatus = intent.getIntExtra(CommonConstant.EXTRA_RECOMMEND_MORE_STATUS, -1);
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            demandName = intent.getStringExtra(CommonConstant.EXTRA_DEMAND_NAME);
            trusteeshipAmount = intent.getIntExtra(CommonConstant.EXTRA_TRUSTEESHIP_AMOUNT, 0);
            serviceBidNumber = intent.getStringExtra(CommonConstant.EXTRA_SERVICE_BID_NUMBER);
            mIsUpdateDemand = intent.getBooleanExtra(CommonConstant.EXTRA_IS_UPDATE_DEMAND, false);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));

        if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
            toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        } else {
            toolbar.setNavigationIcon(null);
        }

        toolbar.setTitle("");

        titleTv.setText(getString(R.string.trusteeship_amount));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(trusteeshipAmount)).append("元");
        trusteeshipAmountTv.setText(stringBuilder);

        paymentMethodAdapter = new PaymentMethodAdapter(getParentContext());
        recyclerView.setAdapter(paymentMethodAdapter);

        paymentMethodAdapter.setOnClickListener(new PaymentMethodAdapter.OnClickListener() {
            @Override
            public void onPaymentMethodSelected(int selectedPaymentMethod) {
                paymentMethod = selectedPaymentMethod;
                checkConfirmEnable();
            }
        });

        confirmBtn.setEnabled(false);
        confirmBtn.setClickable(false);
    }

    private void initData() {
        mPresenter.fetchPaymentMethodListRequest(serviceBidNumber);
    }

    private void showPayDialog(boolean canInput, final double amount, String title, String tip, final String orderString) {
        payDialog = new PayDialog(getParentContext(), canInput, (int) amount, title, tip);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = payDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        payDialog.show();
        payDialog.setOnInputtedListener(new PayDialog.OnPayDialogListener() {
            @Override
            public void onInputted(String paymentPassword) {
                String MD5PaymentPassword = MD5.hexdigest(paymentPassword);

                if (!TextUtils.isEmpty(MD5PaymentPassword)) {
                    if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                        mPresenter.allintaskPayV1Request(orderString, MD5PaymentPassword);
                    } else {
                        mPresenter.allintaskPayRequest(orderString, MD5PaymentPassword);
                    }
                }
            }

            @Override
            public void onForgetPasswordClick() {

            }
        });
    }

    private void checkConfirmEnable() {
        if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
            if (paymentMethod != CommonConstant.PAYMENT_METHOD_DEFAULT) {
                confirmBtn.setEnabled(true);
                confirmBtn.setClickable(true);
            } else {
                confirmBtn.setEnabled(false);
                confirmBtn.setClickable(false);
            }
        } else {
            if (paymentMethod != CommonConstant.PAYMENT_METHOD_DEFAULT) {
                confirmBtn.setEnabled(true);
                confirmBtn.setClickable(true);
            } else {
                confirmBtn.setEnabled(false);
                confirmBtn.setClickable(false);
            }
        }
    }

    private void sendEmploySuccessMessage(int sendUserId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_employ_success_content), String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_EMPLOY_SUCCESS);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_employ_success));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_employ_success_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_employ_success_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                switch (paymentMethod) {
                    case CommonConstant.PAYMENT_METHOD_ALLINTASK_ACCOUNT:
                        mPresenter.checkCanWithdrawRequest();
                        break;

                    case CommonConstant.PAYMENT_METHOD_ALIPAY:
                        if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                            mPresenter.getPublishPaymentDemandAlipayOrderStringRequest(serviceBidNumber);
                        } else {
                            if (trusteeshipAmount > 0) {
                                mPresenter.getTrusteeshipAlipayOrderStringRequest(demandId, trusteeshipAmount);
                            }
                        }
                        break;

                    case CommonConstant.PAYMENT_METHOD_WITHHOLD_TRUSTEESHIP:
                        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                            if (mIsUpdateDemand) {
                                Intent intent = new Intent();
                                intent.setAction(CommonConstant.ACTION_REFRESH_COMPILE_DEMAND);
                                AllintaskApplication.getInstance().getLocalBroadcastManager().sendBroadcast(intent);
                            } else {
                                Intent intent = new Intent(getParentContext(), IntelligentMatchInformServiceProviderActivity.class);
                                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                                intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, demandName);
                                startActivity(intent);
                            }

                            finish();
                        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                            if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                                sendEmploySuccessMessage(userId);

                                Intent intent = new Intent(getParentContext(), MainActivity.class);
                                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
                                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
                                startActivity(intent);
                            } else {
                                if (demandId != -1) {
                                    Intent intent = new Intent(getParentContext(), IntelligentMatchInformServiceProviderActivity.class);
                                    intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                                    intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, demandName);
                                    startActivity(intent);

                                    finish();
                                }
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onShowPaymentMethodList(List<PaymentMethodBean> paymentMethodList) {
        if (null != paymentMethodAdapter && null != paymentMethodList && paymentMethodList.size() > 0) {
            PaymentMethodBean paymentMethodBean = paymentMethodList.get(0);

            if (null != paymentMethodBean) {
                String code = TypeUtils.getString(paymentMethodBean.code, "");

                if (code.equals("ld")) {
                    paymentMethod = CommonConstant.PAYMENT_METHOD_ALLINTASK_ACCOUNT;
                } else if (code.equals("zfb")) {
                    paymentMethod = CommonConstant.PAYMENT_METHOD_ALIPAY;
                } else if (code.equals("wx")) {
                    paymentMethod = CommonConstant.PAYMENT_METHOD_WECHAT_PAY;
                } else if (code.equals("withholdTrusteeship")) {
                    paymentMethod = CommonConstant.PAYMENT_METHOD_WITHHOLD_TRUSTEESHIP;
                }
            }

            checkConfirmEnable();

            paymentMethodAdapter.setDateList(paymentMethodList);
            showContentView();
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onGetAliPayOrderStringSuccess(final String orderString, double totalAmount) {
        if (!TextUtils.isEmpty(orderString)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(TrusteeshipPayActivity.this);
                    Map<String, String> result = payTask.payV2(orderString, true);

                    Message message = alipayHandler.obtainMessage();
                    message.what = MESSAGE_CODE_ALIPAY;
                    message.obj = result;
                    alipayHandler.sendMessage(message);
                }
            });

            thread.start();
        } else {
            showToast("orderString不能为空");
        }
    }

    @Override
    public void onShowCheckCanWithdrawData(boolean isExistBankCard, boolean isExistPayPwd, boolean isExistRealName) {
        if (!isExistPayPwd) {
            DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "你还没有设置支付密码，设置完再提现", getString(R.string.go_to_set), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    int mobileCountryCodeId = UserPreferences.getInstance().getMobileCountryCodeId();

                    Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
                    intent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                }
            }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                mPresenter.getPublishPaymentDemandAllintaskPayOrderStringRequest(serviceBidNumber);
            } else {
                if (trusteeshipAmount > 0) {
                    mPresenter.getTrusteeshipAllintaskPayOrderStringRequest(demandId, trusteeshipAmount);
                }
            }
        }
    }

    @Override
    public void onGetAllintaskPayOrderStringSuccess(String orderString, double totalAmount) {
        showPayDialog(true, trusteeshipAmount, "付款账户：领到账户", null, orderString);
    }

    @Override
    public void onAllintaskPaySuccess() {
        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            if (mIsUpdateDemand) {
                Intent intent = new Intent();
                intent.setAction(CommonConstant.ACTION_REFRESH_COMPILE_DEMAND);
                AllintaskApplication.getInstance().getLocalBroadcastManager().sendBroadcast(intent);
            } else {
                Intent intent = new Intent(getParentContext(), IntelligentMatchInformServiceProviderActivity.class);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, demandName);
                startActivity(intent);
            }

            finish();
        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
            if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                sendEmploySuccessMessage(userId);

                Intent intent = new Intent(getParentContext(), MainActivity.class);
                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
                startActivity(intent);
            } else {
                if (demandId != -1) {
                    Intent intent = new Intent(getParentContext(), IntelligentMatchInformServiceProviderActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                    intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, demandName);
                    startActivity(intent);

                    finish();
                }
            }
        }
    }

    @Override
    public void onAllintaskPayFail(int errorCode, String errorMessage) {
        if (errorCode == 40008) {
            if (null != payDialog) {
                payDialog.resetPaymentPasswordEditText();
                payDialog.setTip(errorMessage);
            }
        } else {
            if (null != payDialog) {
                payDialog.resetPaymentPasswordEditText();
            }

            showToast(errorMessage);
        }
    }

    @Override
    public void onBackPressed() {
        if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
            super.onBackPressed();
        }
    }

}
