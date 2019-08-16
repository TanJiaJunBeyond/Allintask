package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TanJiaJun on 2018/3/16.
 */

public class MyDataBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean zmrzAuthSuccess;
    public String avatarUrl;
    public Date birthday;
    public int gender;
    public String name;
    public Date startWorkAt;
    public int storeUpCount;
    public int userId;
    public boolean isExistGesturePwd;
    public int resumeCompleteRate;

}
