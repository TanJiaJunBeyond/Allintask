package com.allintask.lingdao.view.main;

import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public interface IMessageView extends IBaseView {

    void onShowUserInfoList(List<UserInfoResponseBean> userInfoList);

}
