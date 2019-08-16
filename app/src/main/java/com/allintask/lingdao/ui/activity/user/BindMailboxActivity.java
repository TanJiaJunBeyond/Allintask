package com.allintask.lingdao.ui.activity.user;

import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.BindMailboxPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.MailboxUtils;
import com.allintask.lingdao.view.user.IBindMailboxView;
import com.allintask.lingdao.widget.EditTextWithDelete;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class BindMailboxActivity extends BaseActivity<IBindMailboxView, BindMailboxPresenter> implements IBindMailboxView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.etwd_mailbox)
    EditTextWithDelete mailboxETWD;
    @BindView(R.id.etwd_mailbox_identify_code)
    EditTextWithDelete mailboxIdentifyCodeETWD;
    @BindView(R.id.btn_get_mailbox_identify_code)
    Button getMailboxIdentifyCodeBtn;
    @BindView(R.id.btn_confirm_bind)
    Button confirmBindBtn;

    private String mailbox;
    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bind_mailbox;
    }

    @Override
    protected BindMailboxPresenter CreatePresenter() {
        return new BindMailboxPresenter();
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

        titleTv.setText(getString(R.string.bind_mailbox));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        mailboxETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mailbox = mailboxETWD.getText().toString().trim();
                checkBindMailboxEnable();
            }
        });

        mailboxIdentifyCodeETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBindMailboxEnable();
            }
        });

        confirmBindBtn.setEnabled(false);
        confirmBindBtn.setClickable(false);
    }

    private void checkBindMailboxEnable() {
        String mailboxIdentifyCode = mailboxIdentifyCodeETWD.getText().toString().trim();

        if (!TextUtils.isEmpty(mailbox) && !TextUtils.isEmpty(mailboxIdentifyCode)) {
            confirmBindBtn.setEnabled(true);
            confirmBindBtn.setClickable(true);
        } else {
            confirmBindBtn.setEnabled(false);
            confirmBindBtn.setClickable(false);
        }
    }

    @OnClick({R.id.btn_get_mailbox_identify_code, R.id.btn_confirm_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_mailbox_identify_code:
                if (!TextUtils.isEmpty(mailbox)) {
                    if (MailboxUtils.isMailbox(mailbox)) {
                        mPresenter.sendBindMailboxSmsIdentifyCodeRequest(mailbox);
                    } else {
                        showToast("邮箱格式不正确");
                    }
                } else {
                    showToast("邮箱不能为空");
                }
                break;

            case R.id.btn_confirm_bind:
                String mailboxIdentifyCode = mailboxIdentifyCodeETWD.getText().toString().trim();

                if (MailboxUtils.isMailbox(mailbox)) {
                    mPresenter.bindMailboxRequest(mailboxIdentifyCode, mailbox);
                } else {
                    showToast("邮箱格式不正确");
                }
                break;
        }
    }

    @Override
    public void onCountDownTimerTick(long millisUntilFinished) {
        getMailboxIdentifyCodeBtn.setEnabled(false);
        getMailboxIdentifyCodeBtn.setClickable(false);
        getMailboxIdentifyCodeBtn.setText(String.valueOf(millisUntilFinished / 1000) + "s");
    }

    @Override
    public void onCountDownTimerFinish() {
        getMailboxIdentifyCodeBtn.setEnabled(true);
        getMailboxIdentifyCodeBtn.setClickable(true);
        getMailboxIdentifyCodeBtn.setText("获取");
    }

    @Override
    public void onBindMailboxSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onSendSmsIdentifyCodeSuccess() {
        showToast("发送验证码成功");
        countDownTimer = mPresenter.startCountDownTimer();
    }

    @Override
    protected void onDestroy() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

}
