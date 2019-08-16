package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by TanJiaJun on 2018/2/1.
 */

public class PublishServiceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean isShow;
    public boolean isRequired;
    public int categoryId;
    public String name;
    public int maxSelectCount;
    public List<String> subclassNameList;
    public Set<Integer> isSelectedSet;
    public List<Integer> isSelectedCategoryIdList;

}
