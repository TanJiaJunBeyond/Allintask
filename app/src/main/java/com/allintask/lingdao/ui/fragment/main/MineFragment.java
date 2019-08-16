package com.allintask.lingdao.ui.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.presenter.main.MinePresenter;
import com.allintask.lingdao.ui.activity.SimpleWebViewActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.user.AuthenticationCenterActivity;
import com.allintask.lingdao.ui.activity.user.FingerprintVerifyActivity;
import com.allintask.lingdao.ui.activity.user.GesturePasswordActivity;
import com.allintask.lingdao.ui.activity.user.MyAccountActivity;
import com.allintask.lingdao.ui.activity.user.MyCollectionActivity;
import com.allintask.lingdao.ui.activity.user.MyPhotoAlbumActivity;
import com.allintask.lingdao.ui.activity.user.PersonalInformationActivity;
import com.allintask.lingdao.ui.activity.user.SettingActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.utils.SystemBarTintManager;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IMineView;
import com.allintask.lingdao.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public class MineFragment extends BaseFragment<IMineView, MinePresenter> implements SwipeRefreshLayout.OnRefreshListener, IMineView {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_setting)
    ImageView settingIv;
    @BindView(R.id.rl_personal_information)
    RelativeLayout personalInformationRL;
    @BindView(R.id.civ_head_portrait)
    CircleImageView headPortraitCIV;
    @BindView(R.id.tv_name)
    TextView nameTv;
    @BindView(R.id.iv_gender)
    ImageView genderIv;
    @BindView(R.id.tv_age)
    TextView ageTv;
    @BindView(R.id.tv_percent_of_perfect)
    TextView percentOfPerfectTv;
    @BindView(R.id.tv_work_experience_year)
    TextView workExperienceYearTv;
    @BindView(R.id.rl_my_account)
    RelativeLayout myAccountRL;
    @BindView(R.id.rl_my_photo_album)
    RelativeLayout myPhotoAlbumRL;
    @BindView(R.id.rl_my_collection)
    RelativeLayout myCollectionRL;
    @BindView(R.id.tv_my_collection_amount)
    TextView myCollectionAmountTv;
    @BindView(R.id.rl_authentication_center)
    RelativeLayout authenticationCenterRL;
    @BindView(R.id.tv_unauthorized)
    TextView unauthorizedTv;
    @BindView(R.id.rl_about_us)
    RelativeLayout aboutUsRL;

    private SystemBarTintManager systemBarTintManager;
    private boolean isZmrzAuthSuccess;
    private boolean isExistGesturePwd;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected MinePresenter CreatePresenter() {
        return new MinePresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Window window = ((MainActivity) getParentContext()).getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.select_skills_top_background_start_color));

            ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(window, false);
            ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(window, false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemBarTintManager = new SystemBarTintManager(((MainActivity) getParentContext()));
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.select_skills_top_background_start_color);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        initUI();
    }

    private void initUI() {
        initSwipeRefreshLayout();
    }

    private void initData() {
        boolean isLogin = UserPreferences.getInstance().isLogin();

        if (isLogin) {
            mPresenter.fetchMyDataRequest();
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_orange);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @OnClick({R.id.iv_setting, R.id.rl_personal_information, R.id.rl_my_account, R.id.rl_my_photo_album, R.id.rl_my_collection, R.id.rl_authentication_center, R.id.rl_about_us})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                Intent settingIntent = new Intent(getParentContext(), SettingActivity.class);
                startActivity(settingIntent);
                break;

            case R.id.rl_personal_information:
                Intent personalInformationIntent = new Intent(getParentContext(), PersonalInformationActivity.class);
                startActivity(personalInformationIntent);
                break;

            case R.id.rl_my_account:
                boolean isUseFingerprintVerify = UserPreferences.getInstance().getIsUseFingerprintVerify();

                if (isExistGesturePwd) {
                    Intent intent = new Intent(getParentContext(), GesturePasswordActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_GESTURE_PASSWORD_TYPE, CommonConstant.GESTURE_PASSWORD_LOCK);
                    startActivity(intent);
                } else if (isUseFingerprintVerify) {
                    Intent intent = new Intent(getParentContext(), FingerprintVerifyActivity.class);
                    startActivity(intent);
                } else {
                    Intent myAccountIntent = new Intent(getParentContext(), MyAccountActivity.class);
                    startActivity(myAccountIntent);
                }
                break;

            case R.id.rl_my_photo_album:
                int userId = UserPreferences.getInstance().getUserId();

                Intent myPhotoAlbumIntent = new Intent(getParentContext(), MyPhotoAlbumActivity.class);
                myPhotoAlbumIntent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                startActivity(myPhotoAlbumIntent);
                break;

            case R.id.rl_my_collection:
                Intent myCollectionIntent = new Intent(getParentContext(), MyCollectionActivity.class);
                startActivity(myCollectionIntent);
                break;

            case R.id.rl_authentication_center:
                Intent authenticationCenterIntent = new Intent(getParentContext(), AuthenticationCenterActivity.class);
                startActivity(authenticationCenterIntent);
                break;

            case R.id.rl_about_us:
//                Intent aboutUsIntent = new Intent(getParentContext(), AboutUsActivity.class);
//                startActivity(aboutUsIntent);

                Intent aboutUsIntent = new Intent(getParentContext(), SimpleWebViewActivity.class);
                aboutUsIntent.putExtra(SimpleWebViewActivity.WEB_URL_KEY, ServiceAPIConstant.ABOUT_US_WEB_URL);
                startActivity(aboutUsIntent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.fetchMyDataRequest();
    }

    @Override
    public void setRefresh(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void showUserHeadPortraitUrl(String userHeadPortraitUrl) {
        ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, userHeadPortraitUrl, R.mipmap.ic_default_avatar);
    }

    @Override
    public void showName(String name) {
        nameTv.setText(name);
    }

    @Override
    public void showGender(int gender) {
        if (gender == CommonConstant.MALE) {
            genderIv.setBackgroundResource(R.mipmap.ic_male_white);
        } else if (gender == CommonConstant.FEMALE) {
            genderIv.setBackgroundResource(R.mipmap.ic_female_white);
        } else {
            genderIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showAge(int age) {
        if (age != -1) {
            ageTv.setText(String.valueOf(age) + "岁");
            ageTv.setVisibility(View.VISIBLE);
        } else {
            ageTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showWorkExperienceYear(int workExperienceYear) {
        if (workExperienceYear == 0) {
            workExperienceYearTv.setVisibility(View.GONE);
        } else {
            workExperienceYearTv.setText(String.valueOf(workExperienceYear) + "年工作经验");
            workExperienceYearTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMyCollectionAmount(int myCollectionAmount) {
        if (myCollectionAmount == 0) {
            myCollectionAmountTv.setVisibility(View.GONE);
        } else {
            myCollectionAmountTv.setText(String.valueOf(myCollectionAmount));
            myCollectionAmountTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess) {
        this.isZmrzAuthSuccess = isZmrzAuthSuccess;

        if (this.isZmrzAuthSuccess) {
            unauthorizedTv.setVisibility(View.GONE);
        } else {
            unauthorizedTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showIsExistGesturePwd(boolean isExistGesturePwd) {
        this.isExistGesturePwd = isExistGesturePwd;
    }

    @Override
    public void showResumeCompleteRate(int resumeCompleteRate) {
        StringBuilder stringBuilder = new StringBuilder("完善度：").append(String.valueOf(resumeCompleteRate) + "%");
        percentOfPerfectTv.setText(stringBuilder);
        percentOfPerfectTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        Window window = ((MainActivity) getParentContext()).getWindow();

        if (!hidden) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.select_skills_top_background_start_color));

                ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(window, false);
                ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(window, false);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                systemBarTintManager = new SystemBarTintManager(((MainActivity) getParentContext()));
                systemBarTintManager.setStatusBarTintEnabled(true);
                systemBarTintManager.setStatusBarTintResource(R.color.select_skills_top_background_start_color);

                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

            initData();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                if (null != systemBarTintManager) {
                    systemBarTintManager.setStatusBarTintEnabled(false);
                    systemBarTintManager.setStatusBarTintResource(Color.TRANSPARENT);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

}
