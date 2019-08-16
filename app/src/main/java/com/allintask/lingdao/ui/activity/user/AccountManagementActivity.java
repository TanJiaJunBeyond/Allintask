package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.AccountManagementPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.PhoneUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IAccountManagemantView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/5/24.
 */

public class AccountManagementActivity extends BaseActivity<IAccountManagemantView, AccountManagementPresenter> implements IAccountManagemantView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.rl_modify_phone_number)
    RelativeLayout modifyPhoneNumberRL;
    @BindView(R.id.tv_phone_number)
    TextView phoneNumberTv;
    @BindView(R.id.rl_bind_and_modify_mailbox)
    RelativeLayout bindAndModifyMailboxRL;
    @BindView(R.id.tv_mailbox)
    TextView mailboxTv;
    @BindView(R.id.rl_set_and_modify_login_password)
    RelativeLayout setAndModifyLoginPasswordRL;
    @BindView(R.id.tv_login_password)
    TextView loginPasswordTv;

    private int mobileCountryCodeId = -1;
    private String mailBox;
    private boolean isExistLoginPassword = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account_management;
    }

    @Override
    protected AccountManagementPresenter CreatePresenter() {
        return new AccountManagementPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.account_management));

        setSupportActionBar(toolbar);
    }

    private void initData() {
        mPresenter.fetchSettingRequest();
    }

    @OnClick({R.id.rl_modify_phone_number, R.id.rl_bind_and_modify_mailbox, R.id.rl_set_and_modify_login_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_modify_phone_number:
                if (mobileCountryCodeId != -1) {
                    Intent modifyMobileIntent = new Intent(getParentContext(), ModifyMobileActivity.class);
                    modifyMobileIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
                    startActivityForResult(modifyMobileIntent, CommonConstant.REQUEST_CODE);
                }
                break;

            case R.id.rl_bind_and_modify_mailbox:
                if (TextUtils.isEmpty(mailBox)) {
                    Intent bindModifyMailboxIntent = new Intent(getParentContext(), BindMailboxActivity.class);
                    startActivityForResult(bindModifyMailboxIntent, CommonConstant.REQUEST_CODE);
                } else {
                    Intent bindMailboxIntent = new Intent(getParentContext(), ModifyMailboxActivity.class);
                    bindMailboxIntent.putExtra(CommonConstant.EXTRA_MAILBOX, mailBox);
                    startActivityForResult(bindMailboxIntent, CommonConstant.REQUEST_CODE);
                }
                break;

            case R.id.rl_set_and_modify_login_password:
                if (!isExistLoginPassword) {
                    Intent setLoginPasswordIntent = new Intent(getParentContext(), SetLoginPasswordActivity.class);
                    setLoginPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
                    startActivityForResult(setLoginPasswordIntent, CommonConstant.REQUEST_CODE);
                } else {
                    Intent modifyLoginPasswordIntent = new Intent(getParentContext(), ModifyLoginPasswordActivity.class);
                    modifyLoginPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
                    startActivityForResult(modifyLoginPasswordIntent, CommonConstant.REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onShowMobileCountryCodeId(int mobileCountryCodeId) {
        this.mobileCountryCodeId = mobileCountryCodeId;
        UserPreferences.getInstance().setMobileCountryCodeId(this.mobileCountryCodeId);
    }

    @Override
    public void onShowPhoneNumber(String phoneNumber) {
        UserPreferences.getInstance().setPhoneNumber(phoneNumber);

        String desensitizationPhoneNumber = PhoneUtils.maskPhone(phoneNumber);
        phoneNumberTv.setText(desensitizationPhoneNumber);
    }

    @Override
    public void onShowMailbox(String mailbox) {
        this.mailBox = mailbox;

        if (!TextUtils.isEmpty(this.mailBox)) {
            mailboxTv.setText(this.mailBox);
            mailboxTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
        } else {
            mailboxTv.setText(getString(R.string.unbind));
            mailboxTv.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    @Override
    public void onShowIsExistLoginPassword(boolean isExistLoginPassword) {
        this.isExistLoginPassword = isExistLoginPassword;

        if (this.isExistLoginPassword) {
            loginPasswordTv.setText("******");
            loginPasswordTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
        } else {
            loginPasswordTv.setText(getString(R.string.go_to_set));
            loginPasswordTv.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
            initData();
        }
    }

}
