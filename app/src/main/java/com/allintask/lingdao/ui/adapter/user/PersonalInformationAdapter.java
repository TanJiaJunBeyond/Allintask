package com.allintask.lingdao.ui.adapter.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.SelectGenderDialog;
import com.allintask.lingdao.widget.swipe.HorizontalSlidingView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.custom.dialog.BasicDialog;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/18.
 */

public class PersonalInformationAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_PERSONAL_INFORMATION_HEADER = 0;
    private static final int ITEM_PERSONAL_INFORMATION = 1;
    private static final int ITEM_ADD_PERSONAL_INFORMATION = 2;

    private Context context;

    private List<PersonalInformationBean.EducationalExperienceBean> educationalExperienceList;
    private List<PersonalInformationBean.WorkExperienceBean> workExperienceList;
    private List<PersonalInformationBean.HonorAndQualificationBean> honorAndQualificationList;

    private Calendar calendar;

    private String userHeadPortraitUrl;
    private String name;
    private int age;
    private int gender;
    private String startWorkTime;

    private SelectGenderDialog selectGenderDialog;

    private OnConfirmListener onConfirmListener;

    public PersonalInformationAdapter(Context context) {
        this.context = context;

        educationalExperienceList = new ArrayList<>();
        workExperienceList = new ArrayList<>();
        honorAndQualificationList = new ArrayList<>();

        calendar = Calendar.getInstance();
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_PERSONAL_INFORMATION_HEADER:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_personal_information_header, parent, false));
                break;

            case ITEM_PERSONAL_INFORMATION:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_personal_information, parent, false));
                break;

            case ITEM_ADD_PERSONAL_INFORMATION:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_personal_information, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (holder.getItemViewType()) {
            case ITEM_PERSONAL_INFORMATION_HEADER:
                onBindPersonalInformationHeaderItemView(holder);
                break;

            case ITEM_PERSONAL_INFORMATION:
                onBindPersonalInformationItemView(holder, position);
                break;

            case ITEM_ADD_PERSONAL_INFORMATION:
                onBindAddPersonalInformationItemView(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_PERSONAL_INFORMATION_HEADER;
        } else if (position > 0 && position < educationalExperienceList.size() + 1) {
            return ITEM_PERSONAL_INFORMATION;
        } else if (position == educationalExperienceList.size() + 1) {
            return ITEM_ADD_PERSONAL_INFORMATION;
        } else if (position > educationalExperienceList.size() + 1 && position < educationalExperienceList.size() + workExperienceList.size() + 2) {
            return ITEM_PERSONAL_INFORMATION;
        } else if (position == educationalExperienceList.size() + workExperienceList.size() + 2) {
            return ITEM_ADD_PERSONAL_INFORMATION;
        } else if (position > educationalExperienceList.size() + workExperienceList.size() + 2 && position < educationalExperienceList.size() + workExperienceList.size() + honorAndQualificationList.size() + 3) {
            return ITEM_PERSONAL_INFORMATION;
        } else {
            return ITEM_ADD_PERSONAL_INFORMATION;
        }
    }

    @Override
    public int getItemCount() {
        return educationalExperienceList.size() + workExperienceList.size() + honorAndQualificationList.size() + 4;
    }

    private void onBindPersonalInformationHeaderItemView(CommonRecyclerViewHolder holder) {
        RelativeLayout headPortraitRL = holder.getChildView(R.id.rl_head_portrait);
        CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
        LinearLayout nameLL = holder.getChildView(R.id.ll_name);
        LinearLayout ageLL = holder.getChildView(R.id.ll_age);
        RelativeLayout genderRL = holder.getChildView(R.id.rl_gender);
        TextView genderTv = holder.getChildView(R.id.tv_gender);
        LinearLayout startWorkTimeLL = holder.getChildView(R.id.ll_start_work_time);

        String imageUrl = null;

        if (!TextUtils.isEmpty(userHeadPortraitUrl)) {
            imageUrl = "https:" + userHeadPortraitUrl;
        }

        ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

        switch (gender) {
            case CommonConstant.MALE:
                genderTv.setText("男");
                break;

            case CommonConstant.FEMALE:
                genderTv.setText("女");
                break;
        }

        holder.setTextView(R.id.tv_name, name);
        holder.setTextView(R.id.tv_age, String.valueOf(age) + "岁");

        if (!TextUtils.isEmpty(startWorkTime)) {
            holder.setTextView(R.id.tv_start_work_time, startWorkTime);
        } else {
            holder.setTextView(R.id.tv_start_work_time, context.getString(R.string.please_select));
        }

        headPortraitRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmListener) {
                    onConfirmListener.onSetOrModifyUserHeadPortrait();
                }
            }
        });

        nameLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmListener) {
                    onConfirmListener.onSetOrModifyName();
                }
            }
        });

        ageLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBirthdayDialog();
            }
        });

        genderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });

        startWorkTimeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartWorkTimeDialog();
            }
        });
    }

    private void onBindPersonalInformationItemView(CommonRecyclerViewHolder holder, int position) {
        PersonalInformationBean.EducationalExperienceBean educationalExperienceBean = null;
        PersonalInformationBean.WorkExperienceBean workExperienceBean = null;
        PersonalInformationBean.HonorAndQualificationBean honorAndQualificationBean = null;

        if (null != educationalExperienceList && educationalExperienceList.size() > 0 && position > 0 && position < educationalExperienceList.size() + 1) {
            educationalExperienceBean = educationalExperienceList.get(position - 1);
        }

        if (null != workExperienceList && workExperienceList.size() > 0 && position > educationalExperienceList.size() + 1 && position < educationalExperienceList.size() + workExperienceList.size() + 2) {
            workExperienceBean = workExperienceList.get(position - educationalExperienceList.size() - 2);
        }

        if (null != honorAndQualificationList && honorAndQualificationList.size() > 0 && position > educationalExperienceList.size() + workExperienceList.size() + 2 && position < educationalExperienceList.size() + workExperienceList.size() + honorAndQualificationList.size() + 3) {
            honorAndQualificationBean = honorAndQualificationList.get(position - educationalExperienceList.size() - workExperienceList.size() - 3);
        }

        HorizontalSlidingView horizontalSlidingView = holder.getChildView(R.id.horizontal_sliding_view);
        LinearLayout itemLL = holder.getChildView(R.id.ll_item);

        horizontalSlidingView.closeMenu();
        horizontalSlidingView.setSlidingButtonListener(new HorizontalSlidingView.HorizontalSlidingViewListener() {
            @Override
            public void onMenuIsOpen(View view) {

            }

            @Override
            public void onDownOrMove(HorizontalSlidingView horizontalSlidingView) {

            }
        });

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemLL.getLayoutParams();
        layoutParams.width = WindowUtils.getScreenWidth(context);
        itemLL.setLayoutParams(layoutParams);

        if (null != educationalExperienceBean) {
            TextView dateTv = holder.getChildView(R.id.tv_date);
            TextView deleteTv = holder.getChildView(R.id.tv_delete);

            final int id = TypeUtils.getInteger(educationalExperienceBean.id, -1);
            String school = TypeUtils.getString(educationalExperienceBean.school, "");
            String major = TypeUtils.getString(educationalExperienceBean.major, "");
            Date startAt = educationalExperienceBean.startAt;
            Date endAt = educationalExperienceBean.endAt;

            holder.setTextView(R.id.tv_title, school);
            holder.setTextView(R.id.tv_subtitle, major);

            if (null != startAt && null != endAt) {
                String startTime = CommonConstant.commonDateFormat.format(startAt);
                String endTime = CommonConstant.commonDateFormat.format(endAt);
                holder.setTextView(R.id.tv_date, startTime + "~" + endTime);

                dateTv.setVisibility(View.VISIBLE);
            } else {
                dateTv.setVisibility(View.GONE);
            }

            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onConfirmListener) {
                        onConfirmListener.onDeletePersonalInformation(CommonConstant.EXTRA_EDUCATIONAL_EXPERIENCE_ID, id);
                    }
                }
            });

            itemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onConfirmListener) {
                        onConfirmListener.onModifyPersonalInformation(CommonConstant.EXTRA_EDUCATIONAL_EXPERIENCE_ID, id);
                    }
                }
            });
        }

        if (null != workExperienceBean) {
            TextView dateTv = holder.getChildView(R.id.tv_date);
            TextView deleteTv = holder.getChildView(R.id.tv_delete);

            final int id = TypeUtils.getInteger(workExperienceBean.id, -1);
            String company = TypeUtils.getString(workExperienceBean.company, "");
            String post = TypeUtils.getString(workExperienceBean.post, "");
            Date startAt = workExperienceBean.startAt;
            Date endAt = workExperienceBean.endAt;

            holder.setTextView(R.id.tv_title, company);
            holder.setTextView(R.id.tv_subtitle, post);

            if (null != startAt && null != endAt) {
                String startTime = CommonConstant.commonDateFormat.format(startAt);
                String endTime = CommonConstant.commonDateFormat.format(endAt);
                holder.setTextView(R.id.tv_date, startTime + "~" + endTime);

                dateTv.setVisibility(View.VISIBLE);
            } else {
                dateTv.setVisibility(View.GONE);
            }

            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onConfirmListener) {
                        onConfirmListener.onDeletePersonalInformation(CommonConstant.EXTRA_WORK_EXPERIENCE_ID, id);
                    }
                }
            });

            itemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onConfirmListener) {
                        onConfirmListener.onModifyPersonalInformation(CommonConstant.EXTRA_WORK_EXPERIENCE_ID, id);
                    }
                }
            });
        }

        if (null != honorAndQualificationBean) {
            TextView dateTv = holder.getChildView(R.id.tv_date);
            TextView deleteTv = holder.getChildView(R.id.tv_delete);

            final int id = TypeUtils.getInteger(honorAndQualificationBean.id, -1);
            String issuingAuthority = TypeUtils.getString(honorAndQualificationBean.issuingAuthority, "");
            String prize = TypeUtils.getString(honorAndQualificationBean.prize, "");
            Date gainAt = honorAndQualificationBean.gainAt;

            holder.setTextView(R.id.tv_title, issuingAuthority);
            holder.setTextView(R.id.tv_subtitle, prize);

            if (null != gainAt) {
                String gainTime = CommonConstant.commonDateFormat.format(gainAt);
                holder.setTextView(R.id.tv_date, gainTime);

                dateTv.setVisibility(View.VISIBLE);
            } else {
                dateTv.setVisibility(View.GONE);
            }

            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onConfirmListener) {
                        onConfirmListener.onDeletePersonalInformation(CommonConstant.EXTRA_HONOR_AND_QUALIFICATION_ID, id);
                    }
                }
            });

            itemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onConfirmListener) {
                        onConfirmListener.onModifyPersonalInformation(CommonConstant.EXTRA_HONOR_AND_QUALIFICATION_ID, id);
                    }
                }
            });
        }
    }

    private void onBindAddPersonalInformationItemView(CommonRecyclerViewHolder holder, final int position) {
        RelativeLayout addPersonalInformationRL = holder.getChildView(R.id.rl_add_personal_information);

        if (position == educationalExperienceList.size() + 1) {
            holder.setTextView(R.id.tv_add_personal_information, "添加教育经历");
        } else if (position == educationalExperienceList.size() + workExperienceList.size() + 2) {
            holder.setTextView(R.id.tv_add_personal_information, "添加工作经历");
        } else if (position == educationalExperienceList.size() + workExperienceList.size() + honorAndQualificationList.size() + 3) {
            holder.setTextView(R.id.tv_add_personal_information, "添加荣誉资质");
        }

        addPersonalInformationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmListener) {
                    onConfirmListener.onAddPersonalInformation(position, educationalExperienceList, workExperienceList, honorAndQualificationList);
                }
            }
        });
    }

    public void setUserHeadPortraitUrl(String userHeadPortraitUrl) {
        this.userHeadPortraitUrl = userHeadPortraitUrl;
        notifyItemChanged(0);
    }

    public void setName(String name) {
        this.name = name;
        notifyItemChanged(0);
    }

    public void setAge(int age) {
        this.age = age;
        notifyItemChanged(0);
    }

    public void setGender(int gender) {
        this.gender = gender;
        notifyItemChanged(0);
    }

    public void setStartWorkTime(String startWorkTime) {
        this.startWorkTime = startWorkTime;
        notifyItemChanged(0);
    }

    public void setEducationalExperienceList(List<PersonalInformationBean.EducationalExperienceBean> educationalExperienceList) {
        this.educationalExperienceList.clear();
        this.educationalExperienceList.addAll(educationalExperienceList);
    }

    public void setWorkExperienceList(List<PersonalInformationBean.WorkExperienceBean> workExperienceList) {
        this.workExperienceList.clear();
        this.workExperienceList.addAll(workExperienceList);
    }

    public void setHonorAndQualificationList(List<PersonalInformationBean.HonorAndQualificationBean> honorAndQualificationList) {
        this.honorAndQualificationList.clear();
        this.honorAndQualificationList.addAll(honorAndQualificationList);
    }

    private void showBirthdayDialog() {
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

                    String birthday = yearStr + "-" + monthStr + "-" + dateStr;
                    Date birthdayDate = CommonConstant.commonDateFormat.parse(birthday);
                    age = AgeUtils.getAge(birthdayDate);

                    if (null != onConfirmListener) {
                        onConfirmListener.onBirthdayDialogConfirm(birthday, age);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    private void showGenderDialog() {
        selectGenderDialog = new SelectGenderDialog(context);

        Window window = selectGenderDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        selectGenderDialog.show();
        selectGenderDialog.setOnClickListener(new SelectGenderDialog.OnClickListener() {
            @Override
            public void onMaleClick() {
                if (selectGenderDialog.isShowing()) {
                    selectGenderDialog.dismiss();
                }

                gender = CommonConstant.MALE;

                if (null != onConfirmListener) {
                    onConfirmListener.onGenderDialogConfirm(gender);
                }
            }

            @Override
            public void onFemaleClick() {
                if (selectGenderDialog.isShowing()) {
                    selectGenderDialog.dismiss();
                }

                gender = CommonConstant.FEMALE;

                if (null != onConfirmListener) {
                    onConfirmListener.onGenderDialogConfirm(gender);
                }
            }
        });
    }

    private void showStartWorkTimeDialog() {
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

                    long currentTimeMillis = System.currentTimeMillis();

                    startWorkTime = yearStr + "-" + monthStr + "-" + dateStr;
                    Date startWorkTimeDate = CommonConstant.commonDateFormat.parse(startWorkTime);
                    long startWorkTimeMillis = startWorkTimeDate.getTime();

                    if (startWorkTimeMillis <= currentTimeMillis) {
                        if (null != onConfirmListener) {
                            onConfirmListener.onStartWorkTimeConfirm(startWorkTime);
                        }
                    } else {
                        Toast.makeText(context, "请选择正确的工作时间", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    public interface OnConfirmListener {

        void onSetOrModifyUserHeadPortrait();

        void onSetOrModifyName();

        void onBirthdayDialogConfirm(String birthday, int age);

        void onGenderDialogConfirm(int gender);

        void onStartWorkTimeConfirm(String startWorkTime);

        void onAddPersonalInformation(int position, List<PersonalInformationBean.EducationalExperienceBean> educationalExperienceList, List<PersonalInformationBean.WorkExperienceBean> workExperienceList, List<PersonalInformationBean.HonorAndQualificationBean> honorAndQualificationList);

        void onModifyPersonalInformation(String personalInformationType, int id);

        void onDeletePersonalInformation(String personalInformationType, int id);

    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

}
