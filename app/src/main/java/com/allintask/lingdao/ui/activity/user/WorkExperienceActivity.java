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
import com.allintask.lingdao.presenter.user.WorkExperiencePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.view.user.IWorkExperienceView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/2/8.
 */

public class WorkExperienceActivity extends BaseActivity<IWorkExperienceView, WorkExperiencePresenter> implements IWorkExperienceView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.et_work_unit)
    EditText workUnitEt;
    @BindView(R.id.et_position)
    EditText positionEt;
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

    private int workExperienceId = -1;

    private Calendar calendar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_work_experience;
    }

    @Override
    protected WorkExperiencePresenter CreatePresenter() {
        return new WorkExperiencePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            workExperienceId = intent.getIntExtra(CommonConstant.EXTRA_WORK_EXPERIENCE_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.work_experience));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        workUnitEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String workUnit = workUnitEt.getText().toString().trim();
                int index = workUnitEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(workUnit)) {
                        Editable editable = workUnitEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                checkSaveEnable();
            }
        });

        positionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String position = positionEt.getText().toString().trim();
                int index = positionEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(position)) {
                        Editable editable = positionEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                checkSaveEnable();
            }
        });
    }

    private void initData() {
        calendar = Calendar.getInstance();

        if (workExperienceId != -1) {
            mPresenter.fetchWorkExperienceRequest(workExperienceId);
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
        String workUnit = workUnitEt.getText().toString().trim();
        String position = positionEt.getText().toString().trim();
        String startTime = startTimeTv.getText().toString().trim();
        String endTime = endTimeTv.getText().toString().trim();

        if (!TextUtils.isEmpty(workUnit) && !TextUtils.isEmpty(position) && !TextUtils.isEmpty(startTime) && !startTime.equals(getString(R.string.please_select_start_time)) && !TextUtils.isEmpty(endTime) && !endTime.equals(getString(R.string.please_select_end_time))) {
            saveBtn.setEnabled(true);
            saveBtn.setClickable(true);
        } else {
            saveBtn.setEnabled(false);
            saveBtn.setClickable(false);
        }
    }

    @OnClick({R.id.ll_start_time, R.id.ll_end_time, R.id.tv_remove, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_start_time:
                showStartTimeDatePickerDialog();
                break;

            case R.id.ll_end_time:
                showEndTimeDataPickerDialog();
                break;

            case R.id.tv_remove:
                if (workExperienceId != -1) {
                    mPresenter.removeWorkExperienceRequest(workExperienceId);
                }
                break;

            case R.id.btn_save:
                String workUnit = workUnitEt.getText().toString().trim();
                String position = positionEt.getText().toString().trim();
                String startTime = startTimeTv.getText().toString().trim();
                String endTime = endTimeTv.getText().toString().trim();

                if (workExperienceId != -1) {
                    mPresenter.compileWorkExperienceRequest(workExperienceId, workUnit, position, startTime, endTime);
                }
                break;
        }
    }

    @Override
    public void onShowWorkUnit(String workUnit) {
        workUnitEt.setText(workUnit);
    }

    @Override
    public void onShowPosition(String position) {
        positionEt.setText(position);
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
    public void onCompileWorkExperienceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onRemoveWorkExperienceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

}
