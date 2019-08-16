package com.allintask.lingdao.ui.activity.pay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.pay.PayPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.adapter.pay.PaymentMethodAdapter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.pay.IPayView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.PayDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.sina.weibo.sdk.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.DialogUtils;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public class PayActivity extends BaseActivity<IPayView, PayPresenter> implements IPayView {

    private static final int MESSAGE_CODE_ALIPAY = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_payment_money)
    TextView paymentMoneyTv;
    @BindView(R.id.ll_trusteeship_amount)
    LinearLayout trusteeshipAmountLL;
    @BindView(R.id.tv_trusteeship_amount)
    TextView trusteeshipAmountTv;
    @BindView(R.id.tv_actual_payment_money)
    TextView actualPaymentMoneyTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.cb_agree)
    CheckBox agreeCB;
    @BindView(R.id.btn_pay_at_once)
    Button payAtOnceBtn;

    private int userId;
    private int demandId;
    private int serviceId;
    private String sellerName;
    private int paymentMoney;

    private PayActivityBroadcastReceiver payActivityBroadcastReceiver;

    private int paymentMethod = -1;
    private int salaryTrusteeshipId = -1;
    private double surplusAmount;
    private int actualPaymentMoney;
    private boolean isAgree = true;

    private PaymentMethodAdapter paymentMethodAdapter;
    private PayDialog payDialog;

    private boolean isTrusteeshipPay = false;

    private final AlipayHandler alipayHandler = new AlipayHandler(this);

    private static class AlipayHandler extends Handler {

        private final WeakReference<Activity> activityWeakReference;

        public AlipayHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PayActivity payActivity = (PayActivity) activityWeakReference.get();

            if (null != payActivity) {
                if (msg.what == MESSAGE_CODE_ALIPAY) {
                    Map<String, String> result = (Map<String, String>) msg.obj;
                    String resultStatus = TypeUtils.getString(result.get(CommonConstant.ALIPAY_RESULT_STATUS), "");

                    switch (resultStatus) {
                        case CommonConstant.ALIPAY_RESULT_STATUS_SUCCESS:
                            payActivity.sendPaySuccessMessage(payActivity.userId, payActivity.demandId);
                            payActivity.setResult(CommonConstant.PAID_RESULT_CODE);
                            payActivity.finish();
                            break;

                        case CommonConstant.ALIPAY_RESULT_STATUS_CANCEL:
                            payActivity.showToast("用户取消支付");
                            break;
                    }
                }
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay;
    }

    @Override
    protected PayPresenter CreatePresenter() {
        return new PayPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            serviceId = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_ID, -1);
            sellerName = intent.getStringExtra(CommonConstant.EXTRA_SELLER_NAME);
            paymentMoney = intent.getIntExtra(CommonConstant.EXTRA_PAYMENT_MONEY, 0);
        }

        registerPayActivityReceiver();

        initToolbar();
        initUI();
        initData();
    }

    private void registerPayActivityReceiver() {
        payActivityBroadcastReceiver = new PayActivityBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_REFRESH);
        AllintaskApplication.getInstance().getLocalBroadcastManager().registerReceiver(payActivityBroadcastReceiver, intentFilter);
    }

    private class PayActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (!TextUtils.isEmpty(action)) {
                    if (action.equals(CommonConstant.ACTION_REFRESH)) {
                        String price = intent.getStringExtra(CommonConstant.EXTRA_PRICE);

                        if (!TextUtils.isEmpty(price)) {
                            if (null != payDialog && payDialog.isShowing()) {
                                payDialog.dismiss();
                            }

                            paymentMoney = Integer.valueOf(price);
                            paymentMoneyTv.setText(String.valueOf(paymentMoney) + "元");

                            initData();
                        }
                    }
                }
            }
        }

    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.pay));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        paymentMoneyTv.setText(String.valueOf(paymentMoney) + "元");

        paymentMethodAdapter = new PaymentMethodAdapter(getParentContext());
        recyclerView.setAdapter(paymentMethodAdapter);

        paymentMethodAdapter.setOnClickListener(new PaymentMethodAdapter.OnClickListener() {
            @Override
            public void onPaymentMethodSelected(int selectedPaymentMethod) {
                paymentMethod = selectedPaymentMethod;
                checkPayAtOnceEnable();
            }
        });

        agreeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgree = isChecked;
                checkPayAtOnceEnable();
            }
        });

        payAtOnceBtn.setEnabled(false);
        payAtOnceBtn.setClickable(false);
    }

    private void initData() {
        if (demandId != -1) {
            mPresenter.fetchTrusteeshipSurplusRequest(demandId);
        }
    }

    private void showPayDialog(boolean canInput, final int amount, String title, String tip, final String orderString) {
        payDialog = new PayDialog(getParentContext(), canInput, amount, title, tip);

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
                    if (isTrusteeshipPay) {
                        mPresenter.trusteeshipPayRequest(orderString, MD5PaymentPassword);
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

    private void checkPayAtOnceEnable() {
        if (serviceId != -1 && paymentMethod != CommonConstant.PAYMENT_METHOD_DEFAULT && isAgree) {
            payAtOnceBtn.setEnabled(true);
            payAtOnceBtn.setClickable(true);
        } else {
            payAtOnceBtn.setEnabled(false);
            payAtOnceBtn.setClickable(false);
        }
    }

    private void sendPaySuccessMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_pay_success_content), String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_PAY_SUCCESS);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_pay_success_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_pay_success_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_pay_success_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.btn_pay_at_once})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay_at_once:
                switch (paymentMethod) {
                    case CommonConstant.PAYMENT_METHOD_ALLINTASK_ACCOUNT:
                        mPresenter.checkCanWithdrawRequest();
                        break;

                    case CommonConstant.PAYMENT_METHOD_ALIPAY:
                        if (actualPaymentMoney > 0) {
                            mPresenter.getBuyServiceBidAlipayOrderStringRequest(serviceId, actualPaymentMoney, salaryTrusteeshipId);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onShowTrusteeshipSurplusData(int salaryTrusteeshipId, double surplusAmount) {
        this.salaryTrusteeshipId = salaryTrusteeshipId;
        this.surplusAmount = surplusAmount;

        if (this.surplusAmount > 0D) {
            StringBuilder trusteeshipAmountSB = new StringBuilder(String.valueOf((int) surplusAmount)).append("元");
            trusteeshipAmountTv.setText(trusteeshipAmountSB);
            trusteeshipAmountLL.setVisibility(View.VISIBLE);
        } else {
            trusteeshipAmountLL.setVisibility(View.GONE);
        }

        if (this.surplusAmount >= paymentMoney) {
            isTrusteeshipPay = true;

            actualPaymentMoney = (int) this.surplusAmount - paymentMoney;
            actualPaymentMoneyTv.setText("0元");

            List<PaymentMethodBean> paymentMethodList = new ArrayList<>();
            PaymentMethodBean paymentMethodBean = new PaymentMethodBean();
            paymentMethodBean.code = "ld";
            paymentMethodBean.value = getString(R.string.wallet_pay);
            paymentMethodBean.isSelected = true;
            paymentMethodList.add(paymentMethodBean);
            paymentMethodAdapter.setDateList(paymentMethodList);

            paymentMethod = CommonConstant.PAYMENT_METHOD_ALLINTASK_ACCOUNT;

            checkPayAtOnceEnable();
        } else {
            isTrusteeshipPay = false;

            actualPaymentMoney = paymentMoney - (int) this.surplusAmount;
            StringBuilder actualPaymentMoneySB = new StringBuilder(String.valueOf(actualPaymentMoney)).append("元");
            actualPaymentMoneyTv.setText(actualPaymentMoneySB);

            mPresenter.fetchPaymentMethodListRequest();
        }
    }

    @Override
    public void onShowTrusteeshipNoSurplus() {
        trusteeshipAmountTv.setVisibility(View.GONE);

        actualPaymentMoney = paymentMoney;
        StringBuilder actualPaymentMoneySB = new StringBuilder(String.valueOf(actualPaymentMoney)).append("元");
        actualPaymentMoneyTv.setText(actualPaymentMoneySB);

        mPresenter.fetchPaymentMethodListRequest();
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
                }
            }

            checkPayAtOnceEnable();

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
                    PayTask payTask = new PayTask(PayActivity.this);
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
            if (isTrusteeshipPay) {
                if (paymentMoney > 0) {
                    mPresenter.getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest(serviceId, 0D, salaryTrusteeshipId);
                }
            } else {
                if (actualPaymentMoney > 0) {
                    mPresenter.getBuyServiceBidAllintaskPayOrderStringRequest(serviceId, actualPaymentMoney, salaryTrusteeshipId);
                }
            }
        }
    }

    @Override
    public void onGetAllintaskPayOrderStringSuccess(String orderString, double totalAmount) {
        showPayDialog(true, actualPaymentMoney, "付款账户：领到账户", null, orderString);
    }

    @Override
    public void onAllintaskPaySuccess() {
        sendPaySuccessMessage(userId, demandId);
        setResult(CommonConstant.PAID_RESULT_CODE);
        finish();
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
    public void onGetTrusteeshipBuyServiceBidAllintaskPayOrderStringSuccess(String orderString, double totalAmount) {
        showPayDialog(true, 0, "付款账户：领到账户", null, orderString);
    }

    @Override
    public void onTrusteeshipPaySuccess() {
        sendPaySuccessMessage(userId, demandId);
        setResult(CommonConstant.PAID_RESULT_CODE);
        finish();
    }

    @Override
    public void onTrusteeshipPayFail(int errorCode, String errorMessage) {
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
    protected void onDestroy() {
        if (null != payActivityBroadcastReceiver) {
            AllintaskApplication.getInstance().getLocalBroadcastManager().unregisterReceiver(payActivityBroadcastReceiver);
        }

        super.onDestroy();
    }

}
