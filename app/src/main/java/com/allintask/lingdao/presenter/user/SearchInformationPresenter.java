package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.user.SearchInformationBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.EnglishUtils;
import com.allintask.lingdao.view.user.ISearchInformationView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.common.utils.Validator;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SearchInformationPresenter extends BasePresenter<ISearchInformationView> {

    private IUserModel userModel;
    private List<SearchInformationBean> filtrateList;

    public SearchInformationPresenter() {
        userModel = new UserModel();
        filtrateList = new ArrayList<>();
    }

    public void fetchEducationalInstitutionListRequest() {
        OkHttpRequest fetchEducationInstitutionInfoListRequest = userModel.fetchEducationalInstitutionListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchEducationInstitutionInfoListRequest);
    }

    public void fetchMajorListRequest() {
        OkHttpRequest fetchMajorInfoListRequest = userModel.fetchMajorListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchMajorInfoListRequest);
    }

    public void fetchEducationalBackgroundListRequest() {
        OkHttpRequest fetchEducationBackgroundListRequest = userModel.fetchEducationalBackgroundListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchEducationBackgroundListRequest);
    }

    public void filtrateEducationInstitution(String keyword, List<SearchInformationBean> educationInstitutionList) {
        if (null != filtrateList && filtrateList.size() > 0) {
            this.filtrateList.clear();
        }

        if (null != educationInstitutionList && educationInstitutionList.size() > 0) {
            for (int i = 0; i < educationInstitutionList.size(); i++) {
                SearchInformationBean searchInformationBean = educationInstitutionList.get(i);

                if (EnglishUtils.isEnglishCharacterString(keyword)) {
                    String pyName = TypeUtils.getString(searchInformationBean.pyName, "");
                    String pyShort = TypeUtils.getString(searchInformationBean.pyShort, "");

                    if (keyword.equals(pyName) | pyShort.contains(keyword)) {
                        filtrateList.add(searchInformationBean);
                    }
                } else if (Validator.isChineseCharacter(keyword)) {
                    String name = TypeUtils.getString(searchInformationBean.name, "");

                    if (Validator.isChineseCharacter(name) && name.contains(keyword)) {
                        filtrateList.add(searchInformationBean);
                    }
                }
            }

            if (null != getView()) {
                getView().onShowFiltrateList(filtrateList);
            }
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_SCHOOL_INFO_LIST)) {
            if (success) {
                List<SearchInformationBean> educationInstitutionList = JSONArray.parseArray(data, SearchInformationBean.class);

                if (null != getView()) {
                    getView().onShowSearchInformationList(educationInstitutionList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_AP_NAME_GENERAL_DATA_MAJOR_INFO_LIST)) {
            if (success) {
                List<SearchInformationBean> majorList = JSONArray.parseArray(data, SearchInformationBean.class);

                if (null != getView()) {
                    getView().onShowSearchInformationList(majorList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_EDU_LEVEL_LIST)) {
            if (success) {
                List<SearchInformationBean> educationBackgroundList = JSONArray.parseArray(data, SearchInformationBean.class);

                if (null != getView()) {
                    getView().onShowSearchInformationList(educationBackgroundList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
