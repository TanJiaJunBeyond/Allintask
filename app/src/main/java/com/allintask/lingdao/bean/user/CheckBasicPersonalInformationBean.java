package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TanJiaJun on 2018/3/26.
 */

public class CheckBasicPersonalInformationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int avatarImageId;
    public String avatarImageUrl;
    public Date birthday;
    public Integer gender;
    public boolean isComplete;
    public String name;
    public boolean realNameLock;

}
