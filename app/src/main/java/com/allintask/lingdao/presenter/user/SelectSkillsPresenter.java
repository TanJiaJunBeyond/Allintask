package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.service.HotSkillsBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISelectSkillsView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/2.
 */

public class SelectSkillsPresenter extends BasePresenter<ISelectSkillsView> {

    private IServiceModel serviceModel;
    private List<HotSkillsBean> hotSkillsList;

    public SelectSkillsPresenter() {
        serviceModel = new ServiceModel();
        hotSkillsList = new ArrayList<>();
    }

    public void fetchHotSkillsListRequest() {
        OkHttpRequest fetchHotSkillsListRequest = serviceModel.fetchHotSkillsListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchHotSkillsListRequest);
    }

    public void selectSkillsRequest(int categoryId) {
        OkHttpRequest selectSkillsRequest = serviceModel.selectSkillsRequest(categoryId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, selectSkillsRequest);
    }

    public int getSkillsId(int position) {
        int skillId = 0;

        for (int i = 0; i < hotSkillsList.size(); i++) {
            HotSkillsBean hotSkillsBean = hotSkillsList.get(i);

            if (null != hotSkillsBean) {
                int tempSkillsId = TypeUtils.getInteger(hotSkillsBean.code, -1);

                if (i == position) {
                    skillId = tempSkillsId;
                }
            }
        }
        return skillId;
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_HOT)) {
            if (success) {
                List<HotSkillsBean> hotSkillsList = JSONArray.parseArray(data, HotSkillsBean.class);

                if (null != hotSkillsList && hotSkillsList.size() > 0) {
                    this.hotSkillsList.clear();
                    this.hotSkillsList.addAll(hotSkillsList);

                    List<String> skillsList = new ArrayList<>();

                    for (int i = 0; i < this.hotSkillsList.size(); i++) {
                        HotSkillsBean hotSkillsBean = this.hotSkillsList.get(i);

                        if (null != hotSkillsBean) {
                            String skills = TypeUtils.getString(hotSkillsBean.value, "");
                            skillsList.add(skills);
                        }
                    }

                    if (null != getView()) {
                        getView().onShowHotSkillsList(skillsList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_EXPERIENCE)) {
            if (null != getView()) {
                if (success) {
                    getView().onSelectSkillsSuccess();
                } else {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
