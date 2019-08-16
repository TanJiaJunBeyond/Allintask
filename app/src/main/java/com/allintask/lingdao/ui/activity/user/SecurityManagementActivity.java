package com.allintask.lingdao.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SecurityManagementPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ISecurityManagementView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.component.custom.dialog.BasicDialog;
import cn.tanjiajun.sdk.component.util.DialogUtils;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class SecurityManagementActivity extends BaseActivity<ISecurityManagementView, SecurityManagementPresenter> implements ISecurityManagementView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.rl_set_and_modify_payment_password)
    RelativeLayout setAndModifyPaymentPasswordRL;
    @BindView(R.id.rl_modify_payment_password)
    RelativeLayout modifyPaymentPasswordRL;
    @BindView(R.id.view_payment_password_divider_line)
    View paymentPasswordDividerLineView;
    @BindView(R.id.rl_forget_payment_password)
    RelativeLayout forgetPaymentPasswordRL;
    @BindView(R.id.tv_payment_password)
    TextView paymentPasswordTv;
    @BindView(R.id.rl_fingerprint_unlock)
    RelativeLayout fingerprintUnlockRL;
    @BindView(R.id.sc_fingerprint_unlock)
    SwitchCompat fingerprintUnlockSC;
    @BindView(R.id.view_fingerprint_unlock_divider_line)
    View fingerprintUnlockDividerLineView;
    @BindView(R.id.sc_gesture_unlock)
    SwitchCompat gestureUnlockSC;

    private int mMobileCountryCodeId = -1;
    //    private boolean mIsExistPaymentPassword = false;
    private boolean isExistGesturePassword;
    private boolean mIsZmrzAuthSuccess;
    private BasicDialog fingerprintUnlockDialog;
    private BasicDialog gestureUnlockDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_security_management;
    }

    @Override
    protected SecurityManagementPresenter CreatePresenter() {
        return new SecurityManagementPresenter();
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

        titleTv.setText(getString(R.string.security_management));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        if (FingerprintManagerCompat.from(getParentContext()).isHardwareDetected() && FingerprintManagerCompat.from(getParentContext()).hasEnrolledFingerprints()) {
            fingerprintUnlockRL.setVisibility(View.VISIBLE);
            fingerprintUnlockDividerLineView.setVisibility(View.VISIBLE);

            boolean isUseFingerprintVerify = UserPreferences.getInstance().getIsUseFingerprintVerify();

            if (isUseFingerprintVerify) {
                fingerprintUnlockSC.setChecked(true);
            } else {
                fingerprintUnlockSC.setChecked(false);
            }

            fingerprintUnlockSC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = fingerprintUnlockSC.isChecked();

                    if (isChecked) {
                        if (isExistGesturePassword) {
                            gestureUnlockSC.setChecked(false);
                            mPresenter.deleteGesturePasswordRequest();
                        }

                        fingerprintUnlockSC.setChecked(false);

                        Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_DELETE_GESTURE_PASSWORD);
                        startActivityForResult(intent, CommonConstant.FINGERPRINT_VERIFY_REQUEST_CODE);
                    } else {
                        if (isExistGesturePassword) {
                            gestureUnlockSC.setChecked(false);
                            mPresenter.deleteGesturePasswordRequest();
                        } else {
                            fingerprintUnlockDialog = DialogUtils.showAlertDialog(getParentContext(), "关闭指纹账户锁", "关闭账户锁后，进入钱包将没有验证方式", getString(R.string.go_on), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    UserPreferences.getInstance().setIsUseFingerprintVerify(false);
                                }
                            }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fingerprintUnlockSC.setChecked(true);
                                }
                            });

                            fingerprintUnlockDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    fingerprintUnlockSC.setChecked(true);
                                }
                            });
                        }
                    }
                }
            });
        } else {
            fingerprintUnlockRL.setVisibility(View.GONE);
            fingerprintUnlockDividerLineView.setVisibility(View.GONE);
        }

        gestureUnlockSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = gestureUnlockSC.isChecked();

                if (isChecked) {
                    gestureUnlockSC.setChecked(false);

                    Intent intent = new Intent(getParentContext(), GesturePasswordActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_GESTURE_PASSWORD_TYPE, CommonConstant.SET_GESTURE_PASSWORD);
                    startActivityForResult(intent, CommonConstant.SET_GESTURE_PASSWORD_REQUEST_CODE);
                } else {
                    boolean isUseFingerprintVerify = UserPreferences.getInstance().getIsUseFingerprintVerify();

                    if (isUseFingerprintVerify) {
                        fingerprintUnlockSC.setChecked(false);
                        UserPreferences.getInstance().setIsUseFingerprintVerify(false);
                    } else {
                        gestureUnlockDialog = DialogUtils.showAlertDialog(getParentContext(), "关闭手势账户锁", "关闭账户锁后，进入钱包将没有验证方式", getString(R.string.go_on), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                gestureUnlockSC.setChecked(true);

                                Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                                intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_DELETE_GESTURE_PASSWORD);
                                startActivityForResult(intent, CommonConstant.DELETE_GESTURE_PASSWORD_REQUEST_CODE);
                            }
                        }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                gestureUnlockSC.setChecked(true);
                            }
                        });

                        gestureUnlockDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                gestureUnlockSC.setChecked(true);
                            }
                        });
                    }
                }
            }
        });
    }

    private void initData() {
        mPresenter.fetchSettingRequest();
    }

    @OnClick({R.id.rl_set_and_modify_payment_password, R.id.rl_modify_payment_password, R.id.rl_forget_payment_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_set_and_modify_payment_password:
                if (mIsZmrzAuthSuccess) {
                    Intent setPaymentPasswordIntent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                    setPaymentPasswordIntent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
                    setPaymentPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mMobileCountryCodeId);
//                startActivityForResult(setPaymentPasswordIntent, CommonConstant.REQUEST_CODE);
                    startActivity(setPaymentPasswordIntent);
                } else {
                    DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "实名认证通过后才能操作设置支付密码哦！", getString(R.string.go_to_verify), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getParentContext(), IdentifyAuthenticationActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, mIsZmrzAuthSuccess);
                            startActivity(intent);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.rl_modify_payment_password:
                if (mIsZmrzAuthSuccess) {
                    Intent modifyPaymentPasswordIntent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                    modifyPaymentPasswordIntent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_MODIFY_PAYMENT_PASSWORD);
                    modifyPaymentPasswordIntent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mMobileCountryCodeId);
//                startActivityForResult(modifyPaymentPasswordIntent, CommonConstant.REQUEST_CODE);
                    startActivity(modifyPaymentPasswordIntent);
                } else {
                    DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "实名认证通过后才能操作修改支付密码哦！", getString(R.string.go_to_verify), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getParentContext(), IdentifyAuthenticationActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, mIsZmrzAuthSuccess);
                            startActivity(intent);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.rl_forget_payment_password:
                if (mIsZmrzAuthSuccess) {
                    Intent forgetPaymentPasswordIntent = new Intent(getParentContext(), SelectResetPaymentPasswordWayActivity.class);
                    startActivity(forgetPaymentPasswordIntent);
                } else {
                    DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "实名认证通过后才能操作忘记支付密码哦！", getString(R.string.go_to_verify), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getParentContext(), IdentifyAuthenticationActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, mIsZmrzAuthSuccess);
                            startActivity(intent);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onShowMobileCountryCodeId(int mobileCountryCodeId) {
        mMobileCountryCodeId = mobileCountryCodeId;
        UserPreferences.getInstance().setMobileCountryCodeId(this.mMobileCountryCodeId);
    }

    @Override
    public void onShowIsExistPaymentPassword(boolean isExistPaymentPassword) {
//        mIsExistPaymentPassword = isExistPaymentPassword;
//
//        if (mIsExistPaymentPassword) {
//            paymentPasswordTv.setText("******");
//            paymentPasswordTv.setTextColor(getResources().getColor(R.color.text_dark_gray));
//        } else {
//            paymentPasswordTv.setText(getString(R.string.go_to_set));
//            paymentPasswordTv.setTextColor(getResources().getColor(R.color.text_red));
//        }

        if (isExistPaymentPassword) {
            setAndModifyPaymentPasswordRL.setVisibility(View.GONE);
            modifyPaymentPasswordRL.setVisibility(View.VISIBLE);
            paymentPasswordDividerLineView.setVisibility(View.VISIBLE);
            forgetPaymentPasswordRL.setVisibility(View.VISIBLE);
        } else {
            setAndModifyPaymentPasswordRL.setVisibility(View.VISIBLE);
            modifyPaymentPasswordRL.setVisibility(View.GONE);
            paymentPasswordDividerLineView.setVisibility(View.GONE);
            forgetPaymentPasswordRL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowIsExistGesturePwd(boolean isExistGesturePwd) {
        isExistGesturePassword = isExistGesturePwd;

        if (isExistGesturePassword) {
            fingerprintUnlockSC.setChecked(false);
            UserPreferences.getInstance().setIsUseFingerprintVerify(false);
        }

        gestureUnlockSC.setChecked(isExistGesturePassword);
    }

    @Override
    public void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess) {
        mIsZmrzAuthSuccess = isZmrzAuthSuccess;
    }

    @Override
    public void onDeleteGesturePasswordSuccess() {
//        showToast("删除手势密码成功");
        initData();
    }

    @Override
    public void onDeleteGesturePasswordFail(String errorMessage) {
        showToast(errorMessage);
        boolean isUseFingerprintVerify = UserPreferences.getInstance().getIsUseFingerprintVerify();

        if (isUseFingerprintVerify) {
            UserPreferences.getInstance().setIsUseFingerprintVerify(false);
            fingerprintUnlockSC.setChecked(false);
        }

        gestureUnlockSC.setChecked(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.SET_GESTURE_PASSWORD_REQUEST_CODE && resultCode == CommonConstant.SET_GESTURE_PASSWORD_RESULT_CODE) {
            initData();
        }

        if (requestCode == CommonConstant.DELETE_GESTURE_PASSWORD_REQUEST_CODE && resultCode == CommonConstant.DELETE_GESTURE_PASSWORD_RESULT_CODE) {
            mPresenter.deleteGesturePasswordRequest();
        }

        if (requestCode == CommonConstant.FINGERPRINT_VERIFY_REQUEST_CODE && resultCode == CommonConstant.FINGERPRINT_VERIFY_RESULT_CODE) {
            UserPreferences.getInstance().setIsUseFingerprintVerify(true);
            initData();
        }
    }

}
