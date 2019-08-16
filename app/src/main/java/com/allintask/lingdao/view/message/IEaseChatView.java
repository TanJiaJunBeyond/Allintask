package com.allintask.lingdao.view.message;

import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/3/24.
 */

public interface IEaseChatView extends IBaseView {

    void onShowUserInfoBean(UserInfoResponseBean userInfoResponseBean);

}
