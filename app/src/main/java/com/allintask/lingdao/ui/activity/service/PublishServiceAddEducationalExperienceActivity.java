package com.allintask.lingdao.ui.activity.service;

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
import com.allintask.lingdao.presenter.service.PublishServiceAddEducationalExperiencePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.user.AddPersonalInformationAdapter;
import com.allintask.lingdao.view.service.IPublishServiceAddEducationalExperienceView;
import com.allintask.lingdao.widget.CommonRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public class PublishServiceAddEducationalExperienceActivity extends BaseActivity<IPublishServiceAddEducationalExperienceView, PublishServiceAddEducationalExperiencePresenter> implements IPublishServiceAddEducationalExperienceView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.tv_content)
    TextView contentTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;

    private int serviceId = -1;
    private boolean isExistWorkExperience = false;
    private boolean isExistHonorAndQualification = false;
    private boolean isNeedServicePhotoAlbum = false;

    private AddPersonalInformationAdapter addPersonalInformationAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish_service_add_personal_information;
    }

    @Override
    protected PublishServiceAddEducationalExperiencePresenter CreatePresenter() {
        return new PublishServiceAddEducationalExperiencePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            serviceId = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_ID, -1);
            isExistWorkExperience = intent.getBooleanExtra(CommonConstant.EXTRA_IS_EXIST_WORK_EXPERIENCE, false);
            isExistHonorAndQualification = intent.getBooleanExtra(CommonConstant.EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION, false);
            isNeedServicePhotoAlbum = intent.getBooleanExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, false);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.publish_service));
        rightFirstTv.setText(getString(R.string.skip));
        rightFirstTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExistWorkExperience) {
                    Intent intent = new Intent(getParentContext(), PublishServiceAddWorkExperienceActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                    intent.putExtra(CommonConstant.EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION, isExistHonorAndQualification);
                    intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
                    startActivity(intent);
                } else if (!isExistHonorAndQualification) {
                    Intent intent = new Intent(getParentContext(), PublishServiceAddHonorAndQualificationActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                    intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
                    startActivity(intent);
                } else if (isNeedServicePhotoAlbum) {
                    Intent intent = new Intent(getParentContext(), UploadAlbumActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                    intent.putExtra(CommonConstant.EXTRA_UPLOAD_ALBUM_TYPE, CommonConstant.UPLOAD_ALBUM_PUBLISH_SERVICE);
                    startActivity(intent);
                }

                finish();
            }
        });

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        contentTv.setText(getString(R.string.publish_service_add_educational_experience_content));

        addPersonalInformationAdapter = new AddPersonalInformationAdapter(getParentContext(), CommonConstant.EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_EDUCATIONAL_EXPERIENCE, CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE);
        recyclerView.setAdapter(addPersonalInformationAdapter);

        addPersonalInformationAdapter.setOnCLickListener(new AddPersonalInformationAdapter.OnClickListener() {
            @Override
            public void onEducationalExperienceSaveButtonClick(View view, List<AddEducationalExperienceBean> educationalExperienceList) {
                mPresenter.saveEducationalExperienceRequest(educationalExperienceList);
            }

            @Override
            public void onWorkExperienceSaveButtonClick(View view, List<AddWorkExperienceBean> workExperienceList) {

            }

            @Override
            public void onHonorAndQualificationSaveButtonClick(View view, List<AddHonorAndQualificationBean> honorAndQualificationList) {

            }
        });
    }

    private void initData() {
        List<AddEducationalExperienceBean> educationExperienceList = new ArrayList<>();
        AddEducationalExperienceBean addEducationalExperienceBean = new AddEducationalExperienceBean();
        addEducationalExperienceBean.school = getString(R.string.please_select_your_school);
        addEducationalExperienceBean.major = getString(R.string.please_select_major);
        addEducationalExperienceBean.educationalBackground = getString(R.string.please_input_select_education_background);
        addEducationalExperienceBean.startAt = getString(R.string.please_select_start_time);
        addEducationalExperienceBean.endAt = getString(R.string.please_select_end_time);
        educationExperienceList.add(addEducationalExperienceBean);
        addPersonalInformationAdapter.setDateList(educationExperienceList);
    }

    @Override
    public void onSaveEducationalExperienceSuccess() {
        if (!isExistWorkExperience) {
            Intent intent = new Intent(getParentContext(), PublishServiceAddWorkExperienceActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
            intent.putExtra(CommonConstant.EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION, isExistHonorAndQualification);
            intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
            startActivity(intent);
        } else if (!isExistHonorAndQualification) {
            Intent intent = new Intent(getParentContext(), PublishServiceAddHonorAndQualificationActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
            intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
            startActivity(intent);
        } else if (isNeedServicePhotoAlbum) {
            Intent intent = new Intent(getParentContext(), UploadAlbumActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
            intent.putExtra(CommonConstant.EXTRA_UPLOAD_ALBUM_TYPE, CommonConstant.UPLOAD_ALBUM_PUBLISH_SERVICE);
            startActivity(intent);
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE && null != data) {
            int position = data.getIntExtra(CommonConstant.EXTRA_POSITION, 0);
            int searchInformationType = data.getIntExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION);
            String searchInformation = data.getStringExtra(CommonConstant.EXTRA_SEARCH_INFORMATION);
            int educationalBackgroundId = data.getIntExtra(CommonConstant.EXTRA_EDUCATIONAL_BACKGROUND_ID, 0);

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

            addPersonalInformationAdapter.checkSaveEnable(CommonConstant.ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE, position);
            addPersonalInformationAdapter.notifyItemChanged(position);
            addPersonalInformationAdapter.notifyItemChanged(addPersonalInformationAdapter.getItemCount() - 1);
        }
    }

}
