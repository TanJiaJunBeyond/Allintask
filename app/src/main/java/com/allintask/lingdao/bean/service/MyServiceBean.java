package com.allintask.lingdao.bean.service;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/2/6.
 */

public class MyServiceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;
    public String advantage;
    public int categoryId;
    public String categoryPropertyList;
    public String format;
    public String introduce;
    public String serveWayPriceUnitList;
    public int userId;
    public String voiceSrcUrl;
    public String voiceUrl;
    public int onOffLine;
    public int auditCode;
    public long createTime;
    public int voiceDuration;
    public String provinceCode;
    public String cityCode;

}
