package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ReportPresenter;
import com.allintask.lingdao.presenter.user.ReportReasonActivity;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.view.user.IReportView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public class ReportActivity extends BaseActivity<IReportView, ReportPresenter> implements IReportView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_report_reason)
    LinearLayout reportReasonLL;
    @BindView(R.id.tv_report_reason)
    TextView reportReasonTv;
    @BindView(R.id.et_supplementary_instruction)
    EditText supplementaryInstructionET;
    @BindView(R.id.tv_number_of_words)
    TextView numberOfWordsTv;
    @BindView(R.id.btn_confirm_report)
    Button confirmReportBtn;

    private int userId;

    private String reportReasonIdListString;
    private String reportReasonString;
    private String supplementaryInstruction;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_report;
    }

    @Override
    protected ReportPresenter CreatePresenter() {
        return new ReportPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.report));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        supplementaryInstructionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                supplementaryInstruction = supplementaryInstructionET.getText().toString().trim();
                int index = supplementaryInstructionET.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(supplementaryInstruction)) {
                        Editable editable = supplementaryInstructionET.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int supplementaryInstructionLength = supplementaryInstruction.length();
                numberOfWordsTv.setText(String.valueOf(supplementaryInstructionLength) + "/300");

                checkConfirmReportEnable();
            }
        });

        confirmReportBtn.setEnabled(false);
        confirmReportBtn.setClickable(false);
    }

    private void checkConfirmReportEnable() {
        if (!TextUtils.isEmpty(reportReasonIdListString) && !TextUtils.isEmpty(supplementaryInstruction)) {
            confirmReportBtn.setEnabled(true);
            confirmReportBtn.setClickable(true);
        } else {
            confirmReportBtn.setEnabled(false);
            confirmReportBtn.setClickable(false);
        }
    }

    @OnClick({R.id.ll_report_reason, R.id.btn_confirm_report})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_report_reason:
                Intent intent = new Intent(getParentContext(), ReportReasonActivity.class);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.btn_confirm_report:
                if (userId != -1) {
                    mPresenter.saveReportRequest(userId, reportReasonIdListString, supplementaryInstruction);
                }
                break;
        }
    }

    @Override
    public void onReportSuccess() {
        showToast("举报成功");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE) {
            reportReasonIdListString = data.getStringExtra(CommonConstant.EXTRA_REPORT_REASON_ID_LIST_STRING);
            reportReasonString = data.getStringExtra(CommonConstant.EXTRA_REPORT_REASON_STRING);

            if (!TextUtils.isEmpty(reportReasonString)) {
                reportReasonTv.setText(reportReasonString);
            } else {
                reportReasonTv.setText(getString(R.string.please_select));
            }
        }
    }

}
