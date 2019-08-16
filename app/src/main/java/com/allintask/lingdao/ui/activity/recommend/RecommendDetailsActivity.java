package com.allintask.lingdao.ui.activity.recommend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.bean.recommend.RecommendDetailsPersonalInformationAdapter;
import com.allintask.lingdao.bean.recommend.RecommendDetailsServiceInformationBean;
import com.allintask.lingdao.bean.recommend.RecommendDetailsTopBean;
import com.allintask.lingdao.bean.recommend.RecommendDetailsUserInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.recommend.RecommendDetailsPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.main.PublishDemandActivity;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.activity.user.MyPhotoAlbumActivity;
import com.allintask.lingdao.ui.activity.user.PhotoAlbumActivity;
import com.allintask.lingdao.ui.activity.user.ReportActivity;
import com.allintask.lingdao.ui.adapter.recommend.EvaluateAdapter;
import com.allintask.lingdao.ui.adapter.recommend.RecommendDetailsServiceWayPriceUnitAdapter;
import com.allintask.lingdao.ui.adapter.recommend.RecommendDetailsTopAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.utils.MediaRecordPlayerUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.version.DownloadService;
import com.allintask.lingdao.view.recommend.IRecommendDetailsView;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/4.
 */

public class RecommendDetailsActivity extends BaseActivity<IRecommendDetailsView, RecommendDetailsPresenter> implements IRecommendDetailsView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_first)
    ImageView rightFirstIv;
    @BindView(R.id.iv_right_second)
    ImageView rightSecondIv;
    @BindView(R.id.tv_time)
    TextView timeTv;
    @BindView(R.id.civ_head_portrait)
    CircleImageView headPortraitCIV;
    @BindView(R.id.iv_specialist_recommend)
    ImageView specialistRecommendIv;
    @BindView(R.id.tv_name)
    TextView nameTv;
    @BindView(R.id.iv_gender)
    ImageView genderIv;
    @BindView(R.id.tv_age)
    TextView ageTv;
    @BindView(R.id.tv_work_experience_year)
    TextView workExperienceYearTv;
    @BindView(R.id.tv_location)
    TextView locationTv;
    @BindView(R.id.tv_distance)
    TextView distanceTv;
    @BindView(R.id.ll_personal_album)
    LinearLayout personalAlbumLL;
    @BindView(R.id.ll_personal_album_details)
    LinearLayout personalAlbumDetailsLL;
    @BindView(R.id.iv_first_personal_photo)
    ImageView firstPersonalPhotoIv;
    @BindView(R.id.iv_second_personal_photo)
    ImageView secondPersonalPhotoIv;
    @BindView(R.id.iv_third_personal_photo)
    ImageView thirdPersonalPhotoIv;
    @BindView(R.id.iv_fourth_personal_photo)
    ImageView fourthPersonalPhotoIv;
    @BindView(R.id.ll_seeing)
    LinearLayout seeingLL;
    @BindView(R.id.civ_first_seeing)
    CircleImageView firstSeeingCIV;
    @BindView(R.id.civ_second_seeing)
    CircleImageView secondSeeingCIV;
    @BindView(R.id.civ_third_seeing)
    CircleImageView thirdSeeingCIV;
    @BindView(R.id.civ_fourth_seeing)
    CircleImageView fourthSeeingCIV;
    @BindView(R.id.civ_fifth_seeing)
    CircleImageView fifthSeeingCIV;
    @BindView(R.id.civ_sixth_seeing)
    CircleImageView sixthSeeingCIV;
    @BindView(R.id.tv_number_of_people_seeing)
    TextView numberOfPeopleSeeingTv;
    @BindView(R.id.ll_my_service)
    LinearLayout myServiceLL;
    @BindView(R.id.rv_my_service)
    RecyclerView myServiceRV;
    @BindView(R.id.ll_skills_album)
    LinearLayout skillsAlbumLL;
    @BindView(R.id.ll_skills_album_details)
    LinearLayout skillsAlbumDetailsLL;
    @BindView(R.id.iv_first_skill_photo)
    ImageView firstSkillPhotoIv;
    @BindView(R.id.iv_second_skill_photo)
    ImageView secondSkillPhotoIv;
    @BindView(R.id.iv_third_skill_photo)
    ImageView thirdSkillPhotoIv;
    @BindView(R.id.iv_fourth_skill_photo)
    ImageView fourthSkillPhotoIv;
    @BindView(R.id.ll_voice_introduce)
    LinearLayout voiceIntroduceLL;
    @BindView(R.id.ll_voice_demo)
    LinearLayout voiceDemoLL;
    @BindView(R.id.iv_voice_demo)
    ImageView voiceDemoIv;
    @BindView(R.id.tv_voice_demo_time)
    TextView voiceDemoTimeTv;
    @BindView(R.id.ll_good_at_skills)
    LinearLayout goodAtSkillsLL;
    @BindView(R.id.tv_good_at_skills)
    TextView goodAtSkillsTv;
    @BindView(R.id.crv_service_way_price_unit)
    CommonRecyclerView serviceWayPriceUnitCRV;
    @BindView(R.id.ll_service_city)
    LinearLayout serviceCityLL;
    @BindView(R.id.tv_service_city)
    TextView serviceCityTv;
    @BindView(R.id.ll_service_introduction)
    LinearLayout serviceIntroductionLL;
    @BindView(R.id.tv_service_introduction)
    TextView serviceIntroductionTv;
    @BindView(R.id.ll_my_advantage)
    LinearLayout myAdvantageLL;
    @BindView(R.id.tv_my_advantage)
    TextView myAdvantageTv;
    @BindView(R.id.ll_educational_experience)
    LinearLayout educationalExperienceLL;
    @BindView(R.id.crv_educational_experience)
    CommonRecyclerView educationalExperienceCRV;
    @BindView(R.id.ll_work_experience)
    LinearLayout workExperienceLL;
    @BindView(R.id.crv_work_experience)
    CommonRecyclerView workExperienceCRV;
    @BindView(R.id.ll_honor_and_qualification)
    LinearLayout honorAndQualificationLL;
    @BindView(R.id.crv_honor_and_qualification)
    CommonRecyclerView honorAndQualificationCRV;
    @BindView(R.id.tv_recommend_details_bottom)
    TextView recommendDetailsBottomTv;
    @BindView(R.id.ll_evaluate)
    LinearLayout evaluateLL;
    @BindView(R.id.crv_service_evaluate)
    CommonRecyclerView serviceEvaluateCRV;
    @BindView(R.id.ll_see_more_evaluate)
    LinearLayout seeMoreConclusionLL;
    @BindView(R.id.tv_evaluate_amount)
    TextView evaluateAmountTv;
    @BindView(R.id.ll_bottom)
    LinearLayout bottomLL;
    @BindView(R.id.rl_chat)
    RelativeLayout chatRL;
    @BindView(R.id.rl_subscribe)
    RelativeLayout subscribeRL;
    @BindView(R.id.ll_like)
    LinearLayout likeLL;
    @BindView(R.id.iv_like)
    ImageView likeIv;
    @BindView(R.id.tv_like)
    TextView likeTv;

    private int userId = -1;
    private int serviceId = -1;

    private RecommendDetailsActivityReceiver recommendDetailsActivityReceiver;
    private int myUserId;

    private List<RecommendDetailsTopBean> myServiceList;
    private RecommendDetailsTopAdapter recommendDetailsTopAdapter;
    private List<RecommendDetailsServiceInformationBean.PersonalAlbumBean> personalAlbumList;
    private List<RecommendDetailsServiceInformationBean.ServiceDetailsBean> serviceDetailsList;
    private RecommendDetailsServiceWayPriceUnitAdapter recommendDetailsServiceWayPriceUnitAdapter;
    private RecommendDetailsPersonalInformationAdapter recommendDetailsEducationalExperienceAdapter;
    private RecommendDetailsPersonalInformationAdapter recommendDetailsWorkExperienceAdapter;
    private RecommendDetailsPersonalInformationAdapter recommendDetailsHonorAndQualificationAdapter;
    private EvaluateAdapter evaluateAdapter;

    private RecommendDetailsServiceInformationBean.ServiceDetailsBean serviceDetailsBean;

    private int categoryId = -1;
    private String userHeadPortraitUrl;
    private String name;
    private int gender = -1;
    private String age;
    private String categoryName;
    private int voiceDuration;
    private String distance;
    private ArrayList<Integer> serviceWayIdArrayList;
    private ArrayList<String> servicePriceArrayList;
    private String serviceWayPriceUnit;
    private String serviceWay;
    private String servicePrice;
    private String serviceUnit;
    private String advantage;
    private boolean isLike = false;
    private int likeAmount = 0;
    private boolean isCollect = false;

    private File voiceFile;
    private String downloadVoiceFilePath;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_details;
    }

    @Override
    protected RecommendDetailsPresenter CreatePresenter() {
        return new RecommendDetailsPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            serviceId = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_ID, -1);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setVisibility(View.GONE);

        rightSecondIv.setImageResource(R.mipmap.ic_more);
        rightSecondIv.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        registerRecommendDetailsActivityReceiver();

        myUserId = UserPreferences.getInstance().getUserId();
        myServiceList = new ArrayList<>();
        initMyServiceRecyclerView();

        recommendDetailsServiceWayPriceUnitAdapter = new RecommendDetailsServiceWayPriceUnitAdapter(getParentContext());
        serviceWayPriceUnitCRV.setAdapter(recommendDetailsServiceWayPriceUnitAdapter);

        recommendDetailsEducationalExperienceAdapter = new RecommendDetailsPersonalInformationAdapter(getParentContext(), CommonConstant.EDUCATIONAL_EXPERIENCE);
        educationalExperienceCRV.setAdapter(recommendDetailsEducationalExperienceAdapter);

        recommendDetailsWorkExperienceAdapter = new RecommendDetailsPersonalInformationAdapter(getParentContext(), CommonConstant.WORK_EXPERIENCE);
        workExperienceCRV.setAdapter(recommendDetailsWorkExperienceAdapter);

        recommendDetailsHonorAndQualificationAdapter = new RecommendDetailsPersonalInformationAdapter(getParentContext(), CommonConstant.HONOR_AND_QUALIFICATION);
        honorAndQualificationCRV.setAdapter(recommendDetailsHonorAndQualificationAdapter);

        evaluateAdapter = new EvaluateAdapter(getParentContext());
        serviceEvaluateCRV.setAdapter(evaluateAdapter);

        recommendDetailsBottomTv.setText("1.平台托管薪资，工作完成后平台才打款给人才\n2.付款后，企业可以通过私聊方式与人才沟通服务细节\n3.企业确认开始，人才才开始工作，否则订单无效\n4.如对服务不满意，评价生效前可协商申请退款\n如有疑问，联系客服微信Allintask");
    }

    private void initData() {
        serviceWayIdArrayList = new ArrayList<>();
        servicePriceArrayList = new ArrayList<>();

        mPresenter.fetchRecommendDetailsUserInformationRequest(userId);
        mPresenter.fetchRecommendServiceInformationRequest(userId);
        mPresenter.fetchRecommendEvaluateRequest(userId);
    }

    private void registerRecommendDetailsActivityReceiver() {
        recommendDetailsActivityReceiver = new RecommendDetailsActivityReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_DOWNLOAD_VOICE_SUCCESS);

        registerReceiver(recommendDetailsActivityReceiver, intentFilter);
    }

    private void initMyServiceRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentContext(), LinearLayoutManager.HORIZONTAL, false);
        myServiceRV.setLayoutManager(linearLayoutManager);

        recommendDetailsTopAdapter = new RecommendDetailsTopAdapter(getParentContext());
        myServiceRV.setAdapter(recommendDetailsTopAdapter);

        recommendDetailsTopAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AnimationDrawable animationDrawable = (AnimationDrawable) voiceDemoIv.getBackground();

                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();

                    MediaRecordPlayerUtils.getInstance().pause();
                    MediaRecordPlayerUtils.getInstance().release();

                    Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                    voiceDemoIv.setBackground(drawable);
                }

                int itemCount = recommendDetailsTopAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    RecommendDetailsTopBean recommendDetailsTopBean = (RecommendDetailsTopBean) recommendDetailsTopAdapter.getItem(i);

                    if (null != recommendDetailsTopBean && i == position) {
                        recommendDetailsTopBean.isSelected = true;
                    } else {
                        recommendDetailsTopBean.isSelected = false;
                    }
                }

                if (null != recommendDetailsTopAdapter) {
                    recommendDetailsTopAdapter.notifyDataSetChanged();
                }

                if (null != serviceDetailsList && serviceDetailsList.size() > 0) {
                    serviceDetailsBean = serviceDetailsList.get(position);

                    if (null != serviceDetailsBean) {
                        serviceId = TypeUtils.getInteger(serviceDetailsBean.serveId, -1);
                        categoryId = TypeUtils.getInteger(serviceDetailsBean.categoryId, -1);
                        categoryName = TypeUtils.getString(serviceDetailsBean.categoryName, "");
                        List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean> serveAlbumList = serviceDetailsBean.serveAlbumList;
                        String voiceUrl = TypeUtils.getString(serviceDetailsBean.voiceUrl, "");
                        voiceDuration = TypeUtils.getInteger(serviceDetailsBean.voiceDuration, -1);
                        List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.CategoryPropertyChineseBean> categoryPropertyChineseList = serviceDetailsBean.categoryPropertyChineseList;
                        String province = TypeUtils.getString(serviceDetailsBean.province, "");
                        String city = TypeUtils.getString(serviceDetailsBean.city, "");
                        String introduce = TypeUtils.getString(serviceDetailsBean.introduce, "");
                        advantage = TypeUtils.getString(serviceDetailsBean.advantage, "");
                        List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean> serveWayPriceUnitChineseList = serviceDetailsBean.serveWayPriceUnitChineseList;

                        if (null != serveAlbumList && serveAlbumList.size() > 0) {
                            List<String> serviceAlbumStringList = new ArrayList<>();

                            for (int i = 0; i < 6; i++) {
                                if (i < serveAlbumList.size()) {
                                    RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean serveAlbumBean = serveAlbumList.get(i);

                                    if (null != serveAlbumBean) {
                                        String tempImageUrl = TypeUtils.getString(serveAlbumBean.imageUrl, "");

                                        String imageUrl = "";

                                        if (!TextUtils.isEmpty(tempImageUrl)) {
                                            imageUrl = "https:" + tempImageUrl;
                                        }

                                        serviceAlbumStringList.add(imageUrl);
                                    }
                                } else {
                                    serviceAlbumStringList.add("");
                                }
                            }

                            for (int i = 0; i < serviceAlbumStringList.size(); i++) {
                                String serviceAlbumImageUrl = serviceAlbumStringList.get(i);

                                if (i == 0) {
                                    if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                        ImageViewUtil.setImageView(getParentContext(), firstSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                        firstSkillPhotoIv.setVisibility(View.VISIBLE);
                                    } else {
                                        firstSkillPhotoIv.setVisibility(View.GONE);
                                    }
                                }

                                if (i == 1) {
                                    if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                        ImageViewUtil.setImageView(getParentContext(), secondSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                        secondSkillPhotoIv.setVisibility(View.VISIBLE);
                                    } else {
                                        secondSkillPhotoIv.setVisibility(View.GONE);
                                    }
                                }

                                if (i == 2) {
                                    if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                        ImageViewUtil.setImageView(getParentContext(), thirdSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                        thirdSkillPhotoIv.setVisibility(View.VISIBLE);
                                    } else {
                                        thirdSkillPhotoIv.setVisibility(View.GONE);
                                    }
                                }

                                if (i == 3) {
                                    if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                        ImageViewUtil.setImageView(getParentContext(), fourthSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, false);
                                        fourthSkillPhotoIv.setVisibility(View.VISIBLE);
                                    } else {
                                        fourthSkillPhotoIv.setVisibility(View.GONE);
                                    }
                                }
                            }

                            skillsAlbumLL.setVisibility(View.VISIBLE);
                        } else {
                            skillsAlbumLL.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(voiceUrl)) {
                            voiceUrl = "https:" + voiceUrl;
                            String voiceFilename = voiceUrl.substring(voiceUrl.lastIndexOf("/") + 1);
                            voiceFile = FileUtils.getCacheFile(getParentContext(), AllintaskApplication.getInstance().getVoiceFilePath(), voiceFilename);
                            downloadVoiceFilePath = voiceFile.getPath();

                            if (voiceFile.exists()) {
                                setVoiceDemoLinearLayoutVisible(voiceDuration);
                            } else {
                                startDownloadService(voiceUrl, "语音下载", downloadVoiceFilePath);
                            }

                            voiceDemoLL.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View v) {
                                    final AnimationDrawable animationDrawable = (AnimationDrawable) voiceDemoIv.getBackground();

                                    if (animationDrawable.isRunning()) {
                                        animationDrawable.stop();

                                        MediaRecordPlayerUtils.getInstance().pause();
                                        MediaRecordPlayerUtils.getInstance().release();

                                        Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                                        voiceDemoIv.setBackground(drawable);
                                    } else {
                                        animationDrawable.start();

                                        MediaRecordPlayerUtils.playSound(downloadVoiceFilePath, true);
                                        MediaRecordPlayerUtils.getInstance().set0nMediaRecordPlayerListener(new MediaRecordPlayerUtils.OnMediaRecordPlayerListener() {
                                            @Override
                                            public void setOnCompletionListener() {
                                                animationDrawable.stop();

                                                MediaRecordPlayerUtils.getInstance().release();

                                                Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                                                voiceDemoIv.setBackground(drawable);
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            voiceIntroduceLL.setVisibility(View.GONE);
                        }

                        if (null != categoryPropertyChineseList && categoryPropertyChineseList.size() > 0) {
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 0; i < categoryPropertyChineseList.size(); i++) {
                                RecommendDetailsServiceInformationBean.ServiceDetailsBean.CategoryPropertyChineseBean categoryPropertyChineseBean = categoryPropertyChineseList.get(i);

                                if (null != categoryPropertyChineseBean) {
                                    String categoryProperty = TypeUtils.getString(categoryPropertyChineseBean.categoryProperty, "");
                                    List<String> valuesList = categoryPropertyChineseBean.values;

                                    stringBuilder.append(categoryProperty).append("：");

                                    if (null != valuesList && valuesList.size() > 0) {
                                        for (int j = 0; j < valuesList.size(); j++) {
                                            String value = valuesList.get(j);
                                            stringBuilder.append(value);

                                            if (j != valuesList.size() - 1) {
                                                stringBuilder.append("、");
                                            }
                                        }
                                    }

                                    stringBuilder.append("\n");
                                }
                            }

                            goodAtSkillsTv.setText(stringBuilder);
                            goodAtSkillsLL.setVisibility(View.VISIBLE);
                        } else {
                            goodAtSkillsLL.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(province)) {
                            StringBuilder stringBuilder = new StringBuilder(province);

                            if (!TextUtils.isEmpty(city)) {
                                if (!city.equals(province)) {
                                    stringBuilder.append(city);
                                }
                            }

                            serviceCityTv.setText(stringBuilder);
                            serviceCityLL.setVisibility(View.VISIBLE);
                        } else {
                            serviceCityLL.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(introduce)) {
                            serviceIntroductionTv.setText(introduce);
                            serviceIntroductionLL.setVisibility(View.VISIBLE);
                        } else {
                            serviceIntroductionLL.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(advantage)) {
                            myAdvantageTv.setText(advantage);
                            myAdvantageLL.setVisibility(View.VISIBLE);
                        } else {
                            myAdvantageLL.setVisibility(View.GONE);
                        }

                        if (null != serveWayPriceUnitChineseList && serveWayPriceUnitChineseList.size() > 0) {
                            recommendDetailsServiceWayPriceUnitAdapter.setDateList(serveWayPriceUnitChineseList);

                            serviceWayIdArrayList.clear();
                            servicePriceArrayList.clear();

                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 0; i < serveWayPriceUnitChineseList.size(); i++) {
                                RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean serveWayPriceUnitChineseBean = serveWayPriceUnitChineseList.get(i);

                                if (null != serveWayPriceUnitChineseBean) {
                                    int serveWayId = TypeUtils.getInteger(serveWayPriceUnitChineseBean.serveWayId, -1);
                                    String serveWay = TypeUtils.getString(serveWayPriceUnitChineseBean.serveWay, "");
                                    String price = TypeUtils.getString(serveWayPriceUnitChineseBean.price, "");
                                    String unit = TypeUtils.getString(serveWayPriceUnitChineseBean.unit, "");

                                    serviceWayIdArrayList.add(serveWayId);
                                    servicePriceArrayList.add(price);

                                    stringBuilder.append(serveWay).append("：").append(price);

                                    if (!TextUtils.isEmpty(unit)) {
                                        stringBuilder.append("/").append(unit);
                                    }

                                    if (i != serveWayPriceUnitChineseList.size() - 1) {
                                        stringBuilder.append("\n");
                                    }

                                    if (i == 0) {
                                        serviceWay = serveWay;
                                        servicePrice = price;
                                        serviceUnit = unit;
                                    }
                                }
                            }

                            serviceWayPriceUnit = stringBuilder.toString();
                        }
                    }
                }
            }
        });
    }

    private void startMyPhotoAlbumActivity() {
        Intent intent = new Intent(getParentContext(), MyPhotoAlbumActivity.class);
        intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    private void startPersonalPhotoAlbumActivity(int position) {
        if (null != personalAlbumList && personalAlbumList.size() > 0) {
            RecommendDetailsServiceInformationBean.PersonalAlbumBean personalAlbumBean = personalAlbumList.get(position);

            if (null != personalAlbumBean) {
                int albumId = TypeUtils.getInteger(personalAlbumBean.albumId, -1);

                if (albumId != -1) {
                    Intent personalAlbumDetailsIntent = new Intent(getParentContext(), PhotoAlbumActivity.class);
                    personalAlbumDetailsIntent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    personalAlbumDetailsIntent.putExtra(CommonConstant.EXTRA_ALBUM_ID, albumId);
                    startActivity(personalAlbumDetailsIntent);
                }
            }
        }
    }

    private void startServicePhotoAlbumActivity(int position) {
        if (null != serviceDetailsBean) {
            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean> serveAlbumList = serviceDetailsBean.serveAlbumList;

            if (null != serveAlbumList && serveAlbumList.size() > 0) {
                RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean serveAlbumBean = serveAlbumList.get(position);

                if (null != serveAlbumBean) {
                    int albumId = TypeUtils.getInteger(serveAlbumBean.albumId, -1);

                    if (albumId != -1) {
                        Intent personalAlbumDetailsIntent = new Intent(getParentContext(), PhotoAlbumActivity.class);
                        personalAlbumDetailsIntent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                        personalAlbumDetailsIntent.putExtra(CommonConstant.EXTRA_ALBUM_ID, albumId);
                        startActivity(personalAlbumDetailsIntent);
                    }
                }
            }
        }
    }

    private void startDownloadService(String downloadURL, String title, String desitinationPath) {
        Intent updateIntent = new Intent(getParentContext(), DownloadService.class);
        updateIntent.setAction("com.allintask.lingdao.version.DownloadService");
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TYPE, DownloadService.DOWNLOAD_VOICE);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_PATH, downloadURL);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TITLE, title);
        updateIntent.putExtra(DownloadService.APP_LOCAL_PATH, desitinationPath);
        startService(updateIntent);
    }

    private void setVoiceDemoLinearLayoutVisible(int time) {
        voiceIntroduceLL.setVisibility(View.VISIBLE);

        String timeStr = String.valueOf(time);
        voiceDemoTimeTv.setText(timeStr + "''");
    }

    private void showSharePanel() {
        ShareAction shareAction = new ShareAction(RecommendDetailsActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN).addButton("report", "report", "ic_report", "ic_report").setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (null == share_media) {
                    if (snsPlatform.mKeyword.equals("report")) {
                        if (userId == myUserId) {
                            showToast("不能举报自己");
                        } else {
                            Intent intent = new Intent(getParentContext(), ReportActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                            startActivity(intent);
                        }
                    }
                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                    UMImage umImage = new UMImage(getParentContext(), "https://www.allintask.com/static/mobile/img/activity/local/employNow.png");

                    UMMin umMin = new UMMin("http://sj.qq.com/myapp/detail.htm?apkName=com.allintask.lingdao");
                    umMin.setUserName(CommonConstant.WECHAT_MINI_APPS_USERNAME);
                    umMin.setPath(CommonConstant.WECHAT_MINI_APPS_SERVICE_PATH.replace("{userId}", String.valueOf(userId)).replace("{serveId}", String.valueOf(serviceId)));
                    umMin.setTitle(getString(R.string.wechat_mini_apps_service_description).replace("{name}", name));
                    umMin.setDescription(getString(R.string.splash_text));
                    umMin.setThumb(umImage);
                    new ShareAction(RecommendDetailsActivity.this).withMedia(umMin).setPlatform(share_media).share();
                }
            }
        });

        ShareBoardConfig shareBoardConfig = new ShareBoardConfig();
        shareBoardConfig.setCancelButtonText(getString(R.string.cancel));
        shareBoardConfig.setTitleVisibility(false);
        shareAction.open(shareBoardConfig);
    }

    class RecommendDetailsActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (null != action) {
                    if (action.equals(CommonConstant.ACTION_DOWNLOAD_VOICE_SUCCESS)) {
                        setVoiceDemoLinearLayoutVisible(voiceDuration);
                    }
                }
            }
        }
    }

    @OnClick({R.id.iv_right_first, R.id.iv_right_second, R.id.ll_personal_album, R.id.iv_first_personal_photo, R.id.iv_second_personal_photo, R.id.iv_third_personal_photo, R.id.iv_fourth_personal_photo, R.id.ll_skills_album, R.id.iv_first_skill_photo, R.id.iv_second_skill_photo, R.id.iv_third_skill_photo, R.id.iv_fourth_skill_photo, R.id.ll_see_more_evaluate, R.id.rl_chat, R.id.rl_subscribe, R.id.ll_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right_first:
                if (userId == myUserId) {
                    showToast("不能收藏自己");
                } else {
                    if (!isCollect) {
                        mPresenter.collectUserRequest(userId);

                    } else {
                        mPresenter.cancelCollectUserRequest(userId);
                    }
                }
                break;

            case R.id.iv_right_second:
                showSharePanel();
                break;

            case R.id.ll_personal_album:
                startMyPhotoAlbumActivity();
                break;

            case R.id.iv_first_personal_photo:
                startPersonalPhotoAlbumActivity(0);
                break;

            case R.id.iv_second_personal_photo:
                startPersonalPhotoAlbumActivity(1);
                break;

            case R.id.iv_third_personal_photo:
                startPersonalPhotoAlbumActivity(2);
                break;

            case R.id.iv_fourth_personal_photo:
                startPersonalPhotoAlbumActivity(3);
                break;

            case R.id.ll_skills_album:
                startMyPhotoAlbumActivity();
                break;

            case R.id.iv_first_skill_photo:
                startServicePhotoAlbumActivity(0);
                break;

            case R.id.iv_second_skill_photo:
                startServicePhotoAlbumActivity(1);
                break;

            case R.id.iv_third_skill_photo:
                startServicePhotoAlbumActivity(2);
                break;

            case R.id.iv_fourth_skill_photo:
                startServicePhotoAlbumActivity(3);
                break;

            case R.id.ll_see_more_evaluate:
                Intent seeMoreEvaluateIntent = new Intent(getParentContext(), AllEvaluateActivity.class);
                seeMoreEvaluateIntent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                startActivity(seeMoreEvaluateIntent);
                break;

            case R.id.rl_chat:
                if (userId == myUserId) {
                    showToast("不能与自己聊天");
                } else {
                    Intent chatIntent = new Intent(getParentContext(), ChatActivity.class);
                    chatIntent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(userId));
                    startActivity(chatIntent);
                }
                break;

            case R.id.rl_subscribe:
                if (userId == myUserId) {
                    showToast("不能预约自己");
                } else {
                    if (userId != -1 && serviceId != -1 && categoryId != -1) {
                        Intent subscribeIntent = new Intent(getParentContext(), PublishDemandActivity.class);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SEND_USER_ID, userId);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_CATEGORY_ID, categoryId);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL, userHeadPortraitUrl);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_NAME, name);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_GENDER, gender);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_AGE, age);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SERVICE_CATEGORY, categoryName);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_DISTANCE, distance);
                        subscribeIntent.putIntegerArrayListExtra(CommonConstant.EXTRA_SERVICE_WAY_ID_ARRAY_LIST, serviceWayIdArrayList);
                        subscribeIntent.putStringArrayListExtra(CommonConstant.EXTRA_SERVICE_PRICE_ARRAY_LIST, servicePriceArrayList);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SERVICE_WAY_PRICE_UNIT, serviceWayPriceUnit);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SERVICE_WAY, serviceWay);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SERVICE_PRICE, servicePrice);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_SERVICE_UNIT, serviceUnit);
                        subscribeIntent.putExtra(CommonConstant.EXTRA_ADVANTAGE, advantage);
                        startActivity(subscribeIntent);
                    }
                }
                break;

            case R.id.ll_like:
                likeLL.setOnClickListener(null);

                if (!isLike) {
                    mPresenter.likeUserRequest(userId);
                } else {
                    mPresenter.cancelLikeUserRequest(userId);
                }
                break;
        }
    }

    @Override
    public void onShowPersonalInformation(String loginTimeTip, String avatarUrl, int officialRecommendStatus, String officialRecommendIconUrl, String realName, int gender, Date birthday, Date startWorkAt, String location, int distance, boolean isLike, boolean isStoreUp, int likeCount, RecommendDetailsUserInformationBean.ViewRecordVo viewRecordVo, List<RecommendDetailsUserInformationBean.EducationalExperienceBean> educationalExperienceList, List<RecommendDetailsUserInformationBean.WorkExperienceBean> workExperienceList, List<RecommendDetailsUserInformationBean.HonorAndQualificationBean> honorAndQualificationList) {
        userHeadPortraitUrl = avatarUrl;
        name = realName;
        this.gender = gender;
        isCollect = isStoreUp;

        if (!isStoreUp) {
            rightFirstIv.setImageResource(R.mipmap.ic_not_collect);
        } else {
            rightFirstIv.setImageResource(R.mipmap.ic_collected);
        }

        rightFirstIv.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(loginTimeTip)) {
            timeTv.setText(loginTimeTip);
            timeTv.setVisibility(View.VISIBLE);
        } else {
            timeTv.setVisibility(View.GONE);
        }

        ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, avatarUrl, R.mipmap.ic_default_avatar, true);

        if (!TextUtils.isEmpty(officialRecommendIconUrl)) {
            specialistRecommendIv.setVisibility(View.VISIBLE);
            officialRecommendIconUrl = "https:" + officialRecommendIconUrl;
            ImageViewUtil.setImageView(getParentContext(), specialistRecommendIv, officialRecommendIconUrl, R.mipmap.ic_specialist_recommend, true);
        } else {
            specialistRecommendIv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(realName)) {
            titleTv.setText(realName);
            titleTv.setVisibility(View.VISIBLE);

            nameTv.setText(realName);
            nameTv.setVisibility(View.VISIBLE);
        } else {
            nameTv.setVisibility(View.GONE);
        }

        if (gender == CommonConstant.MALE) {
            genderIv.setImageResource(R.mipmap.ic_male);
            genderIv.setVisibility(View.VISIBLE);
        } else if (gender == CommonConstant.FEMALE) {
            genderIv.setImageResource(R.mipmap.ic_female);
            genderIv.setVisibility(View.VISIBLE);
        } else {
            genderIv.setVisibility(View.GONE);
        }

//        if (null != birthday) {
//            int age = AgeUtils.getAge(birthday);
//            this.age = String.valueOf(age) + "岁";
//            ageTv.setText(this.age);
//            ageTv.setVisibility(View.VISIBLE);
//        } else {
//            ageTv.setVisibility(View.GONE);
//        }

        if (null != startWorkAt) {
            int workExperienceYear = AgeUtils.getAge(startWorkAt);

            StringBuilder stringBuilder = new StringBuilder(String.valueOf(workExperienceYear)).append("年经验");
            workExperienceYearTv.setText(stringBuilder);
            workExperienceYearTv.setVisibility(View.VISIBLE);
        } else {
            workExperienceYearTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(location)) {
            locationTv.setText(location);
            locationTv.setVisibility(View.VISIBLE);
        } else {
            locationTv.setVisibility(View.GONE);
        }

        String tempDistance = String.valueOf(distance);
        int distanceLength = tempDistance.length();

        if (distanceLength >= 4) {
            double distanceDouble = distance / 1000D;
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            this.distance = decimalFormat.format(distanceDouble) + "km";
        } else {
            this.distance = String.valueOf(distance) + "m";
        }

        distanceTv.setText(this.distance);

        if (null != viewRecordVo) {
            List<String> seeingImageUrlList = new ArrayList<>();
            List<String> viewUserAvatarUrlsList = viewRecordVo.viewUserAvatarUrls;
            int numberOfPeopleSeeing = viewRecordVo.count;

            if (null != viewUserAvatarUrlsList && viewUserAvatarUrlsList.size() > 0) {
                for (int i = 0; i < 6; i++) {
                    if (i < viewUserAvatarUrlsList.size()) {
                        String tempSeeingImageUrl = viewUserAvatarUrlsList.get(i);
                        String seeingImageUrl = "";

                        if (!TextUtils.isEmpty(tempSeeingImageUrl)) {
                            seeingImageUrl = "https:" + tempSeeingImageUrl;
                        }

                        seeingImageUrlList.add(seeingImageUrl);
                    } else {
                        seeingImageUrlList.add("");
                    }
                }

                for (int i = 0; i < seeingImageUrlList.size(); i++) {
                    String seeingImageUrl = seeingImageUrlList.get(i);

                    if (i == 0) {
                        if (!TextUtils.isEmpty(seeingImageUrl)) {
                            ImageViewUtil.setImageView(getParentContext(), firstSeeingCIV, seeingImageUrl, R.mipmap.ic_default_avatar, true);
                            firstSeeingCIV.setVisibility(View.VISIBLE);
                        } else {
                            firstSeeingCIV.setVisibility(View.GONE);
                        }
                    }

                    if (i == 1) {
                        if (!TextUtils.isEmpty(seeingImageUrl)) {
                            ImageViewUtil.setImageView(getParentContext(), secondSeeingCIV, seeingImageUrl, R.mipmap.ic_default_avatar, true);
                            secondSeeingCIV.setVisibility(View.VISIBLE);
                        } else {
                            secondSeeingCIV.setVisibility(View.GONE);
                        }
                    }

                    if (i == 2) {
                        if (!TextUtils.isEmpty(seeingImageUrl)) {
                            ImageViewUtil.setImageView(getParentContext(), thirdSeeingCIV, seeingImageUrl, R.mipmap.ic_default_avatar, true);
                            thirdSeeingCIV.setVisibility(View.VISIBLE);
                        } else {
                            thirdSeeingCIV.setVisibility(View.GONE);
                        }
                    }

                    if (i == 3) {
                        if (!TextUtils.isEmpty(seeingImageUrl)) {
                            ImageViewUtil.setImageView(getParentContext(), fourthSeeingCIV, seeingImageUrl, R.mipmap.ic_default_avatar, true);
                            fourthSeeingCIV.setVisibility(View.VISIBLE);
                        } else {
                            fourthSeeingCIV.setVisibility(View.GONE);
                        }
                    }

                    if (i == 4) {
                        if (!TextUtils.isEmpty(seeingImageUrl)) {
                            ImageViewUtil.setImageView(getParentContext(), fifthSeeingCIV, seeingImageUrl, R.mipmap.ic_default_avatar, true);
                            fifthSeeingCIV.setVisibility(View.VISIBLE);
                        } else {
                            fifthSeeingCIV.setVisibility(View.GONE);
                        }
                    }

                    if (i == 5) {
                        if (!TextUtils.isEmpty(seeingImageUrl)) {
                            ImageViewUtil.setImageView(getParentContext(), sixthSeeingCIV, seeingImageUrl, R.mipmap.ic_default_avatar, true);
                            sixthSeeingCIV.setVisibility(View.VISIBLE);
                        } else {
                            sixthSeeingCIV.setVisibility(View.GONE);
                        }
                    }
                }
            }

            StringBuilder numberOfPeopleSeeingStringBuilder = new StringBuilder(String.valueOf(numberOfPeopleSeeing)).append("人");
            numberOfPeopleSeeingTv.setText(numberOfPeopleSeeingStringBuilder);
            seeingLL.setVisibility(View.VISIBLE);
        } else {
            seeingLL.setVisibility(View.GONE);
        }

        if (null != educationalExperienceList && educationalExperienceList.size() > 0) {
            recommendDetailsEducationalExperienceAdapter.setDateList(educationalExperienceList);
            educationalExperienceLL.setVisibility(View.VISIBLE);
        } else {
            educationalExperienceLL.setVisibility(View.GONE);
        }

        if (null != workExperienceList && workExperienceList.size() > 0) {
            recommendDetailsWorkExperienceAdapter.setDateList(workExperienceList);
            workExperienceLL.setVisibility(View.VISIBLE);
        } else {
            workExperienceLL.setVisibility(View.GONE);
        }

        if (null != honorAndQualificationList && honorAndQualificationList.size() > 0) {
            recommendDetailsHonorAndQualificationAdapter.setDateList(honorAndQualificationList);
            honorAndQualificationLL.setVisibility(View.VISIBLE);
        } else {
            honorAndQualificationLL.setVisibility(View.GONE);
        }

        this.isLike = isLike;

        if (!isLike) {
            likeLL.setBackgroundResource(R.drawable.shape_recommend_details_button_white_background);
            likeIv.setImageResource(R.mipmap.ic_not_like);
            likeTv.setTextColor(getResources().getColor(R.color.theme_orange));
        } else {
            likeLL.setBackgroundResource(R.drawable.shape_recommend_details_button_orange_background);
            likeIv.setImageResource(R.mipmap.ic_like);
            likeTv.setTextColor(getResources().getColor(R.color.white));
        }

        likeAmount = likeCount;

        if (likeCount < 1000) {
            likeTv.setText(String.valueOf(likeCount));
        } else {
            likeTv.setText("+999");
        }

        bottomLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowServiceInformation(List<RecommendDetailsServiceInformationBean.PersonalAlbumBean> personalAlbumList, List<RecommendDetailsServiceInformationBean.ServiceDetailsBean> serviceDetailsList) {
        if (null != personalAlbumList && personalAlbumList.size() > 0) {
            if (null != this.personalAlbumList && this.personalAlbumList.size() > 0) {
                this.personalAlbumList.clear();
            }

            this.personalAlbumList = personalAlbumList;

            List<String> personalAlbumStringList = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                if (i < personalAlbumList.size()) {
                    RecommendDetailsServiceInformationBean.PersonalAlbumBean personalAlbumBean = personalAlbumList.get(i);

                    if (null != personalAlbumBean) {
                        String tempImageUrl = TypeUtils.getString(personalAlbumBean.imageUrl, "");

                        String imageUrl = "";

                        if (!TextUtils.isEmpty(tempImageUrl)) {
                            imageUrl = "https:" + tempImageUrl;
                        }

                        personalAlbumStringList.add(imageUrl);
                    }
                } else {
                    personalAlbumStringList.add("");
                }
            }

            for (int i = 0; i < personalAlbumStringList.size(); i++) {
                String imageUrl = personalAlbumStringList.get(i);

                if (i == 0) {
                    if (!TextUtils.isEmpty(imageUrl)) {
                        ImageViewUtil.setImageView(getParentContext(), firstPersonalPhotoIv, imageUrl, R.mipmap.ic_default, true);
                        firstPersonalPhotoIv.setVisibility(View.VISIBLE);
                    } else {
                        firstPersonalPhotoIv.setVisibility(View.GONE);
                    }
                }

                if (i == 1) {
                    if (!TextUtils.isEmpty(imageUrl)) {
                        ImageViewUtil.setImageView(getParentContext(), secondPersonalPhotoIv, imageUrl, R.mipmap.ic_default, true);
                        secondPersonalPhotoIv.setVisibility(View.VISIBLE);
                    } else {
                        secondPersonalPhotoIv.setVisibility(View.GONE);
                    }
                }

                if (i == 2) {
                    if (!TextUtils.isEmpty(imageUrl)) {
                        ImageViewUtil.setImageView(getParentContext(), thirdPersonalPhotoIv, imageUrl, R.mipmap.ic_default, true);
                        thirdPersonalPhotoIv.setVisibility(View.VISIBLE);
                    } else {
                        thirdPersonalPhotoIv.setVisibility(View.GONE);
                    }
                }

                if (i == 3) {
                    if (!TextUtils.isEmpty(imageUrl)) {
                        ImageViewUtil.setImageView(getParentContext(), fourthPersonalPhotoIv, imageUrl, R.mipmap.ic_default, true);
                        fourthPersonalPhotoIv.setVisibility(View.VISIBLE);
                    } else {
                        fourthPersonalPhotoIv.setVisibility(View.GONE);
                    }
                }
            }

            personalAlbumLL.setVisibility(View.VISIBLE);
        } else {
            personalAlbumLL.setVisibility(View.GONE);
        }

        if (null != serviceDetailsList && serviceDetailsList.size() > 0) {
            if (null != this.serviceDetailsList && this.serviceDetailsList.size() > 0) {
                this.serviceDetailsList.clear();
            }

            this.serviceDetailsList = serviceDetailsList;

            if (null != myServiceList && myServiceList.size() > 0) {
                myServiceList.clear();
            }

            for (int i = 0; i < serviceDetailsList.size(); i++) {
                RecommendDetailsServiceInformationBean.ServiceDetailsBean serviceDetailsBean = serviceDetailsList.get(i);

                if (null != serviceDetailsBean) {
                    int serviceId = TypeUtils.getInteger(serviceDetailsBean.serveId, -1);
                    String categoryIconUrl = TypeUtils.getString(serviceDetailsBean.categoryIconUrl, "");
                    String categorySelectIconUrl = TypeUtils.getString(serviceDetailsBean.categorySelectIconUrl, "");
                    String categoryName = TypeUtils.getString(serviceDetailsBean.categoryName, "");

                    RecommendDetailsTopBean recommendDetailsTopBean = new RecommendDetailsTopBean();
                    recommendDetailsTopBean.unSelectedImageUrl = categoryIconUrl;
                    recommendDetailsTopBean.selectedImageUrl = categorySelectIconUrl;
                    recommendDetailsTopBean.name = categoryName;

                    if (this.serviceId == -1) {
                        if (i == 0) {
                            recommendDetailsTopBean.isSelected = true;
                        } else {
                            recommendDetailsTopBean.isSelected = false;
                        }

                        if (i == 0) {
                            myServiceRV.scrollToPosition(i);

                            this.serviceDetailsBean = serviceDetailsBean;
                            this.serviceId = serviceId;
                            this.categoryName = categoryName;

                            recommendDetailsTopBean.isSelected = true;

                            categoryId = TypeUtils.getInteger(serviceDetailsBean.categoryId, -1);
                            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean> serveAlbumList = serviceDetailsBean.serveAlbumList;
                            String voiceUrl = TypeUtils.getString(serviceDetailsBean.voiceUrl, "");
                            voiceDuration = TypeUtils.getInteger(serviceDetailsBean.voiceDuration, -1);
                            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.CategoryPropertyChineseBean> categoryPropertyChineseList = serviceDetailsBean.categoryPropertyChineseList;
                            String province = TypeUtils.getString(serviceDetailsBean.province, "");
                            String city = TypeUtils.getString(serviceDetailsBean.city, "");
                            String introduce = TypeUtils.getString(serviceDetailsBean.introduce, "");
                            advantage = TypeUtils.getString(serviceDetailsBean.advantage, "");
                            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean> serveWayPriceUnitChineseList = serviceDetailsBean.serveWayPriceUnitChineseList;

                            if (null != serveAlbumList && serveAlbumList.size() > 0) {
                                List<String> serviceAlbumStringList = new ArrayList<>();

                                for (int j = 0; j < 6; j++) {
                                    if (j < serveAlbumList.size()) {
                                        RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean serveAlbumBean = serveAlbumList.get(j);

                                        if (null != serveAlbumBean) {
                                            String tempImageUrl = TypeUtils.getString(serveAlbumBean.imageUrl, "");

                                            String imageUrl = "";

                                            if (!TextUtils.isEmpty(tempImageUrl)) {
                                                imageUrl = "https:" + tempImageUrl;
                                            }

                                            serviceAlbumStringList.add(imageUrl);
                                        }
                                    } else {
                                        serviceAlbumStringList.add("");
                                    }
                                }

                                for (int j = 0; j < serviceAlbumStringList.size(); j++) {
                                    String serviceAlbumImageUrl = serviceAlbumStringList.get(j);

                                    if (j == 0) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), firstSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                            firstSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            firstSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }

                                    if (j == 1) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), secondSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                            secondSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            secondSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }

                                    if (j == 2) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), thirdSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                            thirdSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            thirdSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }

                                    if (j == 3) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), fourthSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, false);
                                            fourthSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            fourthSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                skillsAlbumLL.setVisibility(View.VISIBLE);
                            } else {
                                skillsAlbumLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(voiceUrl)) {
                                voiceUrl = "https:" + voiceUrl;
                                String voiceFilename = voiceUrl.substring(voiceUrl.lastIndexOf("/") + 1);
                                voiceFile = FileUtils.getCacheFile(getParentContext(), AllintaskApplication.getInstance().getVoiceFilePath(), voiceFilename);
                                downloadVoiceFilePath = voiceFile.getPath();

                                if (voiceFile.exists()) {
                                    setVoiceDemoLinearLayoutVisible(voiceDuration);
                                } else {
                                    startDownloadService(voiceUrl, "语音下载", downloadVoiceFilePath);
                                }

                                voiceDemoLL.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onClick(View v) {
                                        final AnimationDrawable animationDrawable = (AnimationDrawable) voiceDemoIv.getBackground();

                                        if (animationDrawable.isRunning()) {
                                            animationDrawable.stop();

                                            MediaRecordPlayerUtils.getInstance().pause();
                                            MediaRecordPlayerUtils.getInstance().release();

                                            Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                                            voiceDemoIv.setBackground(drawable);
                                        } else {
                                            animationDrawable.start();

                                            MediaRecordPlayerUtils.playSound(downloadVoiceFilePath, true);
                                            MediaRecordPlayerUtils.getInstance().set0nMediaRecordPlayerListener(new MediaRecordPlayerUtils.OnMediaRecordPlayerListener() {
                                                @Override
                                                public void setOnCompletionListener() {
                                                    animationDrawable.stop();

                                                    MediaRecordPlayerUtils.getInstance().release();

                                                    Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                                                    voiceDemoIv.setBackground(drawable);
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                voiceIntroduceLL.setVisibility(View.GONE);
                            }

                            if (null != categoryPropertyChineseList && categoryPropertyChineseList.size() > 0) {
                                StringBuilder stringBuilder = new StringBuilder();

                                for (int j = 0; j < categoryPropertyChineseList.size(); j++) {
                                    RecommendDetailsServiceInformationBean.ServiceDetailsBean.CategoryPropertyChineseBean categoryPropertyChineseBean = categoryPropertyChineseList.get(j);

                                    if (null != categoryPropertyChineseBean) {
                                        String categoryProperty = TypeUtils.getString(categoryPropertyChineseBean.categoryProperty, "");
                                        List<String> valuesList = categoryPropertyChineseBean.values;

                                        stringBuilder.append(categoryProperty).append("：");

                                        if (null != valuesList && valuesList.size() > 0) {
                                            for (int k = 0; k < valuesList.size(); k++) {
                                                String value = valuesList.get(k);
                                                stringBuilder.append(value);

                                                if (k != valuesList.size() - 1) {
                                                    stringBuilder.append("、");
                                                }
                                            }
                                        }

                                        stringBuilder.append("\n");
                                    }
                                }

                                goodAtSkillsTv.setText(stringBuilder);
                                goodAtSkillsLL.setVisibility(View.VISIBLE);
                            } else {
                                goodAtSkillsLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(province)) {
                                StringBuilder stringBuilder = new StringBuilder(province);

                                if (!TextUtils.isEmpty(city)) {
                                    if (!city.equals(province)) {
                                        stringBuilder.append(city);
                                    }
                                }

                                serviceCityTv.setText(stringBuilder);
                                serviceCityLL.setVisibility(View.VISIBLE);
                            } else {
                                serviceCityLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(introduce)) {
                                serviceIntroductionTv.setText(introduce);
                                serviceIntroductionLL.setVisibility(View.VISIBLE);
                            } else {
                                serviceIntroductionLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(advantage)) {
                                myAdvantageTv.setText(advantage);
                                myAdvantageLL.setVisibility(View.VISIBLE);
                            } else {
                                myAdvantageLL.setVisibility(View.GONE);
                            }

                            if (null != serveWayPriceUnitChineseList && serveWayPriceUnitChineseList.size() > 0) {
                                recommendDetailsServiceWayPriceUnitAdapter.setDateList(serveWayPriceUnitChineseList);

                                serviceWayIdArrayList.clear();
                                servicePriceArrayList.clear();

                                StringBuilder stringBuilder = new StringBuilder();

                                for (int j = 0; j < serveWayPriceUnitChineseList.size(); j++) {
                                    RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean serveWayPriceUnitChineseBean = serveWayPriceUnitChineseList.get(j);

                                    if (null != serveWayPriceUnitChineseBean) {
                                        int serveWayId = TypeUtils.getInteger(serveWayPriceUnitChineseBean.serveWayId, -1);
                                        String serveWay = TypeUtils.getString(serveWayPriceUnitChineseBean.serveWay, "");
                                        String price = TypeUtils.getString(serveWayPriceUnitChineseBean.price, "");
                                        String unit = TypeUtils.getString(serveWayPriceUnitChineseBean.unit, "");

                                        serviceWayIdArrayList.add(serveWayId);
                                        servicePriceArrayList.add(price);

                                        stringBuilder.append(serveWay).append("：").append(price);

                                        if (!TextUtils.isEmpty(unit)) {
                                            stringBuilder.append("/").append(unit);
                                        }

                                        if (j != serveWayPriceUnitChineseList.size() - 1) {
                                            stringBuilder.append("\n");
                                        }

                                        if (j == 0) {
                                            serviceWay = serveWay;
                                            servicePrice = price;
                                            serviceUnit = unit;
                                        }
                                    }
                                }

                                serviceWayPriceUnit = stringBuilder.toString();
                            }
                        } else {
                            recommendDetailsTopBean.isSelected = false;
                        }
                    } else {
                        if (serviceId == this.serviceId) {
                            myServiceRV.scrollToPosition(i);

                            this.serviceDetailsBean = serviceDetailsBean;
                            this.serviceId = serviceId;
                            this.categoryName = categoryName;

                            recommendDetailsTopBean.isSelected = true;

                            categoryId = TypeUtils.getInteger(serviceDetailsBean.categoryId, -1);
                            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean> serveAlbumList = serviceDetailsBean.serveAlbumList;
                            String voiceUrl = TypeUtils.getString(serviceDetailsBean.voiceUrl, "");
                            voiceDuration = TypeUtils.getInteger(serviceDetailsBean.voiceDuration, -1);
                            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.CategoryPropertyChineseBean> categoryPropertyChineseList = serviceDetailsBean.categoryPropertyChineseList;
                            String province = TypeUtils.getString(serviceDetailsBean.province, "");
                            String city = TypeUtils.getString(serviceDetailsBean.city, "");
                            String introduce = TypeUtils.getString(serviceDetailsBean.introduce, "");
                            advantage = TypeUtils.getString(serviceDetailsBean.advantage, "");
                            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean> serveWayPriceUnitChineseList = serviceDetailsBean.serveWayPriceUnitChineseList;

                            if (null != serveAlbumList && serveAlbumList.size() > 0) {
                                List<String> serviceAlbumStringList = new ArrayList<>();

                                for (int j = 0; j < 6; j++) {
                                    if (j < serveAlbumList.size()) {
                                        RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeAlbumBean serveAlbumBean = serveAlbumList.get(j);

                                        if (null != serveAlbumBean) {
                                            String tempImageUrl = TypeUtils.getString(serveAlbumBean.imageUrl, "");

                                            String imageUrl = "";

                                            if (!TextUtils.isEmpty(tempImageUrl)) {
                                                imageUrl = "https:" + tempImageUrl;
                                            }

                                            serviceAlbumStringList.add(imageUrl);
                                        }
                                    } else {
                                        serviceAlbumStringList.add("");
                                    }
                                }

                                for (int j = 0; j < serviceAlbumStringList.size(); j++) {
                                    String serviceAlbumImageUrl = serviceAlbumStringList.get(j);

                                    if (j == 0) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), firstSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                            firstSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            firstSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }

                                    if (j == 1) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), secondSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                            secondSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            secondSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }

                                    if (j == 2) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), thirdSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, true);
                                            thirdSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            thirdSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }

                                    if (j == 3) {
                                        if (!TextUtils.isEmpty(serviceAlbumImageUrl)) {
                                            ImageViewUtil.setImageView(getParentContext(), fourthSkillPhotoIv, serviceAlbumImageUrl, R.mipmap.ic_default, false);
                                            fourthSkillPhotoIv.setVisibility(View.VISIBLE);
                                        } else {
                                            fourthSkillPhotoIv.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                skillsAlbumLL.setVisibility(View.VISIBLE);
                            } else {
                                skillsAlbumLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(voiceUrl)) {
                                voiceUrl = "https:" + voiceUrl;
                                String voiceFilename = voiceUrl.substring(voiceUrl.lastIndexOf("/") + 1);
                                voiceFile = FileUtils.getCacheFile(getParentContext(), AllintaskApplication.getInstance().getVoiceFilePath(), voiceFilename);
                                downloadVoiceFilePath = voiceFile.getPath();

                                if (voiceFile.exists()) {
                                    setVoiceDemoLinearLayoutVisible(voiceDuration);
                                } else {
                                    startDownloadService(voiceUrl, "语音下载", downloadVoiceFilePath);
                                }

                                voiceDemoLL.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onClick(View v) {
                                        final AnimationDrawable animationDrawable = (AnimationDrawable) voiceDemoIv.getBackground();

                                        if (animationDrawable.isRunning()) {
                                            animationDrawable.stop();

                                            MediaRecordPlayerUtils.getInstance().pause();
                                            MediaRecordPlayerUtils.getInstance().release();

                                            Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                                            voiceDemoIv.setBackground(drawable);
                                        } else {
                                            animationDrawable.start();

                                            MediaRecordPlayerUtils.playSound(downloadVoiceFilePath, true);
                                            MediaRecordPlayerUtils.getInstance().set0nMediaRecordPlayerListener(new MediaRecordPlayerUtils.OnMediaRecordPlayerListener() {
                                                @Override
                                                public void setOnCompletionListener() {
                                                    animationDrawable.stop();

                                                    MediaRecordPlayerUtils.getInstance().release();

                                                    Drawable drawable = getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                                                    voiceDemoIv.setBackground(drawable);
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                voiceIntroduceLL.setVisibility(View.GONE);
                            }

                            if (null != categoryPropertyChineseList && categoryPropertyChineseList.size() > 0) {
                                StringBuilder stringBuilder = new StringBuilder();

                                for (int j = 0; j < categoryPropertyChineseList.size(); j++) {
                                    RecommendDetailsServiceInformationBean.ServiceDetailsBean.CategoryPropertyChineseBean categoryPropertyChineseBean = categoryPropertyChineseList.get(j);

                                    if (null != categoryPropertyChineseBean) {
                                        String categoryProperty = TypeUtils.getString(categoryPropertyChineseBean.categoryProperty, "");
                                        List<String> valuesList = categoryPropertyChineseBean.values;

                                        stringBuilder.append(categoryProperty).append("：");

                                        if (null != valuesList && valuesList.size() > 0) {
                                            for (int k = 0; k < valuesList.size(); k++) {
                                                String value = valuesList.get(k);
                                                stringBuilder.append(value);

                                                if (k != valuesList.size() - 1) {
                                                    stringBuilder.append("、");
                                                }
                                            }
                                        }

                                        stringBuilder.append("\n");
                                    }
                                }

                                goodAtSkillsTv.setText(stringBuilder);
                                goodAtSkillsLL.setVisibility(View.VISIBLE);
                            } else {
                                goodAtSkillsLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(province)) {
                                StringBuilder stringBuilder = new StringBuilder(province);

                                if (!TextUtils.isEmpty(city)) {
                                    if (!city.equals(province)) {
                                        stringBuilder.append(city);
                                    }
                                }

                                serviceCityTv.setText(stringBuilder);
                                serviceCityLL.setVisibility(View.VISIBLE);
                            } else {
                                serviceCityLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(introduce)) {
                                serviceIntroductionTv.setText(introduce);
                                serviceIntroductionLL.setVisibility(View.VISIBLE);
                            } else {
                                serviceIntroductionLL.setVisibility(View.GONE);
                            }

                            if (!TextUtils.isEmpty(advantage)) {
                                myAdvantageTv.setText(advantage);
                                myAdvantageLL.setVisibility(View.VISIBLE);
                            } else {
                                myAdvantageLL.setVisibility(View.GONE);
                            }

                            if (null != serveWayPriceUnitChineseList && serveWayPriceUnitChineseList.size() > 0) {
                                recommendDetailsServiceWayPriceUnitAdapter.setDateList(serveWayPriceUnitChineseList);

                                serviceWayIdArrayList.clear();
                                servicePriceArrayList.clear();

                                StringBuilder stringBuilder = new StringBuilder();

                                for (int j = 0; j < serveWayPriceUnitChineseList.size(); j++) {
                                    RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean serveWayPriceUnitChineseBean = serveWayPriceUnitChineseList.get(j);

                                    if (null != serveWayPriceUnitChineseBean) {
                                        int serveWayId = TypeUtils.getInteger(serveWayPriceUnitChineseBean.serveWayId, -1);
                                        String serveWay = TypeUtils.getString(serveWayPriceUnitChineseBean.serveWay, "");
                                        String price = TypeUtils.getString(serveWayPriceUnitChineseBean.price, "");
                                        String unit = TypeUtils.getString(serveWayPriceUnitChineseBean.unit, "");

                                        serviceWayIdArrayList.add(serveWayId);
                                        servicePriceArrayList.add(price);

                                        stringBuilder.append(serveWay).append("：").append(price);

                                        if (!TextUtils.isEmpty(unit)) {
                                            stringBuilder.append("/").append(unit);
                                        }

                                        if (j != serveWayPriceUnitChineseList.size() - 1) {
                                            stringBuilder.append("\n");
                                        }

                                        if (j == 0) {
                                            serviceWay = serveWay;
                                            servicePrice = price;
                                            serviceUnit = unit;
                                        }
                                    }
                                }

                                serviceWayPriceUnit = stringBuilder.toString();
                            }
                        } else {
                            recommendDetailsTopBean.isSelected = false;
                        }
                    }

                    myServiceList.add(recommendDetailsTopBean);
                }
            }

            recommendDetailsTopAdapter.setDateList(myServiceList);
            myServiceLL.setVisibility(View.VISIBLE);
        } else {
            myServiceLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowEvaluateList(int total, List<EvaluationListBean.EvaluationBean> evaluationList) {
        if (null != evaluationList && evaluationList.size() > 0) {
            evaluateAdapter.setDateList(evaluationList);
            evaluateAmountTv.setText(String.valueOf(total));
            evaluateLL.setVisibility(View.VISIBLE);
        } else {
            evaluateLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLikeUserSuccess() {
        isLike = true;
        likeAmount++;

        if (likeAmount < 1000) {
            likeTv.setText(String.valueOf(likeAmount));
        } else {
            likeTv.setText("+999");
        }

        likeLL.setBackgroundResource(R.drawable.shape_recommend_details_button_orange_background);
        likeIv.setImageResource(R.mipmap.ic_like);
        likeTv.setTextColor(getResources().getColor(R.color.white));

        likeLL.setOnClickListener(this);
    }

    @Override
    public void onLikeUserFail() {
        showToast("点赞失败");
        likeLL.setOnClickListener(this);
    }

    @Override
    public void onCancelLikeUserSuccess() {
        isLike = false;
        likeAmount--;

        if (likeAmount < 1000) {
            likeTv.setText(String.valueOf(likeAmount));
        } else {
            likeTv.setText("+999");
        }

        likeLL.setBackgroundResource(R.drawable.shape_recommend_details_button_white_background);
        likeIv.setImageResource(R.mipmap.ic_not_like);
        likeTv.setTextColor(getResources().getColor(R.color.theme_orange));

        likeLL.setOnClickListener(this);
    }

    @Override
    public void onCancelLikeUserFail() {
        showToast("取消点赞失败");
        likeLL.setOnClickListener(this);
    }

    @Override
    public void onCollectUserSuccess() {
        isCollect = true;
        rightFirstIv.setImageResource(R.mipmap.ic_collected);
    }

    @Override
    public void onCancelCollectUserSuccess() {
        isCollect = false;
        rightFirstIv.setImageResource(R.mipmap.ic_not_collect);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onDestroy() {
        if (null != recommendDetailsActivityReceiver) {
            unregisterReceiver(recommendDetailsActivityReceiver);
        }

        MediaRecordPlayerUtils.getInstance().reset();
        MediaRecordPlayerUtils.getInstance().release();

        super.onDestroy();
    }

}
