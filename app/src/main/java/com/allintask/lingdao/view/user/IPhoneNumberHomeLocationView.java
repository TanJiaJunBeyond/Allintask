package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationListBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/22.
 */

public interface IPhoneNumberHomeLocationView extends IBaseView {

    void onShowPhoneNumberHomeLocationList(List<PhoneNumberHomeLocationBean> isAllList, List<PhoneNumberHomeLocationBean> isHotList);

}
