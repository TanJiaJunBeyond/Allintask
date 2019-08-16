package com.allintask.lingdao.ui.activity.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.user.IdentifyAuthenticationPresenter;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IIdentifyAuthenticationView;

import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/6/15.
 */

public class IdentifyAuthenticationActivity extends BaseActivity<IIdentifyAuthenticationView, IdentifyAuthenticationPresenter> implements IIdentifyAuthenticationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.et_name)
    EditText nameEt;
    @BindView(R.id.et_identify_card_number)
    EditText identifyCardNumberEt;
    @BindView(R.id.btn_submit_authentication)
    Button submitAuthenticationBtn;

    private boolean mIsZhiMaCreditAuthenticationSuccess;

    private String mZhiMaCreditAuthenticationUrl;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_identify_authentication;
    }

    @Override
    protected IdentifyAuthenticationPresenter CreatePresenter() {
        return new IdentifyAuthenticationPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            mIsZhiMaCreditAuthenticationSuccess = intent.getBooleanExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, false);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.identify_authentication));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        String name = UserPreferences.getInstance().getNickname();

        if (!TextUtils.isEmpty(name)) {
            nameEt.setText(name);
        }

        if (mIsZhiMaCreditAuthenticationSuccess) {
            submitAuthenticationBtn.setText(getString(R.string.already_passed_zhi_ma_authentication));

            nameEt.setFocusable(false);
            identifyCardNumberEt.setFocusable(false);

            submitAuthenticationBtn.setEnabled(true);
            submitAuthenticationBtn.setClickable(true);
        } else {
            submitAuthenticationBtn.setText(getString(R.string.submit_authentication));

            nameEt.setFocusable(true);
            identifyCardNumberEt.setFocusable(true);

            submitAuthenticationBtn.setEnabled(false);
            submitAuthenticationBtn.setClickable(false);
        }

        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSubmitAuthenticationEnable();
            }
        });

        identifyCardNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSubmitAuthenticationEnable();
            }
        });
    }

    private void initData() {
        if (mIsZhiMaCreditAuthenticationSuccess) {
            mPresenter.fetchZhiMaCreditAuthenticationDataRequest();
        }
    }

    private void checkSubmitAuthenticationEnable() {
        String name = nameEt.getText().toString().trim();
        String identifyCardNumber = identifyCardNumberEt.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(identifyCardNumber)) {
            submitAuthenticationBtn.setEnabled(true);
            submitAuthenticationBtn.setClickable(true);
        } else {
            submitAuthenticationBtn.setEnabled(false);
            submitAuthenticationBtn.setClickable(false);
        }
    }

    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            // 这里使用固定appid 20000067
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            // 处理没有安装支付宝的情况
            new AlertDialog.Builder(getParentContext())
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     *
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getParentContext().getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    @OnClick({R.id.btn_submit_authentication})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_authentication:
                if (!mIsZhiMaCreditAuthenticationSuccess) {
                    String name = nameEt.getText().toString().trim();
                    String identifyCardNumber = identifyCardNumberEt.getText().toString().trim();

                    if (identifyCardNumber.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")) {
                        mPresenter.getZhiMaCreditUrlRequest(name, identifyCardNumber);
                    } else {
                        showToast("身份证号码不正确");
                    }
                }
                break;
        }
    }

    @Override
    public void onShowZhiMaCreditAuthenticationData(String realName, String idCardNo) {
        mIsZhiMaCreditAuthenticationSuccess = true;

        nameEt.setText(realName);
        identifyCardNumberEt.setText(idCardNo);
        submitAuthenticationBtn.setText(getString(R.string.already_passed_zhi_ma_authentication));

        nameEt.setFocusable(false);
        identifyCardNumberEt.setFocusable(false);

        submitAuthenticationBtn.setEnabled(true);
        submitAuthenticationBtn.setClickable(true);
    }

    @Override
    public void onGetZhiMaCreditAuthenticationUrlSuccess(String zhiMaCreditAuthenticationUrl) {
        mZhiMaCreditAuthenticationUrl = zhiMaCreditAuthenticationUrl;

        if (!TextUtils.isEmpty(mZhiMaCreditAuthenticationUrl)) {
            doVerify(mZhiMaCreditAuthenticationUrl);
        }
    }

    @Override
    public void onShowIdentifyAuthenticationData(boolean passed) {
        if (passed) {
            mPresenter.fetchZhiMaCreditAuthenticationDataRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String zhiMaCreditAuthenticationType = UserPreferences.getInstance().getZhiMaCreditAuthenticationType();
        String zhiMaCreditAuthenticationParams = UserPreferences.getInstance().getZhiMaCreditAuthenticationParams();
        String zhiMaCreditAuthenticationSign = UserPreferences.getInstance().getZhiMaCreditAuthenticationSign();

        if (!TextUtils.isEmpty(zhiMaCreditAuthenticationType) && !TextUtils.isEmpty(zhiMaCreditAuthenticationParams) && !TextUtils.isEmpty(zhiMaCreditAuthenticationSign)) {
            mPresenter.fetchZhiMaCreditResultRequest(zhiMaCreditAuthenticationSign, zhiMaCreditAuthenticationParams);

            UserPreferences.getInstance().setZhiMaCreditAuthenticationType(null);
            UserPreferences.getInstance().setZhiMaCreditAuthenticationParams(null);
        }
    }

}
