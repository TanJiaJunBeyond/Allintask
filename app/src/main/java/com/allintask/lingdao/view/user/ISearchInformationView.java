package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.SearchInformationBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public interface ISearchInformationView extends IBaseView {

    void onShowSearchInformationList(List<SearchInformationBean> searchInformationList);

    void onShowFiltrateList(List<SearchInformationBean> filtrateList);

}
