package cn.tanjiajun.sdk.conn.interfaces;

import com.squareup.okhttp.Call;

/**
 * 返回结果回调接口
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public interface IResultCallback {

    void onCall(Call call);

    void onResponse(String response);

    void onFailure(String error);

}
