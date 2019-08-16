package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public interface ISelectServiceView extends IBaseView {

    void onShowSelectServiceList(List<String> serviceList);

    void onSelectServiceSuccess();

}
