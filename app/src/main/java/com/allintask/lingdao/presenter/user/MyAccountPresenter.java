package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.AccountBalanceBean;
import com.allintask.lingdao.bean.user.CheckCanWithdrawBean;
import com.allintask.lingdao.bean.user.MyDataBean;
import com.allintask.lingdao.bean.user.SettingBean;
import com.allintask.lingdao.bean.user.TransactionRecordListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IMyAccountView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public class MyAccountPresenter extends BasePresenter<IMyAccountView> {

    private IUserModel userModel;
    private List<TransactionRecordListBean.TransactionRecordBean> transactionRecordList;

    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;

    public MyAccountPresenter() {
        userModel = new UserModel();
        transactionRecordList = new ArrayList<>();
    }

    public void fetchSettingRequest() {
        OkHttpRequest fetchSettingRequest = userModel.fetchSettingRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchSettingRequest);
    }

    public void fetchAccountBalanceRequest() {
        OkHttpRequest fetchAccountBalanceRequest = userModel.fetchAccountBalanceRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAccountBalanceRequest);
    }

    public void checkCanWithdrawRequest() {
        OkHttpRequest checkCanWithdrawRequest = userModel.checkCanWithdrawRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkCanWithdrawRequest);
    }

    private void fetchTransactionRecordListRequest(boolean isRefresh) {
        if (isRefresh) {
            transactionRecordList.clear();
        }

        OkHttpRequest fetchTransactionRecordListRequest = userModel.fetchTransactionRecordListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchTransactionRecordListRequest);
    }

    public void refresh() {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);
            fetchTransactionRecordListRequest(true);
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore() {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);
            fetchTransactionRecordListRequest(false);
        } else {
            getView().setLoadMore(false);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING)) {
            if (success) {
                SettingBean settingBean = JSONObject.parseObject(data, SettingBean.class);

                if (null != settingBean) {
                    int mobileCountryCodeId = TypeUtils.getInteger(settingBean.mobileCountryCodeId, -1);
                    Integer bardId = TypeUtils.getInteger(settingBean.bardId, -1);
                    String ghtBankName = TypeUtils.getString(settingBean.ghtBankName, "");

                    if (null != getView()) {
                        getView().onShowMobileCountryCodeId(mobileCountryCodeId);
                        getView().onShowBankId(bardId);
                        getView().onShowBankCardName(ghtBankName);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_WALLET_DETAILS)) {
            if (success) {
                AccountBalanceBean accountBalanceBean = JSONObject.parseObject(data, AccountBalanceBean.class);

                if (null != accountBalanceBean) {
                    double advanceRecharge = TypeUtils.getDouble(accountBalanceBean.advanceRecharge, 0D);
                    double canWithdraw = TypeUtils.getDouble(accountBalanceBean.canWithdraw, 0D);
                    boolean isShowRechargeButton = TypeUtils.getBoolean(accountBalanceBean.isShowRechargeButton, false);
                    boolean isShowWithdrawButton = TypeUtils.getBoolean(accountBalanceBean.isShowWithdrawButton, false);
                    double totalAmount = TypeUtils.getDouble(accountBalanceBean.totalAmount, 0D);
                    double withdrawLowPrice = TypeUtils.getDouble(accountBalanceBean.withdrawLowPrice, 0D);
                    double withdrawRate = TypeUtils.getDouble(accountBalanceBean.withdrawRate, 0D);

                    if (null != getView()) {
                        getView().onShowAccountData(totalAmount, canWithdraw, advanceRecharge, isShowRechargeButton, isShowWithdrawButton, withdrawLowPrice, withdrawRate);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_CHECK_CAN_WITHDRAW)) {
            if (success) {
                CheckCanWithdrawBean checkCanWithdrawBean = JSONObject.parseObject(data, CheckCanWithdrawBean.class);

                if (null != checkCanWithdrawBean) {
                    boolean isExistBankCard = TypeUtils.getBoolean(checkCanWithdrawBean.isExistBankCard, false);
                    boolean isExistPayPwd = TypeUtils.getBoolean(checkCanWithdrawBean.isExistPayPwd, false);
                    boolean isExistRealName = TypeUtils.getBoolean(checkCanWithdrawBean.isExistRealName, false);

                    if (null != getView()) {
                        getView().onShowCheckCanWithdrawData(isExistBankCard, isExistPayPwd, isExistRealName);
                        getView().showContentView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_BILL_LIST)) {
            if (success) {
                TransactionRecordListBean transactionRecordListBean = JSONObject.parseObject(data, TransactionRecordListBean.class);

                if (null != transactionRecordListBean) {
                    List<TransactionRecordListBean.TransactionRecordBean> transactionRecordList = transactionRecordListBean.list;

                    if (null != transactionRecordList && transactionRecordList.size() > 0) {
                        this.transactionRecordList.addAll(transactionRecordList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowTransactionRecordList(this.transactionRecordList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }
    }

}
