package com.allintask.lingdao.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SettingPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ISettingView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.custom.image.imageloader.ImageLoaderManager;
import cn.tanjiajun.sdk.component.util.DialogUtils;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class SettingActivity extends BaseActivity<ISettingView, SettingPresenter> implements ISettingView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.rl_account_management)
    RelativeLayout accountManagementRL;
    @BindView(R.id.rl_security_management)
    RelativeLayout securityManagementRL;
    @BindView(R.id.iv_security)
    ImageView securityIv;
    @BindView(R.id.tv_security)
    TextView securityTv;
    @BindView(R.id.tv_gathering_bank_card)
    TextView gatheringBankCardTv;
    @BindView(R.id.rl_bind_wechat)
    RelativeLayout bindWechatRL;
    @BindView(R.id.tv_bind_wechat_status)
    TextView bindWechatStatusTv;
    @BindView(R.id.rl_bind_qq)
    RelativeLayout bindQQRL;
    @BindView(R.id.tv_bind_qq_status)
    TextView bindQQStatusTv;
    @BindView(R.id.rl_new_message_notification)
    RelativeLayout newMessageNotificationRL;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout clearCacheRL;
    @BindView(R.id.tv_cache)
    TextView cacheTv;
    @BindView(R.id.tv_version_name)
    TextView versionNameTv;
    @BindView(R.id.btn_logout)
    Button logoutBtn;

    private ImageLoaderManager imageLoaderManager;

    private int mobileCountryCodeId = -1;
    private String mailBox;
    private boolean isExistLoginPassword = false;
    private boolean isExistPaymentPassword = false;
    private int bankId = -1;
    private String bankCardName;
    private boolean isBindQQ = false;
    private boolean isBindWechat = false;

    private String wechatOpenid;
    private String wechatUnionid;
    private String wechatNickname;
    private String qqOpenid;
    private String qqUid;
    private String qqName;
    private String qqNickname;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected SettingPresenter CreatePresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.setting));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        imageLoaderManager = new ImageLoaderManager();
        String imageLoaderSize = imageLoaderManager.getImageLoaderCacheSize(this);

        if (imageLoaderSize.equals("0.0Byte")) {
            cacheTv.setText("");

            clearCacheRL.setOnClickListener(null);
        } else {
            cacheTv.setText(imageLoaderSize);

            clearCacheRL.setOnClickListener(this);
        }

        StringBuilder stringBuilder = new StringBuilder("v").append(getVersionName());
        versionNameTv.setText(stringBuilder);
    }

    private void initData() {
        mPresenter.fetchSettingRequest();
    }

    private String getVersionName() {
        String versionName = null;
        PackageManager packageManager = getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @OnClick({R.id.rl_account_management, R.id.rl_security_management, R.id.rl_set_and_modify_gathering_bank_card, R.id.rl_bind_wechat, R.id.rl_bind_qq, R.id.rl_new_message_notification, R.id.rl_clear_cache, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_account_management:
//                if (mobileCountryCodeId != -1) {
//                    Intent modifyMobileIntent = new Intent(getParentContext(), ModifyMobileActivity.class);
//                    modifyMobileIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
//                    startActivityForResult(modifyMobileIntent, CommonConstant.REQUEST_CODE);
//                }

                Intent modifyPhoneNumberIntent = new Intent(getParentContext(), AccountManagementActivity.class);
                startActivity(modifyPhoneNumberIntent);
                break;

            case R.id.rl_security_management:
//                if (TextUtils.isEmpty(mailBox)) {
//                    Intent bindModifyMailboxIntent = new Intent(getParentContext(), BindMailboxActivity.class);
//                    startActivityForResult(bindModifyMailboxIntent, CommonConstant.REQUEST_CODE);
//                } else {
//                    Intent bindMailboxIntent = new Intent(getParentContext(), ModifyMailboxActivity.class);
//                    bindMailboxIntent.putExtra(CommonConstant.EXTRA_MAILBOX, mailBox);
//                    startActivityForResult(bindMailboxIntent, CommonConstant.REQUEST_CODE);
//                }

                Intent securityManagementIntent = new Intent(getParentContext(), SecurityManagementActivity.class);
                startActivity(securityManagementIntent);
                break;

//            case R.id.rl_set_and_modify_login_password:
//                if (!isExistLoginPassword) {
//                    Intent setLoginPasswordIntent = new Intent(getParentContext(), SetLoginPasswordActivity.class);
//                    setLoginPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
//                    startActivityForResult(setLoginPasswordIntent, CommonConstant.REQUEST_CODE);
//                } else {
//                    Intent modifyLoginPasswordIntent = new Intent(getParentContext(), ModifyLoginPasswordActivity.class);
//                    modifyLoginPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
//                    startActivityForResult(modifyLoginPasswordIntent, CommonConstant.REQUEST_CODE);
//                }
//                break;
//
//            case R.id.rl_set_and_modify_payment_password:
//                Intent paymentPasswordIntent = new Intent(getParentContext(), PaymentPasswordActivity.class);
//
//                if (!isExistPaymentPassword) {
//                    paymentPasswordIntent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
//                } else {
//                    paymentPasswordIntent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_MODIFY_PAYMENT_PASSWORD);
//                }
//
//                paymentPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
//                startActivityForResult(paymentPasswordIntent, CommonConstant.REQUEST_CODE);
//                break;

            case R.id.rl_set_and_modify_gathering_bank_card:
                Intent setAndModifyGatheringBankCardIntent = new Intent(getParentContext(), BankCardSettingActivity.class);

                if (bankId == -1) {
                    setAndModifyGatheringBankCardIntent.putExtra(CommonConstant.EXTRA_BANK_CARD_SETTING_TYPE, CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD);
                } else {
                    setAndModifyGatheringBankCardIntent.putExtra(CommonConstant.EXTRA_BANK_CARD_SETTING_TYPE, CommonConstant.BANK_CARD_SETTING_TYPE_MODIFY_BANK_CARD);
                    setAndModifyGatheringBankCardIntent.putExtra(CommonConstant.EXTRA_BANK_ID, bankId);
                    setAndModifyGatheringBankCardIntent.putExtra(CommonConstant.EXTRA_BANK_CARD_NAME, bankCardName);
                }

                startActivityForResult(setAndModifyGatheringBankCardIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.rl_bind_wechat:
                if (!isBindWechat) {
                    UMShareAPI.get(getParentContext()).getPlatformInfo(SettingActivity.this, SHARE_MEDIA.WEIXIN, mPresenter.getUMAuthListener());
                } else {
                    DialogUtils.showAlertDialog(getParentContext(), "确认解绑微信", "请确认是否解绑微信！", "确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mPresenter.unbindWechatRequest();
                        }
                    }, "再看看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.rl_bind_qq:
                if (!isBindQQ) {
                    UMShareAPI.get(getParentContext()).getPlatformInfo(SettingActivity.this, SHARE_MEDIA.QQ, mPresenter.getUMAuthListener());
                } else {
                    DialogUtils.showAlertDialog(getParentContext(), "确认解绑QQ", "请确认是否解绑QQ！", "确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mPresenter.unbindQQRequest();
                        }
                    }, "再看看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.rl_new_message_notification:
                Intent newMessageNotificationIntent = new Intent(getParentContext(), NewMessageNotificationActivity.class);
                startActivity(newMessageNotificationIntent);
                break;

            case R.id.rl_clear_cache:
                imageLoaderManager.cleanImageLoaderCache(this);

                showToast("清除成功");

                cacheTv.setText("");

                clearCacheRL.setOnClickListener(null);
                break;

            case R.id.btn_logout:
                mPresenter.logoutRequest();
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

//        String desensitizationPhoneNumber = PhoneUtils.maskPhone(phoneNumber);
//        phoneNumberTv.setText(desensitizationPhoneNumber);
    }

    @Override
    public void onShowIsExistGesturePwd(boolean isExistGesturePwd) {
        boolean isUseFingerprintVerify = UserPreferences.getInstance().getIsUseFingerprintVerify();

        if (!isExistGesturePwd && !isUseFingerprintVerify) {
            securityIv.setBackgroundResource(R.mipmap.ic_unsafe);
            securityTv.setText(getString(R.string.unsafe));
        } else {
            securityIv.setBackgroundResource(R.mipmap.ic_safe);
            securityTv.setText(getString(R.string.safe));
        }

        securityIv.setVisibility(View.VISIBLE);
        securityTv.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void onShowMailbox(String mailbox) {
//        this.mailBox = mailbox;
//
//        if (!TextUtils.isEmpty(this.mailBox)) {
//            mailboxTv.setText(this.mailBox);
//            mailboxTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
//        } else {
//            mailboxTv.setText(getString(R.string.unbind));
//            mailboxTv.setTextColor(getResources().getColor(R.color.text_red));
//        }
//    }
//
//    @Override
//    public void onShowIsExistLoginPassword(boolean isExistLoginPassword) {
//        this.isExistLoginPassword = isExistLoginPassword;
//
//        if (this.isExistLoginPassword) {
//            loginPasswordTv.setText("******");
//            loginPasswordTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
//        } else {
//            loginPasswordTv.setText(getString(R.string.go_to_set));
//            loginPasswordTv.setTextColor(getResources().getColor(R.color.text_red));
//        }
//    }
//
//    @Override
//    public void onShowIsExistPaymentPassword(boolean isExistPaymentPassword) {
//        this.isExistPaymentPassword = isExistPaymentPassword;
//
//        if (this.isExistPaymentPassword) {
//            paymentPasswordTv.setText("******");
//            paymentPasswordTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
//        } else {
//            paymentPasswordTv.setText(getString(R.string.go_to_set));
//            paymentPasswordTv.setTextColor(getResources().getColor(R.color.text_red));
//        }
//    }

    @Override
    public void onShowBankId(Integer bankId) {
        this.bankId = bankId;
    }

    @Override
    public void onShowBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;

        if (bankId != -1 && !TextUtils.isEmpty(this.bankCardName)) {
            gatheringBankCardTv.setText(String.valueOf(this.bankCardName));
            gatheringBankCardTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
        } else {
            gatheringBankCardTv.setText(getString(R.string.go_to_set));
            gatheringBankCardTv.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    @Override
    public void onShowQQName(String qqName) {
        if (!TextUtils.isEmpty(qqName)) {
            isBindQQ = true;
            bindQQStatusTv.setText(qqName);
            bindQQStatusTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
        } else {
            isBindQQ = false;
            bindQQStatusTv.setText(getString(R.string.unbind));
            bindQQStatusTv.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    @Override
    public void onShowWechatName(String wechatName) {
        if (!TextUtils.isEmpty(wechatName)) {
            isBindWechat = true;
            bindWechatStatusTv.setText(wechatName);
            bindWechatStatusTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
        } else {
            isBindWechat = false;
            bindWechatStatusTv.setText(getString(R.string.unbind));
            bindWechatStatusTv.setTextColor(getResources().getColor(R.color.text_red));
        }
    }

    @Override
    public void onWechatUMAuthComplete(Map<String, String> map) {
        if (null != map) {
            wechatOpenid = TypeUtils.getString(map.get(ApiKey.WECHAT_OPENID), "");
            wechatUnionid = TypeUtils.getString(map.get(ApiKey.WECHAT_UNIONID), "");
            wechatNickname = TypeUtils.getString(map.get(ApiKey.WECHAT_NAME), "");
            mPresenter.bindWechatRequest(wechatOpenid, wechatUnionid, wechatNickname);
        }
    }

    @Override
    public void onWechatUMAuthError() {
        showToast("微信验证失败");
    }

    @Override
    public void onQQUMAuthComplete(Map<String, String> map) {
        if (null != map) {
            qqOpenid = TypeUtils.getString(map.get(ApiKey.QQ_OPENID), "");
            qqUid = TypeUtils.getString(map.get(ApiKey.QQ_UID), "");
            qqName = TypeUtils.getString(map.get(ApiKey.QQ_NAME), "");
            qqNickname = TypeUtils.getString(map.get(ApiKey.QQ_NAME), "");
            mPresenter.bindQQRequest(qqOpenid, qqUid, qqName, qqNickname);
        }
    }

    @Override
    public void onQQUMAuthError() {
        showToast("QQ验证失败");
    }

    @Override
    public void onBindWechatSuccess() {
        initData();
    }

    @Override
    public void onBindQQSuccess() {
        initData();
    }

    @Override
    public void onUnbindWechatSuccess() {
        initData();
    }

    @Override
    public void onUnbindQQSuccess() {
        initData();
    }

    @Override
    public void onLogoutSuccess() {
        Intent intent = new Intent(getParentContext(), MainActivity.class);
        intent.putExtra(CommonConstant.ACTION_LOGOUT, true);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getParentContext()).onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
            initData();
        }
    }

}
