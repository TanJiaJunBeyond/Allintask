package com.allintask.lingdao.ui.adapter.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.AddEducationalExperienceBean;
import com.allintask.lingdao.bean.user.AddHonorAndQualificationBean;
import com.allintask.lingdao.bean.user.AddWorkExperienceBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.service.PublishServiceAddEducationalExperienceActivity;
import com.allintask.lingdao.ui.activity.service.PublishServiceAddHonorAndQualificationActivity;
import com.allintask.lingdao.ui.activity.service.PublishServiceAddWorkExperienceActivity;
import com.allintask.lingdao.ui.activity.user.AddPersonalInformationActivity;
import com.allintask.lingdao.ui.activity.user.SearchInformationActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.EmojiUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/18.
 */

public class AddPersonalInformationAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_EDUCATION_EXPERIENCE = 0;
    private static final int ITEM_WORK_EXPERIENCE = 1;
    private static final int ITEM_HONOR_AND_QUALIFICATION = 2;
    private static final int ITEM_ADD_PERSONAL_INFORMATION_BOTTOM = 3;

    private Context context;
    private int addPersonalInformationMode;
    private int addPersonalInformationStatus;
    private List<Boolean> isAddPersonalInformationEnableList;
    private Calendar calendar;
    private List<AddEducationalExperienceBean> educationalExperienceList;
    private List<AddWorkExperienceBean> workExperienceList;
    private List<AddHonorAndQualificationBean> honorAndQualificationList;

    private long mStartTime;
    private long mEndTime;

    private boolean isSaveEnable = false;

    private OnClickListener onClickListener;

    public AddPersonalInformationAdapter(Context context, int addPersonalInformationMode, int addPersonalInformationStatus) {
        this.context = context;
        this.addPersonalInformationMode = addPersonalInformationMode;
        this.addPersonalInformationStatus = addPersonalInformationStatus;

        isAddPersonalInformationEnableList = new ArrayList<>();
        calendar = Calendar.getInstance();

        switch (addPersonalInformationStatus) {
            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                educationalExperienceList = new ArrayList<>();
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                workExperienceList = new ArrayList<>();
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                honorAndQualificationList = new ArrayList<>();
                break;
        }
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_EDUCATION_EXPERIENCE:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_educational_experience, parent, false));
                break;

            case ITEM_WORK_EXPERIENCE:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_work_experience, parent, false));
                break;

            case ITEM_HONOR_AND_QUALIFICATION:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_honor_and_qualification, parent, false));
                break;

            case ITEM_ADD_PERSONAL_INFORMATION_BOTTOM:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_personal_information_bottom, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (holder.getItemViewType()) {
            case ITEM_EDUCATION_EXPERIENCE:
                onBindEducationExperienceItemView(holder, position);
                break;

            case ITEM_WORK_EXPERIENCE:
                onBindWorkExperienceItemView(holder, position);
                break;

            case ITEM_HONOR_AND_QUALIFICATION:
                onBindHonorAndQualificationItemView(holder, position);
                break;

            case ITEM_ADD_PERSONAL_INFORMATION_BOTTOM:
                onBindAddPersonalInformationBottomItemView(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (addPersonalInformationStatus) {
            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                if (position == mList.size()) {
                    return ITEM_ADD_PERSONAL_INFORMATION_BOTTOM;
                } else {
                    return ITEM_EDUCATION_EXPERIENCE;
                }

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                if (position == mList.size()) {
                    return ITEM_ADD_PERSONAL_INFORMATION_BOTTOM;
                } else {
                    return ITEM_WORK_EXPERIENCE;
                }

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                if (position == mList.size()) {
                    return ITEM_ADD_PERSONAL_INFORMATION_BOTTOM;
                } else {
                    return ITEM_HONOR_AND_QUALIFICATION;
                }
        }
        return ITEM_ADD_PERSONAL_INFORMATION_BOTTOM;
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    private void onBindEducationExperienceItemView(CommonRecyclerViewHolder holder, final int position) {
        final AddEducationalExperienceBean addEducationalExperienceBean = (AddEducationalExperienceBean) getItem(position);

        if (null != addEducationalExperienceBean) {
            LinearLayout educationalInstitutionLL = holder.getChildView(R.id.ll_educational_institution);
            LinearLayout majorLL = holder.getChildView(R.id.ll_major);
            LinearLayout educationalBackgroundLL = holder.getChildView(R.id.ll_educational_background);
            LinearLayout startTimeLL = holder.getChildView(R.id.ll_start_time);
            LinearLayout endTimeLL = holder.getChildView(R.id.ll_end_time);

            TextView removeTv = holder.getChildView(R.id.tv_remove);

            String educationalInstitution = addEducationalExperienceBean.school;
            String major = addEducationalExperienceBean.major;
            String educationalBackground = addEducationalExperienceBean.educationalBackground;
            String startTimeStr = addEducationalExperienceBean.startAt;
            String endTimeStr = addEducationalExperienceBean.endAt;

            if (educationalInstitution.equals(context.getString(R.string.please_select_your_school))) {
                holder.setTextView(R.id.tv_educational_institution, educationalInstitution, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_educational_institution, educationalInstitution, context.getResources().getColor(R.color.text_dark_black));
            }

            if (major.equals(context.getString(R.string.please_select_major))) {
                holder.setTextView(R.id.tv_major, major, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_major, major, context.getResources().getColor(R.color.text_dark_black));
            }

            if (educationalBackground.equals(context.getString(R.string.please_input_select_education_background))) {
                holder.setTextView(R.id.tv_educational_background, educationalBackground, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_educational_background, educationalBackground, context.getResources().getColor(R.color.text_dark_black));
            }

            if (startTimeStr.equals(context.getString(R.string.please_select_start_time))) {
                holder.setTextView(R.id.tv_start_time, startTimeStr, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_start_time, startTimeStr, context.getResources().getColor(R.color.text_dark_black));
            }

            if (endTimeStr.equals(context.getString(R.string.please_select_end_time))) {
                holder.setTextView(R.id.tv_end_time, endTimeStr, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_end_time, endTimeStr, context.getResources().getColor(R.color.text_dark_black));
            }

            educationalInstitutionLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SearchInformationActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_POSITION, position);
                    intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION);

                    switch (addPersonalInformationMode) {
                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_ADD_PERSONAL_INFORMATION:
                            ((AddPersonalInformationActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_EDUCATIONAL_EXPERIENCE:
                            ((PublishServiceAddEducationalExperienceActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_WORK_EXPERIENCE:
                            ((PublishServiceAddWorkExperienceActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_HONOR_AND_QUALIFICATION:
                            ((PublishServiceAddHonorAndQualificationActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;
                    }
                }
            });

            majorLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SearchInformationActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_POSITION, position);
                    intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR);

                    switch (addPersonalInformationMode) {
                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_ADD_PERSONAL_INFORMATION:
                            ((AddPersonalInformationActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_EDUCATIONAL_EXPERIENCE:
                            ((PublishServiceAddEducationalExperienceActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_WORK_EXPERIENCE:
                            ((PublishServiceAddWorkExperienceActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_HONOR_AND_QUALIFICATION:
                            ((PublishServiceAddHonorAndQualificationActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;
                    }
                }
            });

            educationalBackgroundLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SearchInformationActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_POSITION, position);
                    intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND);

                    switch (addPersonalInformationMode) {
                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_ADD_PERSONAL_INFORMATION:
                            ((AddPersonalInformationActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_EDUCATIONAL_EXPERIENCE:
                            ((PublishServiceAddEducationalExperienceActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_WORK_EXPERIENCE:
                            ((PublishServiceAddWorkExperienceActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;

                        case CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_HONOR_AND_QUALIFICATION:
                            ((PublishServiceAddHonorAndQualificationActivity) context).startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                            break;
                    }
                }
            });

            startTimeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStartTimeDatePickerDialog(position);
                }
            });

            endTimeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStartTime == 0L) {
                        Toast.makeText(context, "请先选择开始时间", Toast.LENGTH_SHORT).show();
                    } else {
                        showEndTimeDataPickerDialog(position);
                    }
                }
            });

            if (mList.size() == 1 && position == 0) {
                removeTv.setVisibility(View.GONE);
            } else {
                removeTv.setVisibility(View.VISIBLE);
            }

            removeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE, position);

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(0, mList.size() + 1);
                }
            });
        }
    }

    private void onBindWorkExperienceItemView(CommonRecyclerViewHolder holder, final int position) {
        final AddWorkExperienceBean addWorkExperienceBean = (AddWorkExperienceBean) getItem(position);

        if (null != addWorkExperienceBean) {
            final EditText workUnitEt = holder.getChildView(R.id.et_work_unit);
            final EditText positionEt = holder.getChildView(R.id.et_position);
            LinearLayout startTimeLL = holder.getChildView(R.id.ll_start_time);
            LinearLayout endTimeLL = holder.getChildView(R.id.ll_end_time);
            TextView removeTv = holder.getChildView(R.id.tv_remove);

            String company = TypeUtils.getString(addWorkExperienceBean.company, "");
            String post = TypeUtils.getString(addWorkExperienceBean.post, "");
            String startAt = TypeUtils.getString(addWorkExperienceBean.startAt, "");
            String endAt = TypeUtils.getString(addWorkExperienceBean.endAt, "");

            if (workUnitEt.getTag() instanceof TextWatcher) {
                workUnitEt.removeTextChangedListener((TextWatcher) workUnitEt.getTag());
            }

            if (positionEt.getTag() instanceof TextWatcher) {
                positionEt.removeTextChangedListener((TextWatcher) positionEt.getTag());
            }

            holder.setTextView(R.id.et_work_unit, company);
            holder.setTextView(R.id.et_position, post);

            if (startAt.equals(context.getString(R.string.please_select_start_time))) {
                holder.setTextView(R.id.tv_start_time, startAt, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_start_time, startAt, context.getResources().getColor(R.color.text_dark_black));
            }

            if (endAt.equals(context.getString(R.string.please_select_end_time))) {
                holder.setTextView(R.id.tv_end_time, endAt, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_end_time, endAt, context.getResources().getColor(R.color.text_dark_black));
            }

            TextWatcher workUnitTextWatcher = new TextWatcher() {
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

                    addWorkExperienceBean.company = workUnit;

                    checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE, position);

                    notifyItemChanged(position);
                    notifyItemChanged(mList.size() + 1);
                }
            };

            workUnitEt.addTextChangedListener(workUnitTextWatcher);
            workUnitEt.setTag(workUnitTextWatcher);
            workUnitEt.setSelection(company.length());

            TextWatcher positionTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String positionStr = positionEt.getText().toString().trim();
                    int index = positionEt.getSelectionStart() - 1;

                    if (index >= 0) {
                        if (EmojiUtils.noEmoji(positionStr)) {
                            Editable editable = positionEt.getText();
                            editable.delete(index, index + 1);
                        }
                    }

                    addWorkExperienceBean.post = positionStr;

                    checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE, position);

                    notifyItemChanged(position);
                    notifyItemChanged(mList.size());
                }
            };

            positionEt.addTextChangedListener(positionTextWatcher);
            positionEt.setTag(positionTextWatcher);
            positionEt.setSelection(post.length());

            startTimeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStartTimeDatePickerDialog(position);
                }
            });

            endTimeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStartTime == 0L) {
                        Toast.makeText(context, "请先选择开始时间", Toast.LENGTH_SHORT).show();
                    } else {
                        showEndTimeDataPickerDialog(position);
                    }
                }
            });

            if (mList.size() == 1 && position == 0) {
                removeTv.setVisibility(View.GONE);
            } else {
                removeTv.setVisibility(View.VISIBLE);
            }

            removeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE, position);

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(0, mList.size() + 1);
                }
            });
        }
    }

    private void onBindHonorAndQualificationItemView(CommonRecyclerViewHolder holder, final int position) {
        final AddHonorAndQualificationBean addHonorAndQualificationBean = (AddHonorAndQualificationBean) getItem(position);

        if (null != addHonorAndQualificationBean) {
            final EditText awardsOrCertificateEt = holder.getChildView(R.id.et_awards_or_certificate);
            final EditText issuingAuthorityEt = holder.getChildView(R.id.et_issuing_authority);
            LinearLayout acquisitionTimeLL = holder.getChildView(R.id.ll_acquisition_time);
            TextView removeTv = holder.getChildView(R.id.tv_remove);

            String prize = TypeUtils.getString(addHonorAndQualificationBean.prize, "");
            String issuingAuthority = TypeUtils.getString(addHonorAndQualificationBean.issuingAuthority, "");
            String gainAt = TypeUtils.getString(addHonorAndQualificationBean.gainAt, "");

            if (awardsOrCertificateEt.getTag() instanceof TextWatcher) {
                awardsOrCertificateEt.removeTextChangedListener((TextWatcher) awardsOrCertificateEt.getTag());
            }

            if (issuingAuthorityEt.getTag() instanceof TextWatcher) {
                issuingAuthorityEt.removeTextChangedListener((TextWatcher) issuingAuthorityEt.getTag());
            }

            holder.setTextView(R.id.et_awards_or_certificate, prize);
            holder.setTextView(R.id.et_issuing_authority, issuingAuthority);

            TextWatcher awardsOrCertificateTextWatcher = new TextWatcher() {
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

                    addHonorAndQualificationBean.prize = awardsOrCertificate;

                    checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION, position);

                    notifyItemChanged(position);
                    notifyItemChanged(mList.size());
                }
            };

            awardsOrCertificateEt.addTextChangedListener(awardsOrCertificateTextWatcher);
            awardsOrCertificateEt.setTag(awardsOrCertificateTextWatcher);
            awardsOrCertificateEt.setSelection(prize.length());

            TextWatcher issuingAuthorityTextWatcher = new TextWatcher() {
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

                    addHonorAndQualificationBean.issuingAuthority = issuingAuthority;

                    checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION, position);

                    notifyItemChanged(position);
                    notifyItemChanged(mList.size());
                }
            };

            issuingAuthorityEt.addTextChangedListener(issuingAuthorityTextWatcher);
            issuingAuthorityEt.setTag(issuingAuthorityTextWatcher);
            issuingAuthorityEt.setSelection(issuingAuthority.length());

            if (gainAt.equals(context.getString(R.string.please_select_acquisition_time))) {
                holder.setTextView(R.id.tv_acquisition_time, gainAt, context.getResources().getColor(R.color.text_dark_gray));
            } else {
                holder.setTextView(R.id.tv_acquisition_time, gainAt, context.getResources().getColor(R.color.text_dark_black));
            }

            acquisitionTimeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStartTimeDatePickerDialog(position);
                }
            });

            if (mList.size() == 1 && position == 0) {
                removeTv.setVisibility(View.GONE);
            } else {
                removeTv.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onBindAddPersonalInformationBottomItemView(CommonRecyclerViewHolder holder, final int position) {
        TextView addTv = holder.getChildView(R.id.tv_add);
        Button saveBtn = holder.getChildView(R.id.btn_save);

        if (position == 10) {
            addTv.setVisibility(View.GONE);
        } else {
            addTv.setVisibility(View.VISIBLE);
        }

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (addPersonalInformationStatus) {
                    case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                        AddEducationalExperienceBean addEducationalExperienceBean = new AddEducationalExperienceBean();
                        addEducationalExperienceBean.school = context.getString(R.string.please_select_your_school);
                        addEducationalExperienceBean.major = context.getString(R.string.please_select_major);
                        addEducationalExperienceBean.educationalBackground = context.getString(R.string.please_input_select_education_background);
                        addEducationalExperienceBean.startAt = context.getString(R.string.please_select_start_time);
                        addEducationalExperienceBean.endAt = context.getString(R.string.please_select_end_time);
                        mList.add(addEducationalExperienceBean);

                        checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE, position);
                        break;

                    case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                        AddWorkExperienceBean addWorkExperienceBean = new AddWorkExperienceBean();
                        addWorkExperienceBean.startAt = context.getString(R.string.please_select_start_time);
                        addWorkExperienceBean.endAt = context.getString(R.string.please_select_end_time);
                        mList.add(addWorkExperienceBean);

                        checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE, position);
                        break;

                    case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                        AddHonorAndQualificationBean addHonorAndQualificationBean = new AddHonorAndQualificationBean();
                        addHonorAndQualificationBean.gainAt = context.getString(R.string.please_select_acquisition_time);
                        mList.add(addHonorAndQualificationBean);

                        checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION, position);
                        break;
                }

                notifyItemInserted(position);
                notifyItemChanged(0);
                notifyItemChanged(mList.size());
            }
        });

        saveBtn.setEnabled(isSaveEnable);
        saveBtn.setClickable(isSaveEnable);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (addPersonalInformationStatus) {
                    case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                        List<AddEducationalExperienceBean> saveEducationalExperienceList = new ArrayList<>();

                        for (int i = 0; i < educationalExperienceList.size(); i++) {
                            AddEducationalExperienceBean addEducationalExperienceBean = educationalExperienceList.get(i);
                            addEducationalExperienceBean.educationalBackground = null;
                            saveEducationalExperienceList.add(addEducationalExperienceBean);
                        }

                        if (null != onClickListener) {
                            onClickListener.onEducationalExperienceSaveButtonClick(v, saveEducationalExperienceList);
                        }
                        break;

                    case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                        if (null != onClickListener) {
                            onClickListener.onWorkExperienceSaveButtonClick(v, workExperienceList);
                        }
                        break;

                    case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                        if (null != onClickListener) {
                            onClickListener.onHonorAndQualificationSaveButtonClick(v, honorAndQualificationList);
                        }
                        break;
                }
            }
        });
    }

    public void checkSaveEnable(int addPersonalInformationStatus, int position) {
        isAddPersonalInformationEnableList.clear();

        switch (addPersonalInformationStatus) {
            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                educationalExperienceList.clear();

                for (int i = 0; i < mList.size(); i++) {
                    AddEducationalExperienceBean addEducationalExperienceBean = (AddEducationalExperienceBean) mList.get(i);

                    String educationalInstitution = addEducationalExperienceBean.school;
                    String major = addEducationalExperienceBean.major;
                    int educationLevelId = addEducationalExperienceBean.educationLevelId;
                    String educationBackground = addEducationalExperienceBean.educationalBackground;
                    String startTime = addEducationalExperienceBean.startAt;
                    String endTime = addEducationalExperienceBean.endAt;

                    if (!TextUtils.isEmpty(educationalInstitution) && !educationalInstitution.equals(context.getString(R.string.please_select_your_school)) && !TextUtils.isEmpty(major) && !major.equals(context.getString(R.string.please_select_major)) && educationLevelId != -1 && !TextUtils.isEmpty(educationBackground) && !educationBackground.equals(context.getString(R.string.please_input_select_education_background)) && !TextUtils.isEmpty(startTime) && !startTime.equals(context.getString(R.string.please_select_start_time)) && !TextUtils.isEmpty(endTime) && !endTime.equals(context.getString(R.string.please_select_end_time))) {
                        isAddPersonalInformationEnableList.add(true);
                        educationalExperienceList.add(addEducationalExperienceBean);
                    } else {
                        isAddPersonalInformationEnableList.add(false);
                    }
                }
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                workExperienceList.clear();

                for (int i = 0; i < mList.size(); i++) {
                    AddWorkExperienceBean addWorkExperienceBean = (AddWorkExperienceBean) mList.get(i);

                    String company = addWorkExperienceBean.company;
                    String post = addWorkExperienceBean.post;
                    String startAt = addWorkExperienceBean.startAt;
                    String endAt = addWorkExperienceBean.endAt;

                    if (!TextUtils.isEmpty(company) && !TextUtils.isEmpty(post) && !TextUtils.isEmpty(startAt) && !startAt.equals(context.getString(R.string.please_select_start_time)) && !TextUtils.isEmpty(endAt) && !endAt.equals(context.getString(R.string.please_select_end_time))) {
                        isAddPersonalInformationEnableList.add(true);
                        workExperienceList.add(addWorkExperienceBean);
                    } else {
                        isAddPersonalInformationEnableList.add(false);
                    }
                }
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                honorAndQualificationList.clear();

                for (int i = 0; i < mList.size(); i++) {
                    AddHonorAndQualificationBean addHonorAndQualificationBean = (AddHonorAndQualificationBean) mList.get(i);

                    String prize = addHonorAndQualificationBean.prize;
                    String issuingAuthority = addHonorAndQualificationBean.issuingAuthority;
                    String gainAt = addHonorAndQualificationBean.gainAt;

                    if (!TextUtils.isEmpty(prize) && !TextUtils.isEmpty(issuingAuthority) && !TextUtils.isEmpty(gainAt) && !gainAt.equals(context.getString(R.string.please_select_acquisition_time))) {
                        isAddPersonalInformationEnableList.add(true);
                        honorAndQualificationList.add(addHonorAndQualificationBean);
                    } else {
                        isAddPersonalInformationEnableList.add(false);
                    }
                }
                break;
        }

        for (int i = 0; i < isAddPersonalInformationEnableList.size(); i++) {
            boolean isAddPersonInformationEnable = isAddPersonalInformationEnableList.get(i);

            if (!isAddPersonInformationEnable) {
                isSaveEnable = false;
                break;
            } else {
                isSaveEnable = true;
            }
        }
    }

    private void showStartTimeDatePickerDialog(final int position) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
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

                    startDate = CommonConstant.commonDateFormat.parse(startTimeStr);
                    long startTime = startDate.getTime();

                    long currentTimeMillis = System.currentTimeMillis();

                    if (startTime <= currentTimeMillis) {
                        if (mStartTime == 0L || mEndTime == 0L) {
                            mStartTime = startTime;

                            switch (addPersonalInformationStatus) {
                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                                    AddEducationalExperienceBean addEducationalExperienceBean = (AddEducationalExperienceBean) getItem(position);
                                    addEducationalExperienceBean.startAt = startTimeStr;
                                    break;

                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                                    AddWorkExperienceBean addWorkExperienceBean = (AddWorkExperienceBean) getItem(position);
                                    addWorkExperienceBean.startAt = startTimeStr;
                                    break;

                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                                    AddHonorAndQualificationBean addHonorAndQualificationBean = (AddHonorAndQualificationBean) getItem(position);
                                    addHonorAndQualificationBean.gainAt = startTimeStr;
                                    break;
                            }

                            checkSaveEnable(addPersonalInformationStatus, position);

                            notifyItemChanged(position);
                            notifyItemChanged(mList.size());
                        } else if (mEndTime >= startTime) {
                            mStartTime = startTime;

                            switch (addPersonalInformationStatus) {
                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                                    AddEducationalExperienceBean addEducationalExperienceBean = (AddEducationalExperienceBean) getItem(position);
                                    addEducationalExperienceBean.startAt = startTimeStr;
                                    break;

                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                                    AddWorkExperienceBean addWorkExperienceBean = (AddWorkExperienceBean) getItem(position);
                                    addWorkExperienceBean.startAt = startTimeStr;
                                    break;

                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                                    AddHonorAndQualificationBean addHonorAndQualificationBean = (AddHonorAndQualificationBean) getItem(position);
                                    addHonorAndQualificationBean.gainAt = startTimeStr;
                                    break;
                            }

                            checkSaveEnable(addPersonalInformationStatus, position);

                            notifyItemChanged(position);
                            notifyItemChanged(mList.size());
                        } else {
                            Toast.makeText(context, "结束时间必须大于等于开始时间", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (addPersonalInformationStatus == CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION) {
                            Toast.makeText(context, "获取时间不能大于今天", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "开始时间不能大于今天", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    private void showEndTimeDataPickerDialog(final int position) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
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
                        if (endTime >= mStartTime) {
                            mEndTime = endTime;

                            switch (addPersonalInformationStatus) {
                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                                    AddEducationalExperienceBean addEducationalExperienceBean = (AddEducationalExperienceBean) getItem(position);
                                    addEducationalExperienceBean.endAt = endTimeStr;
                                    break;

                                case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                                    AddWorkExperienceBean addWorkExperienceBean = (AddWorkExperienceBean) getItem(position);
                                    addWorkExperienceBean.endAt = endTimeStr;
                                    break;
                            }

                            checkSaveEnable(addPersonalInformationStatus, position);

                            notifyItemChanged(position);
                            notifyItemChanged(mList.size());
                        } else {
                            Toast.makeText(context, "结束时间必须大于等于开始时间", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "结束时间不能大于今天", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    public interface OnClickListener {

        void onEducationalExperienceSaveButtonClick(View view, List<AddEducationalExperienceBean> educationalExperienceList);

        void onWorkExperienceSaveButtonClick(View view, List<AddWorkExperienceBean> workExperienceList);

        void onHonorAndQualificationSaveButtonClick(View view, List<AddHonorAndQualificationBean> honorAndQualificationList);

    }

    public void setOnCLickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
