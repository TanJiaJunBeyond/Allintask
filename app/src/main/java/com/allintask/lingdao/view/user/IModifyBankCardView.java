package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.DepositBankBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public interface IModifyBankCardView extends IBaseView {

    void onShowBankCardDetails(String bankInfoId, String bankCardNumber, String bankCardPhotoUrl);

    void onShowDepositBankListRequest(List<DepositBankBean> depositBankList);

    void uploadSuccess(int status, String codeId, String imageUrl);

    void uploadFail();

    void onModifyBankCardSuccess();

}
