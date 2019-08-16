package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.EvaluatePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.EvaluateUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IEvaluateView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public class EvaluateActivity extends BaseActivity<IEvaluateView, EvaluatePresenter> implements IEvaluateView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.rb_overall_merit)
    RatingBar overallMeritRB;
    @BindView(R.id.tv_overall_merit_evaluate)
    TextView overallMeritEvaluateTv;
    @BindView(R.id.ll_evaluate_facilitator)
    LinearLayout evaluateFacilitatorLL;
    @BindView(R.id.rb_complete_on_time)
    RatingBar completeOnTimeRB;
    @BindView(R.id.tv_complete_on_time_evaluate)
    TextView completeOnTimeEvaluateTv;
    @BindView(R.id.rb_work_quality)
    RatingBar workQualityRB;
    @BindView(R.id.tv_work_quality_evaluate)
    TextView workQualityEvaluateTv;
    @BindView(R.id.rb_service_integrity)
    RatingBar serviceIntegrityRB;
    @BindView(R.id.tv_service_integrity_evaluate)
    TextView serviceIntegrityEvaluateTv;
    @BindView(R.id.et_evaluation_content)
    EditText evaluationContentEt;
    @BindView(R.id.tv_number_of_words)
    TextView numberOfWordsTv;
    @BindView(R.id.btn_submit_evaluate)
    Button submitEvaluateBtn;

    private int evaluateType = CommonConstant.EVALUATE_TYPE_EMPLOYER;
    private int userId;
    private int demandId;
    private int orderId;
    private int serviceId;
    private int buyerUserId;
    private int sellerUserId;

    private float overallMeritEvaluate;
    private float completeOnTimeEvaluate;
    private float workQualityEvaluate;
    private float serviceIntegrityEvaluate;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_evaluate;
    }

    @Override
    protected EvaluatePresenter CreatePresenter() {
        return new EvaluatePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            evaluateType = intent.getIntExtra(CommonConstant.EXTRA_EVALUATE_TYPE, CommonConstant.EVALUATE_TYPE_EMPLOYER);
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            orderId = intent.getIntExtra(CommonConstant.EXTRA_ORDER_ID, -1);
            serviceId = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_ID, -1);
            buyerUserId = intent.getIntExtra(CommonConstant.EXTRA_BUYER_USER_ID, -1);
            sellerUserId = intent.getIntExtra(CommonConstant.EXTRA_SELLER_USER_ID, -1);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        switch (evaluateType) {
            case CommonConstant.EVALUATE_TYPE_EMPLOYER:
                titleTv.setText(getString(R.string.evaluate_employer));
                break;

            case CommonConstant.EVALUATE_TYPE_SERVICE:
                titleTv.setText(getString(R.string.evaluate_service));
                break;
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        overallMeritRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                overallMeritEvaluate = rating;
                overallMeritEvaluateTv.setText(EvaluateUtils.getEvaluation(overallMeritEvaluate));
                checkSubmitEvaluateEnable();
            }
        });

        if (evaluateType == CommonConstant.EVALUATE_TYPE_EMPLOYER) {
            evaluateFacilitatorLL.setVisibility(View.GONE);
        } else if (evaluateType == CommonConstant.EVALUATE_TYPE_SERVICE) {
            evaluateFacilitatorLL.setVisibility(View.VISIBLE);

            completeOnTimeRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    completeOnTimeEvaluate = rating;
                    completeOnTimeEvaluateTv.setText(EvaluateUtils.getEvaluation(completeOnTimeEvaluate));
                    checkSubmitEvaluateEnable();
                }
            });

            workQualityRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    workQualityEvaluate = rating;
                    workQualityEvaluateTv.setText(EvaluateUtils.getEvaluation(workQualityEvaluate));
                    checkSubmitEvaluateEnable();
                }
            });

            serviceIntegrityRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    serviceIntegrityEvaluate = rating;
                    serviceIntegrityEvaluateTv.setText(EvaluateUtils.getEvaluation(serviceIntegrityEvaluate));
                    checkSubmitEvaluateEnable();
                }
            });
        }

        evaluationContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String evaluationContent = evaluationContentEt.getText().toString().trim();
                int index = evaluationContentEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(evaluationContent)) {
                        Editable editable = evaluationContentEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int evaluationContentLength = evaluationContent.length();
                numberOfWordsTv.setText(String.valueOf(evaluationContentLength) + "/100");

                checkSubmitEvaluateEnable();
            }
        });

        submitEvaluateBtn.setEnabled(false);
        submitEvaluateBtn.setClickable(false);
    }

    private void checkSubmitEvaluateEnable() {
        String evaluationContent = evaluationContentEt.getText().toString().trim();

        if (evaluateType == CommonConstant.EVALUATE_TYPE_EMPLOYER) {
            if (orderId != -1 && serviceId != -1 && buyerUserId != -1 && sellerUserId != -1 && overallMeritEvaluate != 0F && !TextUtils.isEmpty(evaluationContent)) {
                submitEvaluateBtn.setEnabled(true);
                submitEvaluateBtn.setClickable(true);
            } else {
                submitEvaluateBtn.setEnabled(false);
                submitEvaluateBtn.setClickable(false);
            }
        } else if (evaluateType == CommonConstant.EVALUATE_TYPE_SERVICE) {
            if (orderId != -1 && serviceId != -1 && buyerUserId != -1 && sellerUserId != -1 && overallMeritEvaluate != 0F && completeOnTimeEvaluate != 0F && workQualityEvaluate != 0F && serviceIntegrityEvaluate != 0F && !TextUtils.isEmpty(evaluationContent)) {
                submitEvaluateBtn.setEnabled(true);
                submitEvaluateBtn.setClickable(true);
            } else {
                submitEvaluateBtn.setEnabled(false);
                submitEvaluateBtn.setClickable(false);
            }
        }
    }

    private void sendEvaluatedMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();
        String message = null;

        if (evaluateType == CommonConstant.EVALUATE_TYPE_EMPLOYER) {
            message = getString(R.string.message_sender_employer_evaluated_content);
        } else if (evaluateType == CommonConstant.EVALUATE_TYPE_SERVICE) {
            message = getString(R.string.message_sender_service_evaluated_content);
        }

        EMMessage emMessage = EMMessage.createTxtSendMessage(message, String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_evaluated_title));

        if (evaluateType == CommonConstant.EVALUATE_TYPE_EMPLOYER) {
            emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_EVALUATED_EMPLOYER);
            emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_employer_evaluated_content));
            emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_employer_evaluated_content));
        } else if (evaluateType == CommonConstant.EVALUATE_TYPE_SERVICE) {
            emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_EVALUATED_FACILITATOR);
            emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_service_evaluated_content));
            emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_service_evaluated_content));
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_see));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_see));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_ORDER_ID, String.valueOf(orderId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, evaluateType);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_COMPLETED);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.btn_submit_evaluate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_evaluate:
                String evaluationContent = evaluationContentEt.getText().toString().trim();
                int evaluationContentLength = evaluationContent.length();

                if (evaluationContentLength >= 10) {
                    if (evaluateType == CommonConstant.EVALUATE_TYPE_EMPLOYER) {
                        mPresenter.evaluateBuyerRequest(orderId, serviceId, buyerUserId, sellerUserId, overallMeritEvaluate, evaluationContent);
                    } else if (evaluateType == CommonConstant.EVALUATE_TYPE_SERVICE) {
                        mPresenter.evaluateSellerRequest(orderId, serviceId, buyerUserId, sellerUserId, overallMeritEvaluate, completeOnTimeEvaluate, workQualityEvaluate, serviceIntegrityEvaluate, evaluationContent);
                    }
                } else {
                    showToast("评价内容不够字数");
                }
                break;
        }
    }

    @Override
    public void onEvaluateBuyerSuccess() {
        sendEvaluatedMessage(userId, demandId);
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onEvaluateSellerSuccess() {
        sendEvaluatedMessage(userId, demandId);
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

}
