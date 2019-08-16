package com.allintask.lingdao.ui.activity.demand;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.IntelligentMatchInformServiceProviderBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.IntelligentMatchInformServiceProviderPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.utils.RandomUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.view.demand.IIntelligentMatchInformServiceProviderView;
import com.allintask.lingdao.widget.CircleImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/2/9.
 */

public class IntelligentMatchInformServiceProviderActivity extends BaseActivity<IIntelligentMatchInformServiceProviderView, IntelligentMatchInformServiceProviderPresenter> implements IIntelligentMatchInformServiceProviderView {

    private static final int MESSAGE_CODE_INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_publish_demand)
    TextView publishDemandTv;
    @BindView(R.id.tv_notice_facilitator_count)
    TextView noticeFacilitatorCountTv;
    @BindView(R.id.iv_radar_background)
    ImageView radarBackgroundIv;
    @BindView(R.id.iv_radar_pointer)
    ImageView radarPointerIv;
    @BindView(R.id.rl_radar_head_portrait)
    RelativeLayout radarHeadPortraitRL;
    @BindView(R.id.civ_left_first)
    CircleImageView leftFirstCIV;
    @BindView(R.id.civ_left_second)
    CircleImageView leftSecondCIV;
    @BindView(R.id.civ_left_third)
    CircleImageView leftThirdCIV;
    @BindView(R.id.civ_left_fourth)
    CircleImageView leftFourthCIV;
    @BindView(R.id.civ_right_first)
    CircleImageView rightFirstCIV;
    @BindView(R.id.civ_right_second)
    CircleImageView rightSecondCIV;
    @BindView(R.id.civ_right_third)
    CircleImageView rightThirdCIV;
    @BindView(R.id.civ_right_fourth)
    CircleImageView rightFourthCIV;
    @BindView(R.id.btn_go_to_choose)
    Button goToChooseBtn;

    private int demandId = -1;
    private String demandName;

    private int radarWidth;
    private int radarHeight;

    private int serviceProviderAmount = 8;
    private int[] randomArray;

    private int firstPosition;
    private int secondPosition;
    private int thirdPosition;
    private int fourthPosition;
    private int fifthPosition;
    private int sixthPosition;
    private int seventhPosition;
    private int eighthPosition;

    private ObjectAnimator leftFirstCircleImageViewAlphaAnimator;
    private ObjectAnimator leftSecondCircleImageViewAlphaAnimator;
    private ObjectAnimator leftThirdCircleImageViewAlphaAnimator;
    private ObjectAnimator leftFourthCircleImageViewAlphaAnimator;
    private ObjectAnimator rightFirstCircleImageViewAlphaAnimator;
    private ObjectAnimator rightSecondCircleImageViewAlphaAnimator;
    private ObjectAnimator rightThirdCircleImageViewAlphaAnimator;
    private ObjectAnimator rightFourthCircleImageViewAlphaAnimator;

    private ObjectAnimator leftFirstCircleImageViewScaleXAnimator;
    private ObjectAnimator leftSecondCircleImageViewScaleXAnimator;
    private ObjectAnimator leftThirdCircleImageViewScaleXAnimator;
    private ObjectAnimator leftFourthCircleImageViewScaleXAnimator;
    private ObjectAnimator rightFirstCircleImageViewScaleXAnimator;
    private ObjectAnimator rightSecondCircleImageViewScaleXAnimator;
    private ObjectAnimator rightThirdCircleImageViewScaleXAnimator;
    private ObjectAnimator rightFourthCircleImageViewScaleXAnimator;

    private ObjectAnimator leftFirstCircleImageViewScaleYAnimator;
    private ObjectAnimator leftSecondCircleImageViewScaleYAnimator;
    private ObjectAnimator leftThirdCircleImageViewScaleYAnimator;
    private ObjectAnimator leftFourthCircleImageViewScaleYAnimator;
    private ObjectAnimator rightFirstCircleImageViewScaleYAnimator;
    private ObjectAnimator rightSecondCircleImageViewScaleYAnimator;
    private ObjectAnimator rightThirdCircleImageViewScaleYAnimator;
    private ObjectAnimator rightFourthCircleImageViewScaleYAnimator;

    private List<String> firstImageUrlList;
    private List<String> secondImageUrlList;
    private List<String> thirdImageUrlList;

    private int time;

    private int facilitatorCount = 0;
    private Timer timer;
    private TimerTask timerTask;

    private final IntelligentMatchInformServiceProviderHandler intelligentMatchInformServiceProviderHandler = new IntelligentMatchInformServiceProviderHandler(this);

    private static class IntelligentMatchInformServiceProviderHandler extends Handler {

        private final WeakReference<Activity> activityWeakReference;

        public IntelligentMatchInformServiceProviderHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            IntelligentMatchInformServiceProviderActivity intelligentMatchInformServiceProviderActivity = (IntelligentMatchInformServiceProviderActivity) activityWeakReference.get();

            if (null != intelligentMatchInformServiceProviderActivity) {
                if (msg.what == MESSAGE_CODE_INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER) {
                    int intelligentMatchInformServiceProviderListSize = (int) msg.obj;
                    intelligentMatchInformServiceProviderActivity.facilitatorCount++;

                    StringBuilder stringBuilder = new StringBuilder("已通知");
                    stringBuilder.append(String.valueOf(intelligentMatchInformServiceProviderActivity.facilitatorCount)).append("个服务商");
                    intelligentMatchInformServiceProviderActivity.noticeFacilitatorCountTv.setText(stringBuilder);

                    if (intelligentMatchInformServiceProviderActivity.facilitatorCount >= intelligentMatchInformServiceProviderListSize) {
                        intelligentMatchInformServiceProviderActivity.timer.cancel();
                        intelligentMatchInformServiceProviderActivity.timerTask.cancel();
                    }
                }
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_intelligent_match_inform_service_provider;
    }

    @Override
    protected IntelligentMatchInformServiceProviderPresenter CreatePresenter() {
        return new IntelligentMatchInformServiceProviderPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            demandName = intent.getStringExtra(CommonConstant.EXTRA_DEMAND_NAME);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getParentContext(), MainActivity.class);
                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.SERVICE_MANAGEMENT_FRAGMENT);
                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
                startActivity(intent);

                finish();
            }
        });

        toolbar.setTitle("");

        titleTv.setText(getString(R.string.intelligent_match_inform_service_provider));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        StringBuilder stringBuilder = new StringBuilder("你发布的“").append(demandName).append("”需求");
        publishDemandTv.setText(stringBuilder);

        radarWidth = WindowUtils.getScreenWidth(getParentContext()) - DensityUtils.dip2px(getParentContext(), 32);
        radarHeight = radarWidth;

        RelativeLayout.LayoutParams radarBackgroundLayoutParams = (RelativeLayout.LayoutParams) radarBackgroundIv.getLayoutParams();
        radarBackgroundLayoutParams.width = radarWidth;
        radarBackgroundLayoutParams.height = radarHeight;
        radarBackgroundIv.setLayoutParams(radarBackgroundLayoutParams);

        RelativeLayout.LayoutParams radarPointLayoutParams = (RelativeLayout.LayoutParams) radarPointerIv.getLayoutParams();
        radarPointLayoutParams.width = radarWidth;
        radarPointLayoutParams.height = radarHeight;
        radarPointerIv.setLayoutParams(radarPointLayoutParams);

        RelativeLayout.LayoutParams headPortraitLayoutParams = (RelativeLayout.LayoutParams) radarHeadPortraitRL.getLayoutParams();
        headPortraitLayoutParams.width = radarWidth;
        headPortraitLayoutParams.height = radarHeight;
        radarHeadPortraitRL.setLayoutParams(headPortraitLayoutParams);

        RotateAnimation rotateAnimation = new RotateAnimation(0F, 360F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        radarPointerIv.startAnimation(rotateAnimation);
    }

    private void initData() {
        if (demandId != -1) {
            firstImageUrlList = new ArrayList<>();
            secondImageUrlList = new ArrayList<>();
            thirdImageUrlList = new ArrayList<>();

            mPresenter.fetchIntelligentMatchInformServiceProviderListRequest(demandId);
        }
    }

    private void initIntelligentMatchServiceProvider() {
        int headPortraitWidth = radarWidth / 10;
        int headPortraitHeight = headPortraitWidth;

        setCircleImageViewLayoutParams(leftFirstCIV, headPortraitWidth, headPortraitHeight, (int) (headPortraitWidth / 1.5), headPortraitWidth, 0, 0);
        setCircleImageViewLayoutParams(leftSecondCIV, headPortraitWidth, headPortraitHeight, (int) (headPortraitWidth / 0.25), (int) (headPortraitWidth / 0.6), 0, 0);
        setCircleImageViewLayoutParams(leftThirdCIV, headPortraitWidth, headPortraitHeight, (int) (headPortraitWidth / 0.75), (int) (headPortraitWidth / 0.375), 0, 0);
        setCircleImageViewLayoutParams(leftFourthCIV, headPortraitWidth, headPortraitHeight, (int) (headPortraitWidth / 0.75), (int) (headPortraitWidth / 0.2), 0, 0);
        setCircleImageViewLayoutParams(rightFirstCIV, headPortraitWidth, headPortraitHeight, 0, (int) (headPortraitWidth / 1.5), (int) (headPortraitWidth / 0.6), 0);
        setCircleImageViewLayoutParams(rightSecondCIV, headPortraitWidth, headPortraitHeight, 0, (int) (headPortraitWidth / 0.25), (int) (headPortraitWidth / 0.429), 0);
        setCircleImageViewLayoutParams(rightThirdCIV, headPortraitWidth, headPortraitHeight, 0, (int) (headPortraitWidth / 0.17), (int) (headPortraitWidth / 0.6), 0);
        setCircleImageViewLayoutParams(rightFourthCIV, headPortraitWidth, headPortraitHeight, 0, (int) (headPortraitWidth / 0.13), (int) (headPortraitWidth / 0.25), 0);

        leftFirstCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(leftFirstCIV, "alpha", 0F, 1F);
        leftFirstCircleImageViewAlphaAnimator.setDuration(1000);

        leftSecondCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(leftSecondCIV, "alpha", 0F, 1F);
        leftSecondCircleImageViewAlphaAnimator.setDuration(1000);

        leftThirdCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(leftThirdCIV, "alpha", 0F, 1F);
        leftThirdCircleImageViewAlphaAnimator.setDuration(1000);

        leftFourthCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(leftFourthCIV, "alpha", 0F, 1F);
        leftFourthCircleImageViewAlphaAnimator.setDuration(1000);

        rightFirstCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(rightFirstCIV, "alpha", 0F, 1F);
        rightFirstCircleImageViewAlphaAnimator.setDuration(1000);

        rightSecondCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(rightSecondCIV, "alpha", 0F, 1F);
        rightSecondCircleImageViewAlphaAnimator.setDuration(1000);

        rightThirdCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(rightThirdCIV, "alpha", 0F, 1F);
        rightThirdCircleImageViewAlphaAnimator.setDuration(1000);

        rightFourthCircleImageViewAlphaAnimator = ObjectAnimator.ofFloat(rightFourthCIV, "alpha", 0F, 1F);
        rightFourthCircleImageViewAlphaAnimator.setDuration(1000);

        leftFirstCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(leftFirstCIV, "scaleX", 1F, 1.3F, 1F);
        leftFirstCircleImageViewScaleXAnimator.setDuration(1000);
        leftFirstCircleImageViewScaleXAnimator.setRepeatCount(-1);

        leftSecondCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(leftSecondCIV, "scaleX", 1F, 1.3F, 1F);
        leftSecondCircleImageViewScaleXAnimator.setDuration(1000);
        leftSecondCircleImageViewScaleXAnimator.setRepeatCount(-1);

        leftThirdCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(leftThirdCIV, "scaleX", 1F, 1.3F, 1F);
        leftThirdCircleImageViewScaleXAnimator.setDuration(1000);
        leftThirdCircleImageViewScaleXAnimator.setRepeatCount(-1);

        leftFourthCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(leftFourthCIV, "scaleX", 1F, 1.3F, 1F);
        leftFourthCircleImageViewScaleXAnimator.setDuration(1000);
        leftFourthCircleImageViewScaleXAnimator.setRepeatCount(-1);

        rightFirstCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(rightFirstCIV, "scaleX", 1F, 1.3F, 1F);
        rightFirstCircleImageViewScaleXAnimator.setDuration(1000);
        rightFirstCircleImageViewScaleXAnimator.setRepeatCount(-1);

        rightSecondCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(rightSecondCIV, "scaleX", 1F, 1.5F, 1F);
        rightSecondCircleImageViewScaleXAnimator.setDuration(1000);
        rightSecondCircleImageViewScaleXAnimator.setRepeatCount(-1);

        rightThirdCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(rightThirdCIV, "scaleX", 1F, 1.3F, 1F);
        rightThirdCircleImageViewScaleXAnimator.setDuration(1000);
        rightThirdCircleImageViewScaleXAnimator.setRepeatCount(-1);

        rightFourthCircleImageViewScaleXAnimator = ObjectAnimator.ofFloat(rightFourthCIV, "scaleX", 1F, 1.3F, 1F);
        rightFourthCircleImageViewScaleXAnimator.setDuration(1000);
        rightFourthCircleImageViewScaleXAnimator.setRepeatCount(-1);

        leftFirstCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(leftFirstCIV, "scaleY", 1F, 1.3F, 1F);
        leftFirstCircleImageViewScaleYAnimator.setDuration(1000);
        leftFirstCircleImageViewScaleYAnimator.setRepeatCount(-1);

        leftSecondCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(leftSecondCIV, "scaleY", 1F, 1.3F, 1F);
        leftSecondCircleImageViewScaleYAnimator.setDuration(1000);
        leftSecondCircleImageViewScaleYAnimator.setRepeatCount(-1);

        leftThirdCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(leftThirdCIV, "scaleY", 1F, 1.3F, 1F);
        leftThirdCircleImageViewScaleYAnimator.setDuration(1000);
        leftThirdCircleImageViewScaleYAnimator.setRepeatCount(-1);

        leftFourthCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(leftFourthCIV, "scaleY", 1F, 1.3F, 1F);
        leftFourthCircleImageViewScaleYAnimator.setDuration(1000);
        leftFourthCircleImageViewScaleYAnimator.setRepeatCount(-1);

        rightFirstCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(rightFirstCIV, "scaleY", 1F, 1.5F, 1F);
        rightFirstCircleImageViewScaleYAnimator.setDuration(1000);
        rightFirstCircleImageViewScaleYAnimator.setRepeatCount(-1);

        rightSecondCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(rightSecondCIV, "scaleY", 1F, 1.5F, 1F);
        rightSecondCircleImageViewScaleYAnimator.setDuration(1000);
        rightSecondCircleImageViewScaleYAnimator.setRepeatCount(-1);

        rightThirdCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(rightThirdCIV, "scaleY", 1F, 1.3F, 1F);
        rightThirdCircleImageViewScaleYAnimator.setDuration(1000);
        rightThirdCircleImageViewScaleYAnimator.setRepeatCount(-1);

        rightFourthCircleImageViewScaleYAnimator = ObjectAnimator.ofFloat(rightFourthCIV, "scaleY", 1F, 1.3F, 1F);
        rightFourthCircleImageViewScaleYAnimator.setDuration(1000);
        rightFourthCircleImageViewScaleYAnimator.setRepeatCount(-1);

        if (null != firstImageUrlList && firstImageUrlList.size() > 0) {
            setTime(firstImageUrlList);
            time = CommonConstant.INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_FIRST_TIME;
        }
    }

    private void setNoticeFacilitatorTimer(final int intelligentMatchInformServiceProviderListSize) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = intelligentMatchInformServiceProviderHandler.obtainMessage();
                msg.what = MESSAGE_CODE_INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER;
                msg.obj = intelligentMatchInformServiceProviderListSize;
                intelligentMatchInformServiceProviderHandler.sendMessage(msg);
            }
        };

        timer.schedule(timerTask, 1000, 1000);
    }

    private void setCircleImageViewLayoutParams(CircleImageView circleImageView, int width, int height, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) circleImageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.leftMargin = leftMargin;
        layoutParams.topMargin = topMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.bottomMargin = bottomMargin;
        circleImageView.setLayoutParams(layoutParams);
    }

    private void setCircleImageViewObjectAnimator(List<ObjectAnimator> firstPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> secondPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> thirdPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> fourthPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> fifthPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> sixthPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> seventhPositionCircleImageViewObjectAnimatorList, final List<ObjectAnimator> eighthPositionCircleImageViewObjectAnimatorList) {
        if (null != firstPositionCircleImageViewObjectAnimatorList && firstPositionCircleImageViewObjectAnimatorList.size() > 0) {
            ObjectAnimator firstPositionCircleImageViewAlphaAnimator = firstPositionCircleImageViewObjectAnimatorList.get(0);
            final ObjectAnimator firstPositionCircleImageViewScaleXAnimator = firstPositionCircleImageViewObjectAnimatorList.get(1);
            final ObjectAnimator firstPositionCircleImageViewScaleYAnimator = firstPositionCircleImageViewObjectAnimatorList.get(2);

            firstPositionCircleImageViewAlphaAnimator.start();
            firstPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    firstPositionCircleImageViewScaleXAnimator.start();
                    firstPositionCircleImageViewScaleYAnimator.start();

                    if (null != secondPositionCircleImageViewObjectAnimatorList && secondPositionCircleImageViewObjectAnimatorList.size() > 0) {
                        ObjectAnimator secondPositionCircleImageViewAlphaAnimator = secondPositionCircleImageViewObjectAnimatorList.get(0);
                        final ObjectAnimator secondPositionCircleImageViewScaleXAnimator = secondPositionCircleImageViewObjectAnimatorList.get(1);
                        final ObjectAnimator secondPositionCircleImageViewScaleYAnimator = secondPositionCircleImageViewObjectAnimatorList.get(2);

                        secondPositionCircleImageViewAlphaAnimator.start();
                        secondPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                secondPositionCircleImageViewScaleXAnimator.start();
                                secondPositionCircleImageViewScaleYAnimator.start();

                                if (null != thirdPositionCircleImageViewObjectAnimatorList && thirdPositionCircleImageViewObjectAnimatorList.size() > 0) {
                                    ObjectAnimator thirdPositionCircleImageViewAlphaAnimator = thirdPositionCircleImageViewObjectAnimatorList.get(0);
                                    final ObjectAnimator thirdPositionCircleImageViewScaleXAnimator = thirdPositionCircleImageViewObjectAnimatorList.get(1);
                                    final ObjectAnimator thirdPositionCircleImageViewScaleYAnimator = thirdPositionCircleImageViewObjectAnimatorList.get(2);

                                    thirdPositionCircleImageViewAlphaAnimator.start();
                                    thirdPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);

                                            thirdPositionCircleImageViewScaleXAnimator.start();
                                            thirdPositionCircleImageViewScaleYAnimator.start();

                                            if (null != fourthPositionCircleImageViewObjectAnimatorList && fourthPositionCircleImageViewObjectAnimatorList.size() > 0) {
                                                ObjectAnimator fourthPositionCircleImageViewAlphaAnimator = fourthPositionCircleImageViewObjectAnimatorList.get(0);
                                                final ObjectAnimator fourthPositionCircleImageViewScaleXAnimator = fourthPositionCircleImageViewObjectAnimatorList.get(1);
                                                final ObjectAnimator fourthPositionCircleImageViewScaleYAnimator = fourthPositionCircleImageViewObjectAnimatorList.get(2);

                                                fourthPositionCircleImageViewAlphaAnimator.start();
                                                fourthPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        super.onAnimationEnd(animation);

                                                        fourthPositionCircleImageViewScaleXAnimator.start();
                                                        fourthPositionCircleImageViewScaleYAnimator.start();

                                                        if (null != fifthPositionCircleImageViewObjectAnimatorList && fifthPositionCircleImageViewObjectAnimatorList.size() > 0) {
                                                            ObjectAnimator fifthPositionCircleImageViewAlphaAnimator = fifthPositionCircleImageViewObjectAnimatorList.get(0);
                                                            final ObjectAnimator fifthPositionCircleImageViewScaleXAnimator = fifthPositionCircleImageViewObjectAnimatorList.get(1);
                                                            final ObjectAnimator fifthPositionCircleImageViewScaleYAnimator = fifthPositionCircleImageViewObjectAnimatorList.get(2);

                                                            fifthPositionCircleImageViewAlphaAnimator.start();
                                                            fifthPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                                                                @Override
                                                                public void onAnimationEnd(Animator animation) {
                                                                    super.onAnimationEnd(animation);

                                                                    fifthPositionCircleImageViewScaleXAnimator.start();
                                                                    fifthPositionCircleImageViewScaleYAnimator.start();

                                                                    if (null != sixthPositionCircleImageViewObjectAnimatorList && sixthPositionCircleImageViewObjectAnimatorList.size() > 0) {
                                                                        ObjectAnimator sixthPositionCircleImageViewAlphaAnimator = sixthPositionCircleImageViewObjectAnimatorList.get(0);
                                                                        final ObjectAnimator sixthPositionCircleImageViewScaleXAnimator = sixthPositionCircleImageViewObjectAnimatorList.get(1);
                                                                        final ObjectAnimator sixthPositionCircleImageViewScaleYAnimator = sixthPositionCircleImageViewObjectAnimatorList.get(2);

                                                                        sixthPositionCircleImageViewAlphaAnimator.start();
                                                                        sixthPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                                                                            @Override
                                                                            public void onAnimationEnd(Animator animation) {
                                                                                super.onAnimationEnd(animation);

                                                                                sixthPositionCircleImageViewScaleXAnimator.start();
                                                                                sixthPositionCircleImageViewScaleYAnimator.start();

                                                                                if (null != seventhPositionCircleImageViewObjectAnimatorList && secondPositionCircleImageViewObjectAnimatorList.size() > 0) {
                                                                                    ObjectAnimator seventhPositionCircleImageViewAlphaAnimator = seventhPositionCircleImageViewObjectAnimatorList.get(0);
                                                                                    final ObjectAnimator seventhPositionCircleImageViewScaleXAnimator = seventhPositionCircleImageViewObjectAnimatorList.get(1);
                                                                                    final ObjectAnimator seventhPositionCircleImageViewScaleYAnimator = seventhPositionCircleImageViewObjectAnimatorList.get(2);

                                                                                    seventhPositionCircleImageViewAlphaAnimator.start();
                                                                                    seventhPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                                                                                        @Override
                                                                                        public void onAnimationEnd(Animator animation) {
                                                                                            super.onAnimationEnd(animation);

                                                                                            seventhPositionCircleImageViewScaleXAnimator.start();
                                                                                            seventhPositionCircleImageViewScaleYAnimator.start();

                                                                                            if (null != eighthPositionCircleImageViewObjectAnimatorList && eighthPositionCircleImageViewObjectAnimatorList.size() > 0) {
                                                                                                ObjectAnimator eighthPositionCircleImageViewAlphaAnimator = eighthPositionCircleImageViewObjectAnimatorList.get(0);
                                                                                                final ObjectAnimator eighthPositionCircleImageViewScaleXAnimator = eighthPositionCircleImageViewObjectAnimatorList.get(1);
                                                                                                final ObjectAnimator eighthPositionCircleImageViewScaleYAnimator = eighthPositionCircleImageViewObjectAnimatorList.get(2);

                                                                                                eighthPositionCircleImageViewAlphaAnimator.start();
                                                                                                eighthPositionCircleImageViewAlphaAnimator.addListener(new AnimatorListenerAdapter() {
                                                                                                    @Override
                                                                                                    public void onAnimationEnd(Animator animation) {
                                                                                                        super.onAnimationEnd(animation);

                                                                                                        eighthPositionCircleImageViewScaleXAnimator.start();
                                                                                                        eighthPositionCircleImageViewScaleYAnimator.start();

                                                                                                        if (time == CommonConstant.INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_SECOND_TIME && null != thirdImageUrlList && thirdImageUrlList.size() > 0) {
                                                                                                            setTime(thirdImageUrlList);
                                                                                                            time = CommonConstant.INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_THIRD_TIME;
                                                                                                        }

                                                                                                        if (time == CommonConstant.INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_FIRST_TIME && null != secondImageUrlList && secondImageUrlList.size() > 0) {
                                                                                                            setTime(secondImageUrlList);
                                                                                                            time = CommonConstant.INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_SECOND_TIME;
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private List<ObjectAnimator> getCircleImageViewObjectAnimatorList(int position) {
        List<ObjectAnimator> objectAnimatorList = new ArrayList<>();

        ObjectAnimator alphaAnimator = null;
        ObjectAnimator scaleXAnimator = null;
        ObjectAnimator scaleYAnimator = null;

        switch (position) {
            case 0:
                alphaAnimator = leftFirstCircleImageViewAlphaAnimator;
                scaleXAnimator = leftFirstCircleImageViewScaleXAnimator;
                scaleYAnimator = leftFirstCircleImageViewScaleYAnimator;
                break;

            case 1:
                alphaAnimator = leftSecondCircleImageViewAlphaAnimator;
                scaleXAnimator = leftSecondCircleImageViewScaleXAnimator;
                scaleYAnimator = leftSecondCircleImageViewScaleYAnimator;
                break;

            case 2:
                alphaAnimator = leftThirdCircleImageViewAlphaAnimator;
                scaleXAnimator = leftThirdCircleImageViewScaleXAnimator;
                scaleYAnimator = leftThirdCircleImageViewScaleYAnimator;
                break;

            case 3:
                alphaAnimator = leftFourthCircleImageViewAlphaAnimator;
                scaleXAnimator = leftFourthCircleImageViewScaleXAnimator;
                scaleYAnimator = leftFourthCircleImageViewScaleYAnimator;
                break;

            case 4:
                alphaAnimator = rightFirstCircleImageViewAlphaAnimator;
                scaleXAnimator = rightFirstCircleImageViewScaleXAnimator;
                scaleYAnimator = rightFirstCircleImageViewScaleYAnimator;
                break;

            case 5:
                alphaAnimator = rightSecondCircleImageViewAlphaAnimator;
                scaleXAnimator = rightSecondCircleImageViewScaleXAnimator;
                scaleYAnimator = rightSecondCircleImageViewScaleYAnimator;
                break;

            case 6:
                alphaAnimator = rightThirdCircleImageViewAlphaAnimator;
                scaleXAnimator = rightThirdCircleImageViewScaleXAnimator;
                scaleYAnimator = rightThirdCircleImageViewScaleYAnimator;
                break;

            case 7:
                alphaAnimator = rightFourthCircleImageViewAlphaAnimator;
                scaleXAnimator = rightFourthCircleImageViewScaleXAnimator;
                scaleYAnimator = rightFourthCircleImageViewScaleYAnimator;
                break;
        }

        objectAnimatorList.add(alphaAnimator);
        objectAnimatorList.add(scaleXAnimator);
        objectAnimatorList.add(scaleYAnimator);
        return objectAnimatorList;
    }

    private void setCircleImageView(int position, String imageUrl) {
        switch (position) {
            case 0:
                if (null != leftFirstCIV) {
                    ImageViewUtil.setImageView(getParentContext(), leftFirstCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 1:
                if (null != leftSecondCIV) {
                    ImageViewUtil.setImageView(getParentContext(), leftSecondCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 2:
                if (null != leftThirdCIV) {
                    ImageViewUtil.setImageView(getParentContext(), leftThirdCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 3:
                if (null != leftFourthCIV) {
                    ImageViewUtil.setImageView(getParentContext(), leftFourthCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 4:
                if (null != rightFirstCIV) {
                    ImageViewUtil.setImageView(getParentContext(), rightFirstCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 5:
                if (null != rightSecondCIV) {
                    ImageViewUtil.setImageView(getParentContext(), rightSecondCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 6:
                if (null != rightThirdCIV) {
                    ImageViewUtil.setImageView(getParentContext(), rightThirdCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;

            case 7:
                if (null != rightFourthCIV) {
                    ImageViewUtil.setImageView(getParentContext(), rightFourthCIV, imageUrl, R.mipmap.ic_default_avatar);
                }
                break;
        }
    }

    private void setTime(List<String> imageUrlList) {
        for (int i = 0; i < imageUrlList.size(); i++) {
            String imageUrl = TypeUtils.getString(imageUrlList.get(i), "");

            switch (i) {
                case 0:
                    setCircleImageView(firstPosition, imageUrl);
                    break;

                case 1:
                    setCircleImageView(secondPosition, imageUrl);
                    break;

                case 2:
                    setCircleImageView(thirdPosition, imageUrl);
                    break;

                case 3:
                    setCircleImageView(fourthPosition, imageUrl);
                    break;

                case 4:
                    setCircleImageView(fifthPosition, imageUrl);
                    break;

                case 5:
                    setCircleImageView(sixthPosition, imageUrl);
                    break;

                case 6:
                    setCircleImageView(seventhPosition, imageUrl);
                    break;

                case 7:
                    setCircleImageView(eighthPosition, imageUrl);
                    break;
            }
        }

        switch (imageUrlList.size()) {
            case 1:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), null, null, null, null, null, null, null);
                break;

            case 2:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), null, null, null, null, null, null);
                break;

            case 3:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), getCircleImageViewObjectAnimatorList(thirdPosition), null, null, null, null, null);
                break;

            case 4:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), getCircleImageViewObjectAnimatorList(thirdPosition), getCircleImageViewObjectAnimatorList(fourthPosition), null, null, null, null);
                break;

            case 5:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), getCircleImageViewObjectAnimatorList(thirdPosition), getCircleImageViewObjectAnimatorList(fourthPosition), getCircleImageViewObjectAnimatorList(fifthPosition), null, null, null);
                break;

            case 6:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), getCircleImageViewObjectAnimatorList(thirdPosition), getCircleImageViewObjectAnimatorList(fourthPosition), getCircleImageViewObjectAnimatorList(fifthPosition), getCircleImageViewObjectAnimatorList(sixthPosition), null, null);
                break;

            case 7:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), getCircleImageViewObjectAnimatorList(thirdPosition), getCircleImageViewObjectAnimatorList(fourthPosition), getCircleImageViewObjectAnimatorList(fifthPosition), getCircleImageViewObjectAnimatorList(sixthPosition), getCircleImageViewObjectAnimatorList(seventhPosition), null);
                break;

            case 8:
                setCircleImageViewObjectAnimator(getCircleImageViewObjectAnimatorList(firstPosition), getCircleImageViewObjectAnimatorList(secondPosition), getCircleImageViewObjectAnimatorList(thirdPosition), getCircleImageViewObjectAnimatorList(fourthPosition), getCircleImageViewObjectAnimatorList(fifthPosition), getCircleImageViewObjectAnimatorList(sixthPosition), getCircleImageViewObjectAnimatorList(seventhPosition), getCircleImageViewObjectAnimatorList(eighthPosition));
                break;
        }
    }

    private void destroyObjectAnimator(ObjectAnimator objectAnimator) {
        if (null != objectAnimator) {
            objectAnimator.cancel();
            objectAnimator = null;
        }
    }

    private void sendMessage(int userId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_recommend_content), String.valueOf(userId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_RECOMMEND);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_recommend_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_recommend_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_recommend_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_dispose));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.btn_go_to_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_to_choose:
                Intent intent = new Intent(getParentContext(), MainActivity.class);
                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
                intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onShowIntelligentMatchInformServiceProviderList(final List<IntelligentMatchInformServiceProviderBean> intelligentMatchInformServiceProviderList) {
        if (null != intelligentMatchInformServiceProviderList && intelligentMatchInformServiceProviderList.size() > 0) {
            randomArray = RandomUtils.randomArray(0, serviceProviderAmount - 1, serviceProviderAmount);

            if (null != randomArray) {
                firstPosition = randomArray[0];
                secondPosition = randomArray[1];
                thirdPosition = randomArray[2];
                fourthPosition = randomArray[3];
                fifthPosition = randomArray[4];
                sixthPosition = randomArray[5];
                seventhPosition = randomArray[6];
                eighthPosition = randomArray[7];
            }

            for (int i = 0; i < intelligentMatchInformServiceProviderList.size(); i++) {
                IntelligentMatchInformServiceProviderBean intelligentMatchInformServiceProviderBean = intelligentMatchInformServiceProviderList.get(i);

                if (null != intelligentMatchInformServiceProviderBean) {
                    int userId = TypeUtils.getInteger(intelligentMatchInformServiceProviderBean.userId, -1);
                    String avatarUrl = TypeUtils.getString(intelligentMatchInformServiceProviderBean.avatarUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    if (i <= 7) {
                        firstImageUrlList.add(imageUrl);
                    } else if (i <= 15) {
                        secondImageUrlList.add(imageUrl);
                    } else {
                        thirdImageUrlList.add(imageUrl);
                    }

                    sendMessage(userId);
                }
            }

            noticeFacilitatorCountTv.setText("已通知1个服务商");

            setNoticeFacilitatorTimer(intelligentMatchInformServiceProviderList.size());
            initIntelligentMatchServiceProvider();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getParentContext(), MainActivity.class);
        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (null != timer) {
            timer.cancel();
        }

        if (null != timerTask) {
            timerTask.cancel();
        }

        destroyObjectAnimator(leftFirstCircleImageViewAlphaAnimator);
        destroyObjectAnimator(leftSecondCircleImageViewAlphaAnimator);
        destroyObjectAnimator(leftThirdCircleImageViewAlphaAnimator);
        destroyObjectAnimator(leftFourthCircleImageViewAlphaAnimator);
        destroyObjectAnimator(rightFirstCircleImageViewAlphaAnimator);
        destroyObjectAnimator(rightSecondCircleImageViewAlphaAnimator);
        destroyObjectAnimator(rightThirdCircleImageViewAlphaAnimator);
        destroyObjectAnimator(rightFourthCircleImageViewAlphaAnimator);
        destroyObjectAnimator(leftFirstCircleImageViewScaleXAnimator);
        destroyObjectAnimator(leftSecondCircleImageViewScaleXAnimator);
        destroyObjectAnimator(leftThirdCircleImageViewScaleXAnimator);
        destroyObjectAnimator(leftFourthCircleImageViewScaleXAnimator);
        destroyObjectAnimator(rightFirstCircleImageViewScaleXAnimator);
        destroyObjectAnimator(rightSecondCircleImageViewScaleXAnimator);
        destroyObjectAnimator(rightThirdCircleImageViewScaleXAnimator);
        destroyObjectAnimator(rightFourthCircleImageViewScaleXAnimator);
        destroyObjectAnimator(leftFirstCircleImageViewScaleYAnimator);
        destroyObjectAnimator(leftSecondCircleImageViewScaleYAnimator);
        destroyObjectAnimator(leftThirdCircleImageViewScaleYAnimator);
        destroyObjectAnimator(leftFourthCircleImageViewScaleYAnimator);
        destroyObjectAnimator(rightFirstCircleImageViewScaleYAnimator);
        destroyObjectAnimator(rightSecondCircleImageViewScaleYAnimator);
        destroyObjectAnimator(rightThirdCircleImageViewScaleYAnimator);
        destroyObjectAnimator(rightFourthCircleImageViewScaleYAnimator);

        super.onDestroy();
    }

}
