package com.allintask.lingdao.ui.activity.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.HonorAndQualificationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.view.user.IHonorAndQualificationView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/2/8.
 */

public class HonorAndQualificationActivity extends BaseActivity<IHonorAndQualificationView, HonorAndQualificationPresenter> implements IHonorAndQualificationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.et_awards_or_certificate)
    EditText awardsOrCertificateEt;
    @BindView(R.id.et_issuing_authority)
    EditText issuingAuthorityEt;
    @BindView(R.id.ll_acquisition_time)
    LinearLayout acquisitionTimeLL;
    @BindView(R.id.tv_acquisition_time)
    TextView acquisitionTimeTv;
    @BindView(R.id.btn_save)
    Button saveBtn;

    private int honorAndQualificationId = -1;

    private Calendar calendar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_honor_and_qualification;
    }

    @Override
    protected HonorAndQualificationPresenter CreatePresenter() {
        return new HonorAndQualificationPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            honorAndQualificationId = intent.getIntExtra(CommonConstant.EXTRA_HONOR_AND_QUALIFICATION_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.honor_and_qualification));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        awardsOrCertificateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String awardsOrCertificate = awardsOrCertificateEt.getText().toString().trim();
                int index = awardsOrCertificateEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(awardsOrCertificate)) {
                        Editable editable = awardsOrCertificateEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                checkSaveEnable();
            }
        });

        issuingAuthorityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String issuingAuthority = issuingAuthorityEt.getText().toString().trim();
                int index = issuingAuthorityEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(issuingAuthority)) {
                        Editable editable = issuingAuthorityEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                checkSaveEnable();
            }
        });
    }

    private void initData() {
        calendar = Calendar.getInstance();

        if (honorAndQualificationId != -1) {
            mPresenter.fetchHonorAndQualificationRequest(honorAndQualificationId);
        }
    }

    private void showAcquisitionTimeDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getParentContext(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String yearStr = String.valueOf(year);
                    String monthStr = String.valueOf(month + 1);
                    String dateStr = String.valueOf(dayOfMonth);

                    if (monthStr.length() == 1) {
                        monthStr = "0" + monthStr;
                    }

                    if (dateStr.length() == 1) {
                        dateStr = "0" + dateStr;
                    }

                    String acquisitionTimeStr = yearStr + "-" + monthStr + "-" + dateStr;
                    Date acquisitionTimeDate = CommonConstant.commonDateFormat.parse(acquisitionTimeStr);
                    long endTime = acquisitionTimeDate.getTime();

                    long currentTimeMillis = System.currentTimeMillis();

                    if (endTime <= currentTimeMillis) {
                        acquisitionTimeTv.setText(acquisitionTimeStr);
                    } else {
                        showToast("获取时间不能大于今天");
                    }

                    checkSaveEnable();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    private void checkSaveEnable() {
        String awardsOrCertificate = awardsOrCertificateEt.getText().toString().trim();
        String issuingAuthority = issuingAuthorityEt.getText().toString().trim();
        String acquisitionTime = acquisitionTimeTv.getText().toString().trim();

        if (!TextUtils.isEmpty(awardsOrCertificate) && !TextUtils.isEmpty(issuingAuthority) && !TextUtils.isEmpty(acquisitionTime) && !acquisitionTime.equals(getString(R.string.please_select_acquisition_time))) {
            saveBtn.setEnabled(true);
            saveBtn.setClickable(true);
        } else {
            saveBtn.setEnabled(false);
            saveBtn.setClickable(false);
        }
    }

    @OnClick({R.id.ll_acquisition_time, R.id.tv_remove, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_acquisition_time:
                showAcquisitionTimeDatePickerDialog();
                break;

            case R.id.tv_remove:
                if (honorAndQualificationId != -1) {
                    mPresenter.removeHonorAndQualificationRequest(honorAndQualificationId);
                }
                break;

            case R.id.btn_save:
                String awardsOrCertificate = awardsOrCertificateEt.getText().toString().trim();
                String issuingAuthority = issuingAuthorityEt.getText().toString().trim();
                String acquisitionTime = acquisitionTimeTv.getText().toString().trim();

                if (honorAndQualificationId != -1) {
                    mPresenter.compileHonorAndQualificationRequest(honorAndQualificationId, awardsOrCertificate, issuingAuthority, acquisitionTime);
                }
                break;
        }
    }

    @Override
    public void onShowAwardsOrCertificate(String awardsOrCertificate) {
        awardsOrCertificateEt.setText(awardsOrCertificate);
        checkSaveEnable();
    }

    @Override
    public void onShowIssuingAuthority(String issuingAuthority) {
        issuingAuthorityEt.setText(issuingAuthority);
        checkSaveEnable();
    }

    @Override
    public void onShowAcquisitionTime(String acquisitionTime) {
        acquisitionTimeTv.setText(acquisitionTime);
        checkSaveEnable();
    }

    @Override
    public void onCompileHonorAndQualificationSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onRemoveHonorAndQualificationSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

}
