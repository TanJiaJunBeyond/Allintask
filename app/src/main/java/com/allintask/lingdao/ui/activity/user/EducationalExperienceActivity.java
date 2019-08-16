package com.allintask.lingdao.ui.activity.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.EducationalExperiencePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.IEducationalExperienceView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class EducationalExperienceActivity extends BaseActivity<IEducationalExperienceView, EducationalExperiencePresenter> implements IEducationalExperienceView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_educational_institution)
    LinearLayout educationalInstitutionLL;
    @BindView(R.id.tv_educational_institution)
    TextView educationalInstitutionTv;
    @BindView(R.id.ll_major)
    LinearLayout majorLL;
    @BindView(R.id.tv_major)
    TextView majorTv;
    @BindView(R.id.ll_educational_background)
    LinearLayout educationalBackgroundLL;
    @BindView(R.id.tv_educational_background)
    TextView educationalBackgroundTv;
    @BindView(R.id.ll_start_time)
    LinearLayout startTimeLL;
    @BindView(R.id.tv_start_time)
    TextView startTimeTv;
    @BindView(R.id.ll_end_time)
    LinearLayout endTimeLL;
    @BindView(R.id.tv_end_time)
    TextView endTimeTv;
    @BindView(R.id.tv_remove)
    TextView removeTv;
    @BindView(R.id.btn_save)
    Button saveBtn;

    private int educationalExperienceId = -1;

    private Calendar calendar;
    private int educationalBackgroundId = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_educational_experience;
    }

    @Override
    protected EducationalExperiencePresenter CreatePresenter() {
        return new EducationalExperiencePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            educationalExperienceId = intent.getIntExtra(CommonConstant.EXTRA_EDUCATIONAL_EXPERIENCE_ID, -1);
        }

        initToolbar();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.education_experience));

        setSupportActionBar(toolbar);
    }

    private void initData() {
        calendar = Calendar.getInstance();

        if (educationalExperienceId != -1) {
            mPresenter.fetchEducationalExperienceRequest(educationalExperienceId);
        }
    }

    private void showStartTimeDatePickerDialog() {
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

                    String startTimeStr = yearStr + "-" + monthStr + "-" + dateStr;
                    Date startDate;

                    String endTimeStr = endTimeTv.getText().toString().trim();
                    Date endDate = CommonConstant.commonDateFormat.parse(endTimeStr);
                    long endTime = endDate.getTime();

                    startDate = CommonConstant.commonDateFormat.parse(startTimeStr);
                    long startTime = startDate.getTime();

                    long currentTimeMillis = System.currentTimeMillis();

                    if (startTime <= currentTimeMillis) {
                        if (startTime == 0L) {
                            startTimeTv.setText(startTimeStr);
                        } else if (endTime >= startTime) {
                            startTimeTv.setText(startTimeStr);
                        } else {
                            showToast("结束时间必须大于等于开始时间");
                        }
                    } else {
                        showToast("开始时间不能大于今天");
                    }

                    checkSaveEnable();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    private void showEndTimeDataPickerDialog() {
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

                    String endTimeStr = yearStr + "-" + monthStr + "-" + dateStr;
                    Date endDate = CommonConstant.commonDateFormat.parse(endTimeStr);
                    long endTime = endDate.getTime();

                    long currentTimeMillis = System.currentTimeMillis();

                    if (endTime <= currentTimeMillis) {
                        String startTimeStr = startTimeTv.getText().toString().trim();
                        Date startDate = CommonConstant.commonDateFormat.parse(startTimeStr);
                        long startTime = startDate.getTime();

                        if (endTime >= startTime) {
                            endTimeTv.setText(endTimeStr);
                        } else {
                            showToast("结束时间必须大于等于开始时间");
                        }
                    } else {
                        showToast("结束时间不能大于今天");
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
        String educationalInstitution = educationalInstitutionTv.getText().toString().trim();
        String major = majorTv.getText().toString().trim();
        String educationBackground = educationalBackgroundTv.getText().toString().trim();
        String startTime = startTimeTv.getText().toString().trim();
        String endTime = endTimeTv.getText().toString().trim();

        if (!TextUtils.isEmpty(educationalInstitution) && !educationalInstitution.equals(getString(R.string.please_select_your_school)) && !TextUtils.isEmpty(major) && !major.equals(getString(R.string.please_select_major)) && educationalBackgroundId != -1 && !TextUtils.isEmpty(educationBackground) && !educationBackground.equals(getString(R.string.please_input_select_education_background)) && !TextUtils.isEmpty(startTime) && !startTime.equals(getString(R.string.please_select_start_time)) && !TextUtils.isEmpty(endTime) && !endTime.equals(getString(R.string.please_select_end_time))) {
            saveBtn.setEnabled(true);
            saveBtn.setClickable(true);
        } else {
            saveBtn.setEnabled(false);
            saveBtn.setClickable(false);
        }
    }

    @OnClick({R.id.ll_educational_institution, R.id.ll_major, R.id.ll_educational_background, R.id.ll_start_time, R.id.ll_end_time, R.id.tv_remove, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_educational_institution:
                Intent educationalInstitutionIntent = new Intent(getParentContext(), SearchInformationActivity.class);
                educationalInstitutionIntent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION);
                startActivityForResult(educationalInstitutionIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.ll_major:
                Intent majorIntent = new Intent(getParentContext(), SearchInformationActivity.class);
                majorIntent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR);
                startActivityForResult(majorIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.ll_educational_background:
                Intent educationalBackgroundIntent = new Intent(getParentContext(), SearchInformationActivity.class);
                educationalBackgroundIntent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND);
                startActivityForResult(educationalBackgroundIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.ll_start_time:
                showStartTimeDatePickerDialog();
                break;

            case R.id.ll_end_time:
                showEndTimeDataPickerDialog();
                break;

            case R.id.tv_remove:
                if (educationalExperienceId != -1) {
                    mPresenter.removeEducationExperienceRequest(educationalExperienceId);
                }
                break;

            case R.id.btn_save:
                if (educationalExperienceId != -1) {
                    String educationalInstitution = educationalInstitutionTv.getText().toString().trim();
                    String major = majorTv.getText().toString().trim();
                    String startTime = startTimeTv.getText().toString().trim();
                    String endTime = endTimeTv.getText().toString().trim();

                    mPresenter.compileEducationExperienceRequest(educationalExperienceId, educationalInstitution, major, educationalBackgroundId, startTime, endTime);
                }
                break;
        }
    }

    @Override
    public void onShowEducationalInstitution(String educationalInstitution) {
        educationalInstitutionTv.setText(educationalInstitution);
        checkSaveEnable();
    }

    @Override
    public void onShowMajor(String major) {
        majorTv.setText(major);
        checkSaveEnable();
    }

    @Override
    public void onShowEducationalBackgroundId(int educationalBackgroundId) {
        this.educationalBackgroundId = educationalBackgroundId;
        mPresenter.fetchEducationalBackgroundListRequest(educationalBackgroundId);
        checkSaveEnable();
    }

    @Override
    public void onShowEducationalBackground(String educationalBackground) {
        educationalBackgroundTv.setText(educationalBackground);
        checkSaveEnable();
    }

    @Override
    public void onShowStartTime(String startTime) {
        startTimeTv.setText(startTime);
        checkSaveEnable();
    }

    @Override
    public void onShowEndTime(String endTime) {
        endTimeTv.setText(endTime);
        checkSaveEnable();
    }

    @Override
    public void onCompileEducationExperienceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onRemoveEducationExperienceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE && null != data) {
            int searchInformationType = data.getIntExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION);
            String searchInformation = data.getStringExtra(CommonConstant.EXTRA_SEARCH_INFORMATION);
            int educationalBackgroundId = data.getIntExtra(CommonConstant.EXTRA_EDUCATIONAL_BACKGROUND_ID, 0);

            switch (searchInformationType) {
                case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION:
                    educationalInstitutionTv.setText(searchInformation);
                    break;

                case CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR:
                    majorTv.setText(searchInformation);
                    break;

                case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND:
                    this.educationalBackgroundId = educationalBackgroundId;
                    educationalBackgroundTv.setText(searchInformation);
                    break;
            }

            checkSaveEnable();
        }
    }

}
