package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.AddEducationalExperienceBean;
import com.allintask.lingdao.bean.user.AddHonorAndQualificationBean;
import com.allintask.lingdao.bean.user.AddWorkExperienceBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.AddPersonalInformationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.user.AddPersonalInformationAdapter;
import com.allintask.lingdao.view.user.IAddPersonalInformationView;
import com.allintask.lingdao.widget.CommonRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/1/19.
 */

public class AddPersonalInformationActivity extends BaseActivity<IAddPersonalInformationView, AddPersonalInformationPresenter> implements IAddPersonalInformationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;

    private int addPersonalInformationStatus;

    private AddPersonalInformationAdapter addPersonalInformationAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_personal_information;
    }

    @Override
    protected AddPersonalInformationPresenter CreatePresenter() {
        return new AddPersonalInformationPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            addPersonalInformationStatus = intent.getIntExtra(CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_TYPE, CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        switch (addPersonalInformationStatus) {
            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                titleTv.setText(getString(R.string.education_experience));
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                titleTv.setText(getString(R.string.work_experience));
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                titleTv.setText(getString(R.string.honor_and_qualification));
                break;
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        addPersonalInformationAdapter = new AddPersonalInformationAdapter(getParentContext(), CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_ADD_PERSONAL_INFORMATION, addPersonalInformationStatus);
        recyclerView.setAdapter(addPersonalInformationAdapter);

        addPersonalInformationAdapter.setOnCLickListener(new AddPersonalInformationAdapter.OnClickListener() {
            @Override
            public void onEducationalExperienceSaveButtonClick(View view, List<AddEducationalExperienceBean> educationalExperienceList) {
                mPresenter.saveEducationalExperienceRequest(educationalExperienceList);
            }

            @Override
            public void onWorkExperienceSaveButtonClick(View view, List<AddWorkExperienceBean> workExperienceList) {
                mPresenter.saveWorkExperienceRequest(workExperienceList);
            }

            @Override
            public void onHonorAndQualificationSaveButtonClick(View view, List<AddHonorAndQualificationBean> honorAndQualificationList) {
                mPresenter.saveHonorAndQualificationRequest(honorAndQualificationList);
            }
        });
    }

    private void initData() {
        switch (addPersonalInformationStatus) {
            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE:
                List<AddEducationalExperienceBean> educationExperienceList = new ArrayList<>();
                AddEducationalExperienceBean addEducationalExperienceBean = new AddEducationalExperienceBean();
                addEducationalExperienceBean.school = getString(R.string.please_select_your_school);
                addEducationalExperienceBean.major = getString(R.string.please_select_major);
                addEducationalExperienceBean.educationalBackground = getString(R.string.please_input_select_education_background);
                addEducationalExperienceBean.startAt = getString(R.string.please_select_start_time);
                addEducationalExperienceBean.endAt = getString(R.string.please_select_end_time);
                educationExperienceList.add(addEducationalExperienceBean);
                addPersonalInformationAdapter.setDateList(educationExperienceList);
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE:
                List<AddWorkExperienceBean> workExperienceList = new ArrayList<>();
                AddWorkExperienceBean addWorkExperienceBean = new AddWorkExperienceBean();
                addWorkExperienceBean.startAt = getString(R.string.please_select_start_time);
                addWorkExperienceBean.endAt = getString(R.string.please_select_end_time);
                workExperienceList.add(addWorkExperienceBean);
                addPersonalInformationAdapter.setDateList(workExperienceList);
                break;

            case CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION:
                List<AddHonorAndQualificationBean> honorAndQualificationList = new ArrayList<>();
                AddHonorAndQualificationBean addHonorAndQualificationBean = new AddHonorAndQualificationBean();
                addHonorAndQualificationBean.gainAt = getString(R.string.please_select_acquisition_time);
                honorAndQualificationList.add(addHonorAndQualificationBean);
                addPersonalInformationAdapter.setDateList(honorAndQualificationList);
                break;
        }
    }

    @Override
    public void onSaveEducationalExperienceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onSaveWorkExperienceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onSaveHonorAndQualificationSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE && null != data) {
            int position = data.getIntExtra(CommonConstant.EXTRA_POSITION, 0);
            int searchInformationType = data.getIntExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION);
            String searchInformation = data.getStringExtra(CommonConstant.EXTRA_SEARCH_INFORMATION);
            int educationalBackgroundId = data.getIntExtra(CommonConstant.EXTRA_EDUCATIONAL_BACKGROUND_ID, -1);

            AddEducationalExperienceBean addEducationalExperienceBean = (AddEducationalExperienceBean) addPersonalInformationAdapter.getItem(position);

            switch (searchInformationType) {
                case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION:
                    if (!TextUtils.isEmpty(searchInformation)) {
                        addEducationalExperienceBean.school = searchInformation;
                    } else {
                        addEducationalExperienceBean.school = getString(R.string.please_select_your_school);
                    }
                    break;

                case CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR:
                    if (!TextUtils.isEmpty(searchInformation)) {
                        addEducationalExperienceBean.major = searchInformation;
                    } else {
                        addEducationalExperienceBean.major = getString(R.string.please_select_major);
                    }
                    break;

                case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND:
                    if (!TextUtils.isEmpty(searchInformation)) {
                        addEducationalExperienceBean.educationalBackground = searchInformation;
                    } else {
                        addEducationalExperienceBean.educationalBackground = getString(R.string.please_input_select_education_background);
                    }

                    addEducationalExperienceBean.educationLevelId = educationalBackgroundId;
                    break;
            }

            addPersonalInformationAdapter.checkSaveEnable(addPersonalInformationStatus, position);
            addPersonalInformationAdapter.notifyItemChanged(position);
            addPersonalInformationAdapter.notifyItemChanged(addPersonalInformationAdapter.getItemCount() - 1);
        }
    }

}
