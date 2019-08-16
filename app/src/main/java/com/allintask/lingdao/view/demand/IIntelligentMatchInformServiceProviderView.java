package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.bean.demand.IntelligentMatchInformServiceProviderBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/9.
 */

public interface IIntelligentMatchInformServiceProviderView extends IBaseView {

    void onShowIntelligentMatchInformServiceProviderList(List<IntelligentMatchInformServiceProviderBean> intelligentMatchInformServiceProviderList);

}
