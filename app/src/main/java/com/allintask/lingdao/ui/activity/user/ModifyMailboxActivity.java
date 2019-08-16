package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
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
import com.allintask.lingdao.presenter.user.ModifyMailboxPresenter;
import com.allintask.lingdao.presenter.user.ModifyMobilePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.MailboxUtils;
import com.allintask.lingdao.view.user.IModifyMailboxView;
import com.allintask.lingdao.widget.EditTextWithDelete;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class ModifyMailboxActivity extends BaseActivity<IModifyMailboxView, ModifyMailboxPresenter> implements IModifyMailboxView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_mailbox)
    TextView mailboxTv;
    @BindView(R.id.etwd_mailbox)
    EditTextWithDelete mailboxETWD;
    @BindView(R.id.etwd_mailbox_identify_code)
    EditTextWithDelete mailboxIdentifyCodeETWD;
    @BindView(R.id.btn_get_mailbox_identify_code)
    Button getMailboxIdentifyCodeBtn;
    @BindView(R.id.btn_confirm_modify)
    Button confirmModifyBtn;

    private String mailBox;
    private String newMailbox;
    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_modify_mailbox;
    }

    @Override
    protected ModifyMailboxPresenter CreatePresenter() {
        return new ModifyMailboxPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            mailBox = intent.getStringExtra(CommonConstant.EXTRA_MAILBOX);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.modify_mailbox));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        mailboxTv.setText(mailBox);

        mailboxIdentifyCodeETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmModifyEnable();
            }
        });

        mailboxETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newMailbox = mailboxETWD.getText().toString().trim();
                checkConfirmModifyEnable();
            }
        });

        confirmModifyBtn.setEnabled(false);
        confirmModifyBtn.setClickable(false);
    }

    private void checkConfirmModifyEnable() {
        String mailbox = mailboxETWD.getText().toString().trim();
        String mailboxIdentifyCode = mailboxIdentifyCodeETWD.getText().toString().trim();

        if (!TextUtils.isEmpty(mailbox) && !TextUtils.isEmpty(mailboxIdentifyCode)) {
            confirmModifyBtn.setEnabled(true);
            confirmModifyBtn.setClickable(true);
        } else {
            confirmModifyBtn.setEnabled(false);
            confirmModifyBtn.setClickable(false);
        }
    }

    @OnClick({R.id.btn_get_mailbox_identify_code, R.id.btn_confirm_modify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_mailbox_identify_code:
                if (!TextUtils.isEmpty(newMailbox)) {
                    if (MailboxUtils.isMailbox(newMailbox)) {
                        mPresenter.sendModifyMailboxSmsIdentifyCodeRequest(newMailbox);
                    } else {
                        showToast("邮箱格式不正确");
                    }
                } else {
                    showToast("邮箱不能为空");
                }
                break;

            case R.id.btn_confirm_modify:
                String mailboxIdentifyCode = mailboxIdentifyCodeETWD.getText().toString().trim();

                if (MailboxUtils.isMailbox(newMailbox)) {
                    mPresenter.modifyMailboxRequest(mailboxIdentifyCode, newMailbox);
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
    public void onModifyMailboxSuccess() {
        showToast("修改邮箱成功");
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
