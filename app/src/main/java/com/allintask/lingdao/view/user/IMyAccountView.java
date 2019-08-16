package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.TransactionRecordListBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public interface IMyAccountView extends ISwipeRefreshView {

    void onShowMobileCountryCodeId(int mobileCountryCodeId);

    void onShowBankId(Integer bankId);

    void onShowBankCardName(String bankCardName);

    void onShowAccountData(double accountBalance, double canWithdraw, double advanceRecharge, boolean isShowRechargeButton, boolean isShowWithdrawButton, double withdrawLowPrice, double withdrawRate);

    void onShowCheckCanWithdrawData(boolean isExistBankCard, boolean isExistPayPwd, boolean isExistRealName);

    void onShowTransactionRecordList(List<TransactionRecordListBean.TransactionRecordBean> transactionRecordList);

}
