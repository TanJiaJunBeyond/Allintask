package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/4/2.
 */

public class AppUpdateBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int isForceUpdate;
    public int isShow;
    public String tip;
    public String url;
    public String version;
    public String newVersion;

}
