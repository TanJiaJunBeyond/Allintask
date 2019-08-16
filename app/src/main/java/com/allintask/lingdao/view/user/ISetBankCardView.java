package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.DepositBankBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public interface ISetBankCardView extends IBaseView {

    void onShowDepositBankListRequest(List<DepositBankBean> depositBankList);

    void uploadSuccess(int status, String codeId, String imageUrl);

    void uploadFail();

    void onSetBankCardSuccess();

}
