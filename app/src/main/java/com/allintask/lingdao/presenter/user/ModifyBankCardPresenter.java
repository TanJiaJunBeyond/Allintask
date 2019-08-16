package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.BankCardDetailsBean;
import com.allintask.lingdao.bean.user.BankInfoDetailsBean;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.bean.user.DepositBankBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyBankCardView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class ModifyBankCardPresenter extends BasePresenter<IModifyBankCardView> {

    private IUserModel userModel;

    private int bankId;

    public ModifyBankCardPresenter() {
        userModel = new UserModel();
    }

    public void fetchBankCardDetailsRequest(int bankId) {
        this.bankId = bankId;
        OkHttpRequest fetchBankCardDetailsRequest = userModel.fetchBankCardDetailsRequest(this.bankId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchBankCardDetailsRequest);
    }

    public void fetchBankInfoListRequest() {
        OkHttpRequest fetchBankInfoListRequest = userModel.fetchBankInfoListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchBankInfoListRequest);
    }

    public void checkUploadIsSuccessRequest(String flagId) {
        OkHttpRequest checkUploadIsSuccessRequest = userModel.checkUploadIsSuccessRequest(flagId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkUploadIsSuccessRequest);
    }

    public void updateBankCardRequest(String updateBankCardRequestBeanJsonString) {
        OkHttpRequest updateBankCardRequest = userModel.updateBankCardRequest(updateBankCardRequestBeanJsonString);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, updateBankCardRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_GET + "/" + String.valueOf(bankId))) {
            if (success) {
                BankCardDetailsBean bankCardDetailsBean = JSONObject.parseObject(data, BankCardDetailsBean.class);

                if (null != bankCardDetailsBean) {
                    String ghtBankCode = TypeUtils.getString(bankCardDetailsBean.ghtBankCode, "");
                    String bankCardNo = TypeUtils.getString(bankCardDetailsBean.bankCardNo, "");
                    String bankCardImageUrl = TypeUtils.getString(bankCardDetailsBean.bankCardImageUrl, "");

                    if (null != getView()) {
                        getView().onShowBankCardDetails(ghtBankCode, bankCardNo, bankCardImageUrl);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_LIST)) {
            if (success) {
                List<DepositBankBean> depositBankList = JSONArray.parseArray(data, DepositBankBean.class);

                if (null != getView()) {
                    getView().onShowDepositBankListRequest(depositBankList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN)) {
            if (success) {
                List<CheckUploadIsSuccessBean> checkUploadIsSuccessList = JSONArray.parseArray(data, CheckUploadIsSuccessBean.class);

                int status = -1;
                String codeId = null;
                String imgUrl = null;

                if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
                    CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(0);

                    if (null != checkUploadIsSuccessBean) {
                        status = TypeUtils.getInteger(checkUploadIsSuccessBean.status, -1);
                        codeId = TypeUtils.getString(checkUploadIsSuccessBean.codeId, "");
                        imgUrl = TypeUtils.getString(checkUploadIsSuccessBean.imgUrl, "");
                    }
                }

                if (null != getView()) {
                    getView().uploadSuccess(status, codeId, imgUrl);
                }
            } else {
                if (null != getView()) {
                    getView().uploadFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_UPDATE)) {
            if (success) {
                if (null != getView()) {
                    getView().onModifyBankCardSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("修改银行卡失败");
                }
            }
        }
    }

}
