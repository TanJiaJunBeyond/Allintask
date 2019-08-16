package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.EvaluateDetailsPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EvaluateUtils;
import com.allintask.lingdao.view.user.IEvaluateDetailsView;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/3/23.
 */

public class EvaluateDetailsActivity extends BaseActivity<IEvaluateDetailsView, EvaluateDetailsPresenter> implements IEvaluateDetailsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_first)
    LinearLayout firstLL;
    @BindView(R.id.tv_first)
    TextView firstTv;
    @BindView(R.id.rb_first_overall_merit)
    RatingBar firstOverallMeritRB;
    @BindView(R.id.tv_first_overall_merit_evaluate)
    TextView firstOverallMeritEvaluateTv;
    @BindView(R.id.ll_first_service_evaluate)
    LinearLayout firstServiceEvaluateLL;
    @BindView(R.id.rb_first_complete_on_time)
    RatingBar firstCompleteOnTimeRB;
    @BindView(R.id.tv_first_complete_on_time_evaluate)
    TextView firstCompleteOnTimeEvaluateTv;
    @BindView(R.id.rb_first_work_quality)
    RatingBar firstWorkQualityRB;
    @BindView(R.id.tv_first_work_quality_evaluate)
    TextView firstWorkQualityEvaluateTv;
    @BindView(R.id.rb_first_service_integrity)
    RatingBar firstServiceIntegrityRB;
    @BindView(R.id.tv_first_service_integrity_evaluate)
    TextView firstServiceIntegrityEvaluateTv;
    @BindView(R.id.tv_first_evaluate_content)
    TextView firstEvaluateContentTv;
    @BindView(R.id.ll_second)
    LinearLayout secondLL;
    @BindView(R.id.tv_second)
    TextView secondTv;
    @BindView(R.id.rb_second_overall_merit)
    RatingBar secondOverallMeritRB;
    @BindView(R.id.tv_second_overall_merit_evaluate)
    TextView secondOverallMeritEvaluateTv;
    @BindView(R.id.ll_second_service_evaluate)
    LinearLayout secondServiceEvaluateLL;
    @BindView(R.id.rb_second_complete_on_time)
    RatingBar secondCompleteOnTimeRB;
    @BindView(R.id.tv_second_complete_on_time_evaluate)
    TextView secondCompleteOnTimeEvaluateTv;
    @BindView(R.id.rb_second_work_quality)
    RatingBar secondWorkQualityRB;
    @BindView(R.id.tv_second_work_quality_evaluate)
    TextView secondWorkQualityEvaluateTv;
    @BindView(R.id.rb_second_service_integrity)
    RatingBar secondServiceIntegrityRB;
    @BindView(R.id.tv_second_service_integrity_evaluate)
    TextView secondServiceIntegrityEvaluateTv;
    @BindView(R.id.tv_second_evaluate_content)
    TextView secondEvaluateContentTv;

    private int evaluateDetailsType = CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER;
    private int orderId = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_evaluate_details;
    }

    @Override
    protected EvaluateDetailsPresenter CreatePresenter() {
        return new EvaluateDetailsPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            evaluateDetailsType = intent.getIntExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER);
            orderId = intent.getIntExtra(CommonConstant.EXTRA_ORDER_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.evaluation_details));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        if (evaluateDetailsType == CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER) {
            firstTv.setText(getString(R.string.my_assessment_of_the_employer));
            secondTv.setText(getString(R.string.my_employer_assessment_of_me));

            firstServiceEvaluateLL.setVisibility(View.GONE);
        } else if (evaluateDetailsType == CommonConstant.EVALUATE_DETAILS_TYPE_SERVICE) {
            firstTv.setText(getString(R.string.my_evaluation_of_service_providers));
            secondTv.setText(getString(R.string.service_providers_evaluate_me));

            secondServiceEvaluateLL.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if (orderId != -1) {
            if (evaluateDetailsType == CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER) {
                mPresenter.fetchBuyerEvaluateDetailsRequest(orderId);
            } else if (evaluateDetailsType == CommonConstant.EVALUATE_DETAILS_TYPE_SERVICE) {
                mPresenter.fetchSellerEvaluateDetailsRequest(orderId);
            }
        }
    }

    @Override
    public void onShowEvaluateDetails(float buyerOverallMerit, String buyerEvaluateContent, float sellerOverallMerit, float sellerCompleteOnTime, float sellerWorkQuality, float sellerServiceIntegrity, String sellerEvaluateContent) {
        if (evaluateDetailsType == CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER) {
            if (!TextUtils.isEmpty(buyerEvaluateContent)) {
                firstOverallMeritRB.setRating(buyerOverallMerit);
                firstOverallMeritEvaluateTv.setText(EvaluateUtils.getEvaluation(buyerOverallMerit));
                firstEvaluateContentTv.setText(buyerEvaluateContent);
                firstLL.setVisibility(View.VISIBLE);
            } else {
                firstLL.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(sellerEvaluateContent)) {
                secondOverallMeritRB.setRating(sellerOverallMerit);
                secondOverallMeritEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerOverallMerit));
                secondCompleteOnTimeRB.setRating(sellerCompleteOnTime);
                secondCompleteOnTimeEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerCompleteOnTime));
                secondWorkQualityRB.setRating(sellerWorkQuality);
                secondWorkQualityEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerWorkQuality));
                secondServiceIntegrityRB.setRating(sellerServiceIntegrity);
                secondServiceIntegrityEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerServiceIntegrity));
                secondEvaluateContentTv.setText(sellerEvaluateContent);
                secondLL.setVisibility(View.VISIBLE);
            } else {
                secondLL.setVisibility(View.GONE);
            }
        } else if (evaluateDetailsType == CommonConstant.EVALUATE_DETAILS_TYPE_SERVICE) {
            if (!TextUtils.isEmpty(sellerEvaluateContent)) {
                firstOverallMeritRB.setRating(sellerOverallMerit);
                firstOverallMeritEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerOverallMerit));
                firstCompleteOnTimeRB.setRating(sellerCompleteOnTime);
                firstCompleteOnTimeEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerCompleteOnTime));
                firstWorkQualityRB.setRating(sellerWorkQuality);
                firstWorkQualityEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerWorkQuality));
                firstServiceIntegrityRB.setRating(sellerServiceIntegrity);
                firstServiceIntegrityEvaluateTv.setText(EvaluateUtils.getEvaluation(sellerServiceIntegrity));
                firstEvaluateContentTv.setText(sellerEvaluateContent);
                firstLL.setVisibility(View.VISIBLE);
            } else {
                firstLL.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(buyerEvaluateContent)) {
                secondOverallMeritRB.setRating(buyerOverallMerit);
                secondOverallMeritEvaluateTv.setText(EvaluateUtils.getEvaluation(buyerOverallMerit));
                secondEvaluateContentTv.setText(buyerEvaluateContent);

                secondLL.setVisibility(View.VISIBLE);
            } else {
                secondLL.setVisibility(View.GONE);
            }
        }
    }

}
