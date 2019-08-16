package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.presenter.user.LoginPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.SimpleWebViewActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.view.user.ILoginView;
import com.allintask.lingdao.widget.EditPasswordView;
import com.allintask.lingdao.widget.EditTextWithDelete;
import com.sina.weibo.sdk.utils.MD5;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMWXHandler;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2017/12/28.
 */

public class LoginActivity extends BaseActivity<ILoginView, LoginPresenter> implements ILoginView {

    @BindView(R.id.tv_content)
    TextView contentTv;
    @BindView(R.id.ll_phone_number_home_location)
    LinearLayout phoneNumberHomeLocationLL;
    @BindView(R.id.tv_phone_number_home_location)
    TextView phoneNumberHomeLocationTv;
    @BindView(R.id.etwd_sms_identify_code)
    EditTextWithDelete smsIdentifyCodeETWD;
    @BindView(R.id.btn_get_sms_identify_code)
    Button getSmsIdentifyCodeBtn;
    @BindView(R.id.ll_sms_identify_code)
    LinearLayout smsIdentifyCodeLL;
    @BindView(R.id.etwd_phone_number)
    EditTextWithDelete phoneNumberETWD;
    @BindView(R.id.epwv_password)
    EditPasswordView passwordEPWV;
    @BindView(R.id.cb_agree)
    CheckBox agreeCB;
    @BindView(R.id.tv_user_agreement)
    TextView userAgreementTv;
    @BindView(R.id.tv_forget_password)
    TextView forgetPasswordTv;
    @BindView(R.id.btn_login)
    Button loginBtn;
    @BindView(R.id.tv_login_mode)
    TextView loginModeTv;
    @BindView(R.id.iv_wechat_login)
    ImageView wechatLoginIv;
    @BindView(R.id.iv_qq_login)
    ImageView qqLoginIv;

    private CountDownTimer countDownTimer;
    private int loginType = CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE;
    private boolean isAgree = true;
    private int mobileCountryCodeId = 10040;
    private String regularEx = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$";
    private String wechatOpenid;
    private String wechatUnionid;
    private String wechatNickname;
    private String qqOpenid;
    private String qqUid;
    private String qqName;
    private String qqNickname;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter CreatePresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void init() {
        initUI();
        initData();
    }

    private void initUI() {
        phoneNumberHomeLocationTv.setText("86");

        phoneNumberETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginEnable(loginType);
            }
        });

        smsIdentifyCodeETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginEnable(loginType);
            }
        });

        passwordEPWV.getEditTextWithDel().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLoginEnable(loginType);
            }
        });

        agreeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAgree = true;
                } else {
                    isAgree = false;
                }

                checkLoginEnable(loginType);
            }
        });

        loginBtn.setEnabled(false);
        loginBtn.setClickable(false);
    }

    private void initData() {
        mPresenter.fetchDefaultPhoneNumberHomeLocationRequest();
    }

    private void checkLoginEnable(int loginType) {
        String phoneNumber = phoneNumberETWD.getText().toString().trim();

        switch (loginType) {
            case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE:
            case CommonConstant.LOGIN_TYPE_LOGIN_BY_WECHAT:
            case CommonConstant.LOGIN_TYPE_LOGIN_BY_QQ:
                String smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(smsIdentifyCode) && isAgree) {
                    loginBtn.setEnabled(true);
                    loginBtn.setClickable(true);
                } else {
                    loginBtn.setEnabled(false);
                    loginBtn.setClickable(false);
                }
                break;

            case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_PASSWORD:
                String password = passwordEPWV.getPassword();

                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)) {
                    loginBtn.setEnabled(true);
                    loginBtn.setClickable(true);
                } else {
                    loginBtn.setEnabled(false);
                    loginBtn.setClickable(false);
                }
                break;
        }
    }

    @OnClick({R.id.ll_phone_number_home_location, R.id.btn_get_sms_identify_code, R.id.tv_user_agreement, R.id.tv_forget_password, R.id.btn_login, R.id.tv_login_mode, R.id.iv_wechat_login, R.id.iv_qq_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_phone_number_home_location:
                Intent phoneNumberHomeLocationIntent = new Intent(getParentContext(), PhoneNumberHomeLocationActivity.class);
                startActivityForResult(phoneNumberHomeLocationIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.btn_get_sms_identify_code:
                String getSmsIdentifyCodePhoneNumber = phoneNumberETWD.getText().toString().trim();

                if (mobileCountryCodeId != 0) {
                    if (!TextUtils.isEmpty(getSmsIdentifyCodePhoneNumber)) {
                        if (TextUtils.isEmpty(regularEx)) {
                            switch (loginType) {
                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE:
                                    mPresenter.sendLoginSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_WECHAT:
                                    mPresenter.sendWechatSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_QQ:
                                    mPresenter.sendQQSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                                    break;
                            }
                        } else if (getSmsIdentifyCodePhoneNumber.matches(regularEx)) {
                            switch (loginType) {
                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE:
                                    mPresenter.sendLoginSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_WECHAT:
                                    mPresenter.sendWechatSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_QQ:
                                    mPresenter.sendQQSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                                    break;
                            }
                        } else {
                            showToast("手机号码格式不正确");
                        }
                    } else {
                        showToast("请填写手机号码");
                    }
                } else {
                    showToast("请选择手机号归属地");
                }
                break;

            case R.id.tv_user_agreement:
                Intent intent = new Intent(getParentContext(), SimpleWebViewActivity.class);
                intent.putExtra(SimpleWebViewActivity.WEB_URL_KEY, ServiceAPIConstant.USER_AGREEMENT_WEB_URL);
                startActivity(intent);
                break;

            case R.id.tv_forget_password:
                Intent forgetPasswordIntent = new Intent(getParentContext(), ForgetPasswordActivity.class);
                startActivity(forgetPasswordIntent);
                break;

            case R.id.btn_login:
                String loginPhoneNumber = phoneNumberETWD.getText().toString().trim();

                if (mobileCountryCodeId != 0) {
                    if (TextUtils.isEmpty(regularEx)) {
                        switch (loginType) {
                            case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE:
                                String smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();
                                mPresenter.loginByPhoneNumberAndSmsIdentifyCodeRequest(mobileCountryCodeId, loginPhoneNumber, smsIdentifyCode);
                                break;

                            case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_PASSWORD:
                                String password = passwordEPWV.getPassword();
                                int passwordLength = password.length();

                                if (passwordLength >= 6) {
                                    String MD5Password = MD5.hexdigest(password);
                                    mPresenter.loginByPhoneNumberAndPasswordRequest(mobileCountryCodeId, loginPhoneNumber, MD5Password);
                                } else {
                                    showToast("密码长度必须大于6位、小于16位");
                                }
                                break;

                            case CommonConstant.LOGIN_TYPE_LOGIN_BY_WECHAT:
                                String wechatPhoneNumber = phoneNumberETWD.getText().toString().trim();
                                String wechatSmsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

                                if (!TextUtils.isEmpty(wechatPhoneNumber) && !TextUtils.isEmpty(wechatSmsIdentifyCode) && !TextUtils.isEmpty(wechatUnionid)) {
                                    mPresenter.bindByWechatRequest(mobileCountryCodeId, wechatPhoneNumber, wechatSmsIdentifyCode, wechatUnionid);
                                }
                                break;

                            case CommonConstant.LOGIN_TYPE_LOGIN_BY_QQ:
                                String qqPhoneNumber = phoneNumberETWD.getText().toString().trim();
                                String qqSmsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

                                if (!TextUtils.isEmpty(qqPhoneNumber) && !TextUtils.isEmpty(qqSmsIdentifyCode) && !TextUtils.isEmpty(qqUid)) {
                                    mPresenter.bindByQQRequest(mobileCountryCodeId, qqPhoneNumber, qqSmsIdentifyCode, qqUid);
                                }
                                break;
                        }
                    } else {
                        if (loginPhoneNumber.matches(regularEx)) {
                            switch (loginType) {
                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE:
                                    String smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();
                                    mPresenter.loginByPhoneNumberAndSmsIdentifyCodeRequest(mobileCountryCodeId, loginPhoneNumber, smsIdentifyCode);
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_PASSWORD:
                                    String password = passwordEPWV.getPassword();
                                    int passwordLength = password.length();

                                    if (passwordLength >= 6) {
                                        String MD5Password = MD5.hexdigest(password);
                                        mPresenter.loginByPhoneNumberAndPasswordRequest(mobileCountryCodeId, loginPhoneNumber, MD5Password);
                                    } else {
                                        showToast("密码长度必须大于6位、小于16位");
                                    }
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_WECHAT:
                                    String wechatPhoneNumber = phoneNumberETWD.getText().toString().trim();
                                    String wechatSmsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

                                    if (!TextUtils.isEmpty(wechatPhoneNumber) && !TextUtils.isEmpty(wechatSmsIdentifyCode) && !TextUtils.isEmpty(wechatUnionid)) {
                                        mPresenter.bindByWechatRequest(mobileCountryCodeId, wechatPhoneNumber, wechatSmsIdentifyCode, wechatUnionid);
                                    }
                                    break;

                                case CommonConstant.LOGIN_TYPE_LOGIN_BY_QQ:
                                    String qqPhoneNumber = phoneNumberETWD.getText().toString().trim();
                                    String qqSmsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

                                    if (!TextUtils.isEmpty(qqPhoneNumber) && !TextUtils.isEmpty(qqSmsIdentifyCode) && !TextUtils.isEmpty(qqUid)) {
                                        mPresenter.bindByQQRequest(mobileCountryCodeId, qqPhoneNumber, qqSmsIdentifyCode, qqUid);
                                    }
                                    break;
                            }
                        } else {
                            showToast("手机号码格式不正确");
                        }
                    }
                } else {
                    showToast("请选择手机号归属地");
                }
                break;

            case R.id.tv_login_mode:
                switch (smsIdentifyCodeLL.getVisibility()) {
                    case View.INVISIBLE:
                        loginType = CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE;

                        smsIdentifyCodeLL.setVisibility(View.VISIBLE);
                        passwordEPWV.setVisibility(View.INVISIBLE);
                        agreeCB.setVisibility(View.VISIBLE);
                        userAgreementTv.setVisibility(View.VISIBLE);
                        forgetPasswordTv.setVisibility(View.GONE);

                        loginModeTv.setText(getString(R.string.password_login));
                        break;

                    case View.VISIBLE:
                        loginType = CommonConstant.LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_PASSWORD;

                        smsIdentifyCodeLL.setVisibility(View.INVISIBLE);
                        passwordEPWV.setVisibility(View.VISIBLE);
                        agreeCB.setVisibility(View.INVISIBLE);
                        userAgreementTv.setVisibility(View.INVISIBLE);
                        forgetPasswordTv.setVisibility(View.VISIBLE);

                        loginModeTv.setText(getString(R.string.sms_identify_code_login));
                        break;
                }
                break;

            case R.id.iv_wechat_login:
                UMShareAPI.get(getParentContext()).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, mPresenter.getUMAuthListener());
                break;

            case R.id.iv_qq_login:
                UMShareAPI.get(getParentContext()).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, mPresenter.getUMAuthListener());
                break;
        }
    }

    @Override
    public void onShowPhoneNumberHomeLocationMobileCountryCodeId(int mobileCountryCodeId) {
        this.mobileCountryCodeId = mobileCountryCodeId;
    }

    @Override
    public void onShowPhoneNumberHomeLocationValue(String value) {
        phoneNumberHomeLocationTv.setText(value);
    }

    @Override
    public void onShowPhoneNumberHomeLocationRegularEx(String regularEx) {
        this.regularEx = regularEx;
    }

    @Override
    public void onCountDownTimerTick(long millisUntilFinished) {
        getSmsIdentifyCodeBtn.setEnabled(false);
        getSmsIdentifyCodeBtn.setClickable(false);
        getSmsIdentifyCodeBtn.setText(String.valueOf(millisUntilFinished / 1000) + "s");
    }

    @Override
    public void onCountDownTimerFinish() {
        getSmsIdentifyCodeBtn.setEnabled(true);
        getSmsIdentifyCodeBtn.setClickable(true);
        getSmsIdentifyCodeBtn.setText(getString(R.string.get));
    }

    @Override
    public void onWechatUMAuthComplete(Map<String, String> map) {
        if (null != map) {
            wechatOpenid = TypeUtils.getString(map.get(ApiKey.WECHAT_OPENID), "");
            wechatUnionid = TypeUtils.getString(map.get(ApiKey.WECHAT_UNIONID), "");
            wechatNickname = TypeUtils.getString(map.get(ApiKey.WECHAT_NAME), "");
            mPresenter.loginByWechatRequest(wechatOpenid, wechatUnionid, wechatNickname);
        }
    }

    @Override
    public void onWechatUMAuthError(Throwable throwable) {
        String errorMessage = throwable.getMessage();

        if (errorMessage.contains("没有安装应用")) {
            showToast("没有安装微信");
        } else {
            showToast("微信验证失败");
        }
    }

    @Override
    public void onQQUMAuthComplete(Map<String, String> map) {
        if (null != map) {
            qqOpenid = TypeUtils.getString(map.get(ApiKey.QQ_OPENID), "");
            qqUid = TypeUtils.getString(map.get(ApiKey.QQ_UID), "");
            qqName = TypeUtils.getString(map.get(ApiKey.QQ_NAME), "");
            qqNickname = TypeUtils.getString(map.get(ApiKey.QQ_NAME), "");
            mPresenter.loginByQQRequest(qqOpenid, qqUid, qqName, qqNickname);
        }
    }

    @Override
    public void onQQUMAuthError(Throwable throwable) {
        String errorMessage = throwable.getMessage();

        if (errorMessage.contains("没有安装应用")) {
            showToast("没有安装QQ");
        } else {
            showToast("QQ验证失败");
        }
    }

    @Override
    public void onLoginSuccess() {
        mPresenter.checkNeedGuideRequest();
    }

    @Override
    public void onWechatLoginIsBinding() {
        mPresenter.checkNeedGuideRequest();
    }

    @Override
    public void onWechatLoginUnbind() {
        loginType = CommonConstant.LOGIN_TYPE_LOGIN_BY_WECHAT;

        contentTv.setVisibility(View.VISIBLE);
        loginModeTv.setVisibility(View.GONE);

        contentTv.setText(R.string.wechat_login_content);
        loginBtn.setText(getString(R.string.bind));
    }

    @Override
    public void onQQLoginIsBinding() {
        mPresenter.checkNeedGuideRequest();
    }

    @Override
    public void onQQLoginUnbind() {
        loginType = CommonConstant.LOGIN_TYPE_LOGIN_BY_QQ;

        contentTv.setVisibility(View.VISIBLE);
        loginModeTv.setVisibility(View.GONE);

        contentTv.setText(R.string.qq_login_content);
        loginBtn.setText(getString(R.string.bind));
    }

    @Override
    public void onCheckNeedGuideSuccess(boolean isNeedGuide) {
        if (isNeedGuide) {
            Intent intent = new Intent(getParentContext(), CompletePersonalInformationActivity.class);
            intent.putExtra(CommonConstant.EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE, CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_SET);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getParentContext(), MainActivity.class);
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void onSendSmsIdentifyCodeSuccess() {
        showToast("发送验证码成功");
        countDownTimer = mPresenter.startCountDownTimer();
    }

    @Override
    public void onBackPressed() {
        AllintaskApplication.IS_STARTED = false;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getParentContext()).onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE && null != data) {
            mobileCountryCodeId = data.getIntExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_MOBILE_COUNTRY_CODE_ID, 0);
            String value = data.getStringExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_VALUE);
            regularEx = data.getStringExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_REGULAR_EX);

            phoneNumberHomeLocationTv.setText(value);
        }
    }

    @Override
    protected void onDestroy() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

}
