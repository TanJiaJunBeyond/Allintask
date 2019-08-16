package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/1/25.
 */

public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String accessToken;
    public String expireIn;
    public String refreshToken;
    public int userId;
    public String password;

}
