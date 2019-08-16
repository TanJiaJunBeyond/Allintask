package com.allintask.lingdao.bean.upload;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/19.
 */

public class UploadRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String contentLength;
    public String fileName;
    public String userId;
    public String sourceId;
    public String flagId;
    public String codeId;
    public String version;
    public String sign;

}
