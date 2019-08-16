package cn.tanjiajun.sdk.conn.interfaces;

import com.squareup.okhttp.Call;

import cn.tanjiajun.sdk.conn.OkHttpClientConfiguration;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * 网络客户端接口
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public interface IOkHttpClient {

    void prepare(OkHttpClientConfiguration configuration);

    void sendResultCallback(Call call, int resultCode, String result, IResultCallback mResultCallback);

    void commitRequestTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback);

    void getSyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback);

    void getAsyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback);

    void postSyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback);

    void postAsyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback);

}
