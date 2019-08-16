package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class SettingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String bankCardNo;
    public String bardCardDetails;
    public Integer bardId;
    public String email;
    public String ghtBankCode;
    public String ghtBankName;
    public boolean isExistLoginPwd;
    public boolean isExistPayPwd;
    public String mobile;
    public String mobileCountryCode;
    public int mobileCountryCodeId;
    public String qqName;
    public String wxName;
    public boolean isExistGesturePwd;

}
