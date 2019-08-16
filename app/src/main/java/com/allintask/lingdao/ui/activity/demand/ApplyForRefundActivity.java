package com.allintask.lingdao.ui.activity.demand;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.ApplyForRefundPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.demand.IApplyForRefundView;
import com.allintask.lingdao.widget.SelectWorkStatusDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public class ApplyForRefundActivity extends BaseActivity<IApplyForRefundView, ApplyForRefundPresenter> implements IApplyForRefundView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_work_status)
    LinearLayout workStatusLL;
    @BindView(R.id.tv_work_status)
    TextView workStatusTv;
    @BindView(R.id.et_refund_reason)
    EditText refundReasonEt;
    @BindView(R.id.et_refund_money)
    EditText refundMoneyEt;
    @BindView(R.id.tv_number_of_words)
    TextView numberOfWordsTv;
    @BindView(R.id.tv_order_price)
    TextView orderPriceTv;
    @BindView(R.id.btn_confirm_submit)
    Button confirmSubmitBtn;

    private int userId;
    private int demandId;
    private int orderId;
    private int orderPrice = 0;

    private int workStatus = -1;
    private SelectWorkStatusDialog selectWorkStatusDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_for_refund;
    }

    @Override
    protected ApplyForRefundPresenter CreatePresenter() {
        return new ApplyForRefundPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            orderId = intent.getIntExtra(CommonConstant.EXTRA_ORDER_ID, -1);
            orderPrice = intent.getIntExtra(CommonConstant.EXTRA_ORDER_PRICE, 0);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.apply_for_refund));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        refundReasonEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String refundReason = refundReasonEt.getText().toString().trim();
                int index = refundReasonEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(refundReason)) {
                        Editable editable = refundReasonEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int refundReasonLength = refundReason.length();
                numberOfWordsTv.setText(String.valueOf(refundReasonLength) + "/20");

                checkConfirmSubmitEnable();
            }
        });

        refundMoneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmSubmitEnable();
            }
        });

        StringBuilder stringBuilder = new StringBuilder("最多为订单金额");
        stringBuilder.append(String.valueOf(orderPrice)).append("元");
        orderPriceTv.setText(stringBuilder);

        confirmSubmitBtn.setEnabled(false);
        confirmSubmitBtn.setClickable(false);
    }

    private void showSelectWorkStatusDialog() {
        selectWorkStatusDialog = new SelectWorkStatusDialog(getParentContext());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectWorkStatusDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectWorkStatusDialog.show();
        selectWorkStatusDialog.setOnClickListener(new SelectWorkStatusDialog.OnClickListener() {
            @Override
            public void onWorkStatusClick(int selectedWorkStatus) {
                if (null != selectWorkStatusDialog && selectWorkStatusDialog.isShowing()) {
                    selectWorkStatusDialog.dismiss();
                }

                workStatus = selectedWorkStatus;

                if (workStatus == CommonConstant.WORK_STATUS_UNFINISHED) {
                    workStatusTv.setText(getString(R.string.unfinished));
                } else if (workStatus == CommonConstant.WORK_STATUS_COMPLETED) {
                    workStatusTv.setText(getString(R.string.completed));
                }

                checkConfirmSubmitEnable();
            }
        });
    }

    private void checkConfirmSubmitEnable() {
        String refundReason = refundReasonEt.getText().toString().trim();
        String refundMoney = refundMoneyEt.getText().toString().trim();

        if (workStatus != -1 && !TextUtils.isEmpty(refundReason) && !TextUtils.isEmpty(refundMoney)) {
            confirmSubmitBtn.setEnabled(true);
            confirmSubmitBtn.setClickable(true);
        } else {
            confirmSubmitBtn.setEnabled(false);
            confirmSubmitBtn.setClickable(false);
        }
    }

    private void sendApplyForRefundMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_apply_for_refund_content), String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_APPLY_FOR_REFUND);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_apply_for_refund_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_apply_for_refund_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_apply_for_refund_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_dispose));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.ll_work_status, R.id.btn_confirm_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_work_status:
                showSelectWorkStatusDialog();
                break;

            case R.id.btn_confirm_submit:
                String refundReason = refundReasonEt.getText().toString().trim();
                String refundMoney = refundMoneyEt.getText().toString().trim();

                mPresenter.applyRefundRequest(orderId, workStatus, refundReason, refundMoney);
                break;
        }
    }

    @Override
    public void onApplyForRefundSuccess() {
        sendApplyForRefundMessage(userId, demandId);
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

}
