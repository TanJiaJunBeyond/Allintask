package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/15.
 */

public class CheckUploadIsSuccessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int status;
    public String codeId;
    public String imgUrl;

}
