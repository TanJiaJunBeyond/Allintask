package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.FingerprintVerifyPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.IFingerprintVerifyView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/5/28.
 */

public class FingerprintVerifyActivity extends BaseActivity<IFingerprintVerifyView, FingerprintVerifyPresenter> implements IFingerprintVerifyView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.btn_click_to_verify_fingerprint)
    Button clickToVerifyFingerprintBtn;
    @BindView(R.id.tv_payment_password_unlock)
    TextView paymentPasswordUnlockTv;

    private FingerprintManagerCompat fingerprintManagerCompat;
    private CancellationSignal cancellationSignal;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fingerprint_verify;
    }

    @Override
    protected FingerprintVerifyPresenter CreatePresenter() {
        return new FingerprintVerifyPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.fingerprint_verify));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        fingerprintManagerCompat = FingerprintManagerCompat.from(getParentContext());
        cancellationSignal = new CancellationSignal();
    }

    @OnClick({R.id.btn_click_to_verify_fingerprint, R.id.tv_payment_password_unlock})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click_to_verify_fingerprint:
                clickToVerifyFingerprintBtn.setText("正在验证");
                fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        Intent myAccountIntent = new Intent(getParentContext(), MyAccountActivity.class);
                        startActivity(myAccountIntent);

                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        showToast("指纹验证失败");
                        clickToVerifyFingerprintBtn.setText(getString(R.string.click_to_verify_fingerprint));
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        super.onAuthenticationHelp(helpMsgId, helpString);
                        showToast(helpString);
                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);

                        if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_TIMEOUT) {
                            showToast(errString);
                            clickToVerifyFingerprintBtn.setText(getString(R.string.click_to_verify_fingerprint));
                        }
                    }
                }, null);
                break;

            case R.id.tv_payment_password_unlock:
                Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_PAYMENT_PASSWORD_UNLOCK);
                startActivity(intent);

                finish();
                break;
        }
    }

}
