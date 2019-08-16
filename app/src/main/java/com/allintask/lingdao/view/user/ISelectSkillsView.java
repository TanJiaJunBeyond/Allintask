package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/2.
 */

public interface ISelectSkillsView extends IBaseView {

    void onShowHotSkillsList(List<String> hotSkillsList);

    void onSelectSkillsSuccess();

}
