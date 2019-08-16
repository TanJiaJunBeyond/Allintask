package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.AuthenticationCenterPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.IAuthenticationCenterView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/6/15.
 */

public class AuthenticationCenterActivity extends BaseActivity<IAuthenticationCenterView, AuthenticationCenterPresenter> implements IAuthenticationCenterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.rl_zhi_ma_credit_authentication)
    RelativeLayout zhiMaCreditAuthenticationRL;
    @BindView(R.id.tv_zhi_ma_credit_authentication)
    TextView zhiMaCreditAuthenticationTv;
    @BindView(R.id.iv_zhi_ma_credit_authentication)
    ImageView zhiMaCreditAuthenticationIv;

    private boolean mIsZhiMaCreditAuthenticationSuccess = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authentication_center;
    }

    @Override
    protected AuthenticationCenterPresenter CreatePresenter() {
        return new AuthenticationCenterPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.authentication_center));

        setSupportActionBar(toolbar);
    }

    private void initData() {
        mPresenter.fetchMyDataRequest();
    }

    @OnClick({R.id.rl_zhi_ma_credit_authentication})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_zhi_ma_credit_authentication:
                Intent intent = new Intent(getParentContext(), IdentifyAuthenticationActivity.class);
                intent.putExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, mIsZhiMaCreditAuthenticationSuccess);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess) {
        mIsZhiMaCreditAuthenticationSuccess = isZmrzAuthSuccess;

        if (mIsZhiMaCreditAuthenticationSuccess) {
            zhiMaCreditAuthenticationTv.setText(getString(R.string.authenticated));
            zhiMaCreditAuthenticationTv.setTextColor(getResources().getColor(R.color.theme_orange));

            zhiMaCreditAuthenticationIv.setVisibility(View.VISIBLE);
        } else {
            zhiMaCreditAuthenticationTv.setText(getString(R.string.unauthorized));
            zhiMaCreditAuthenticationTv.setTextColor(getResources().getColor(R.color.text_dark_black));

            zhiMaCreditAuthenticationIv.setVisibility(View.GONE);
        }

        zhiMaCreditAuthenticationTv.setVisibility(View.VISIBLE);
    }

}
