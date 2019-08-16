package cn.tanjiajun.sdk.conn;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.tanjiajun.sdk.conn.cookie.PersistentCookieStore;
import cn.tanjiajun.sdk.conn.interfaces.IOkHttpClient;
import cn.tanjiajun.sdk.conn.interfaces.IResultCallback;

/**
 * 网络请求客户端
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class OkHttpClient implements IOkHttpClient {

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = -1;

    private Handler mHandler;
    private com.squareup.okhttp.OkHttpClient mOkHttpClient;

    public OkHttpClient() {
        mHandler = new Handler(Looper.getMainLooper());
        mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
    }

    /**
     * 初始化
     *
     * @param configuration 网络客户端配置
     * @see OkHttpClientConfiguration
     * @since 0.0.1
     */
    @Override
    public void prepare(OkHttpClientConfiguration configuration) {
        if (null != configuration) {
            // 设置客户端和服务器建立连接的超时时间
            if (configuration.connectTimeoutSeconds > 0) {
                mOkHttpClient.setConnectTimeout(configuration.connectTimeoutSeconds, TimeUnit.SECONDS);
            }

            // 设置客户端上传数据到服务器的超时时间
            if (configuration.readTimeoutSeconds > 0) {
                mOkHttpClient.setReadTimeout(configuration.readTimeoutSeconds, TimeUnit.SECONDS);
            }

            // 设置客户端从服务器下载响应数据的超时时间
            if (configuration.writeTimeoutSeconds > 0) {
                mOkHttpClient.setWriteTimeout(configuration.writeTimeoutSeconds, TimeUnit.SECONDS);
            }

            if (configuration.isPersistentCookieSupported) {
                mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(configuration.context), CookiePolicy.ACCEPT_ALL));
            }

            if (configuration.isLocalCacheSupported) {
                mOkHttpClient.networkInterceptors().add(configuration.interceptor);
                mOkHttpClient.setCache(configuration.localCache);
            }

            mOkHttpClient.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));
        }
    }

    /**
     * 提交网络请求
     *
     * @param okRequest       网络请求参数
     * @param options         网络请求选项
     * @param mResultCallback 结果回调接口
     * @see #getSyncTask(OkHttpRequest, OkHttpRequestOptions, String, boolean, IResultCallback)
     * @see #getAsyncTask(OkHttpRequest, OkHttpRequestOptions, String, boolean, IResultCallback)
     * @see #postSyncTask(OkHttpRequest, OkHttpRequestOptions, String, boolean, IResultCallback)
     * @see #postAsyncTask(OkHttpRequest, OkHttpRequestOptions, String, boolean, IResultCallback)
     * @since 0.0.1
     */
    @Override
    public void commitRequestTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback) {
        if (null == okRequest) {
            sendResultCallback(null, RESULT_FAIL, "empty request", mResultCallback);
            return;
        }

        if (null == options) {
            options = OkHttpRequestOptions.createSimple();
        }

        if (options.async) {
            if (options.method.equals(OkHttpRequestOptions.GET)) {
                getAsyncTask(okRequest, options, token, isJsonString, mResultCallback);
            } else if (options.method.equals(OkHttpRequestOptions.POST) | options.method.equals(OkHttpRequestOptions.PUT) | options.method.equals(OkHttpRequestOptions.DELETE)) {
                postAsyncTask(okRequest, options, token, isJsonString, mResultCallback);
            }
        } else {
            if (options.method.equals(OkHttpRequestOptions.GET)) {
                getSyncTask(okRequest, options, token, isJsonString, mResultCallback);
            } else if (options.method.equals(OkHttpRequestOptions.POST) | options.method.equals(OkHttpRequestOptions.PUT) | options.method.equals(OkHttpRequestOptions.DELETE)) {
                postSyncTask(okRequest, options, token, isJsonString, mResultCallback);
            }
        }
    }

    /**
     * 同步GET请求
     *
     * @param okRequest       网络请求参数
     * @param options         网络请求选项
     * @param mResultCallback 结果回调接口
     * @see #sendResultCallback(Call, int, String, IResultCallback)
     * @see #deliverSyncResult(Request, OkHttpRequest, IResultCallback)
     * @since 0.0.1
     */
    @Override
    public void getSyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback) {
        String requestUrl = okRequest.getBaseUrl() + okRequest.getAPIPath();

        if (TextUtils.isEmpty(requestUrl)) {
            sendResultCallback(null, RESULT_FAIL, "empty request url", mResultCallback);
        } else {
            Request syncRequest;

            if (TextUtils.isEmpty(token)) {
                syncRequest = new Request.Builder().url(requestUrl).build();
            } else {
                syncRequest = new Request.Builder().url(requestUrl).header("access-token", "Bearer " + token).build();
            }

            deliverSyncResult(syncRequest, okRequest, mResultCallback);
        }
    }

    /**
     * 异步GET请求
     *
     * @param okRequest       网络请求参数
     * @param options         网络请求选项
     * @param mResultCallback 结果回调接口
     * @see #sendResultCallback(Call, int, String, IResultCallback)
     * @see #deliverAsyncResult(Request, OkHttpRequest, IResultCallback)
     * @since 0.0.1
     */
    @Override
    public void getAsyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, final IResultCallback mResultCallback) {
        String requestUrl = okRequest.getBaseUrl() + okRequest.getAPIPath();

        if (TextUtils.isEmpty(requestUrl)) {
            sendResultCallback(null, RESULT_FAIL, "empty request url", mResultCallback);
        } else {
            String requestParamString = okRequest.getRequestParamString();

            if (!TextUtils.isEmpty(requestParamString)) {
                requestUrl = requestUrl + "?" + okRequest.getRequestParamString();
            }

            Request asyncRequest;

            if (TextUtils.isEmpty(token)) {
                asyncRequest = new Request.Builder().url(requestUrl).build();
            } else {
                asyncRequest = new Request.Builder().url(requestUrl).header("access-token", "Bearer " + token).build();
            }

            deliverAsyncResult(asyncRequest, okRequest, mResultCallback);
        }

    }

    /**
     * 同步POST请求
     *
     * @param okRequest       网络请求参数
     * @param options         网络请求选项
     * @param mResultCallback 结果回调接口
     * @see #deliverSyncResult(Request, OkHttpRequest, IResultCallback)
     * @since 0.0.1
     */
    @Override
    public void postSyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback) {
        if (null != okRequest) {
            if (isJsonString) {
                deliverSyncResult(buildJsonStringRequest(okRequest, options), okRequest, mResultCallback);
            } else {
                if (null == okRequest.getRequestFileTypeParamMap()) {
                    deliverSyncResult(buildSingleFormRequest(okRequest, options, token), okRequest, mResultCallback);
                } else {
                    deliverSyncResult(buildMultiPartFormRequest(okRequest, options, token), okRequest, mResultCallback);
                }
            }
        }
    }

    /**
     * 异步POST请求
     *
     * @param okRequest       网络请求参数
     * @param options         网络请求选项
     * @param mResultCallback 结果回调接口
     * @see #deliverSyncResult(Request, OkHttpRequest, IResultCallback)
     * @since 0.0.1
     */
    @Override
    public void postAsyncTask(OkHttpRequest okRequest, OkHttpRequestOptions options, String token, boolean isJsonString, IResultCallback mResultCallback) {
        if (null != okRequest) {
            if (isJsonString) {
                deliverAsyncResult(buildJsonStringRequest(okRequest, options), okRequest, mResultCallback);
            } else {
                if (null == okRequest.getRequestFileTypeParamMap()) {
                    deliverAsyncResult(buildSingleFormRequest(okRequest, options, token), okRequest, mResultCallback);
                } else {
                    deliverAsyncResult(buildMultiPartFormRequest(okRequest, options, token), okRequest, mResultCallback);
                }
            }
        }
    }

    /**
     * 创建单表单（不含上传文件等）请求
     *
     * @param okRequest 网络请求参数
     * @param options   网络请求选项
     * @return 表单请求
     * @see com.squareup.okhttp.FormEncodingBuilder
     * @see com.squareup.okhttp.Request
     * @since 0.0.1
     */
    public Request buildSingleFormRequest(OkHttpRequest okRequest, OkHttpRequestOptions options, String token) {
        Request request = null;

        if (null != okRequest) {
            String url = okRequest.getBaseUrl() + okRequest.getAPIPath();
            FormEncodingBuilder formBuilder = new FormEncodingBuilder();
            Map<String, List<String>> requestFormParamMap = okRequest.getRequestFormParamMap();

            if (null != requestFormParamMap) {
                for (Map.Entry<String, List<String>> paramEntry : requestFormParamMap.entrySet()) {
                    if (null != paramEntry) {
                        String paramKey = paramEntry.getKey();
                        List<String> paramValues = paramEntry.getValue();

                        if (null != paramValues) {
                            for (String value : paramValues) {
                                formBuilder.add(paramKey, value);
                            }
                        } else {
                            formBuilder.add(paramKey, "");
                        }
                    }
                }
            }

            if (TextUtils.isEmpty(token)) {
                request = new Request.Builder().url(url).method(options.method, formBuilder.build()).build();
            } else {
                request = new Request.Builder().url(url).method(options.method, formBuilder.build()).header("access-token", "Bearer " + token).build();
            }
        }
        return request;
    }

    /**
     * 创建多表单（含上传文件等）请求
     *
     * @param okRequest 网络请求参数
     * @param options   网络请求选项
     * @return 表单请求
     * @see com.squareup.okhttp.MultipartBuilder
     * @see com.squareup.okhttp.Request
     * @since 0.0.1
     */
    public Request buildMultiPartFormRequest(OkHttpRequest okRequest, OkHttpRequestOptions options, String token) {
        Request request = null;

        if (null != okRequest) {
            MultipartBuilder multipartBuilder = new MultipartBuilder();
            multipartBuilder.type(MultipartBuilder.FORM);

            String url = okRequest.getBaseUrl() + okRequest.getAPIPath();
            Map<String, List<String>> requestFormParamMap = okRequest.getRequestFormParamMap();
            Map<String, List<File>> requestFileTypeMap = okRequest.getRequestFileTypeParamMap();

            if (null != requestFormParamMap) {
                for (Map.Entry<String, List<String>> paramEntry : requestFormParamMap.entrySet()) {
                    if (null != paramEntry) {
                        String paramKey = paramEntry.getKey();
                        List<String> paramValues = paramEntry.getValue();

                        if (null != paramValues) {
                            for (String value : paramValues) {
                                Headers headers = Headers.of("Content-Disposition", "form-data; name=\"" + paramKey + "\"");
                                RequestBody requestBody = RequestBody.create(null, value);
                                multipartBuilder.addPart(headers, requestBody);
                            }
                        } else {
                            Headers headers = Headers.of("Content-Disposition", "form-data; name=\"" + paramKey + "\"");
                            RequestBody requestBody = RequestBody.create(null, "");
                            multipartBuilder.addPart(headers, requestBody);
                        }
                    }
                }
            }

            if (null != requestFileTypeMap) {
                for (Map.Entry<String, List<File>> paramEntry : requestFileTypeMap.entrySet()) {
                    if (null != paramEntry) {
                        String paramKey = paramEntry.getKey();
                        List<File> paramValue = paramEntry.getValue();

                        if (null != paramValue) {
                            for (File targetFile : paramValue) {
                                if (null != targetFile) {
                                    String fileName = targetFile.getName();
                                    Headers headers = Headers.of("Content-Disposition", "form-data; name=\"" + paramKey + "\"; filename=\"" + fileName + "\"");
                                    RequestBody requestBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), targetFile);
                                    multipartBuilder.addPart(headers, requestBody);
                                }
                            }
                        }
                    }
                }
            }

            if (TextUtils.isEmpty(token)) {
                request = new Request.Builder().url(url).method(options.method, multipartBuilder.build()).build();
            } else {
                request = new Request.Builder().url(url).method(options.method, multipartBuilder.build()).header("access-token", "Bearer " + token).build();
            }
        }
        return request;
    }

    /**
     * 创建Json字符串请求
     *
     * @param okRequest 网络请求参数
     * @param options   网络请求选项
     * @return Json字符串请求
     * @see com.squareup.okhttp.Request
     * @since 0.0.1
     */
    public Request buildJsonStringRequest(OkHttpRequest okRequest, OkHttpRequestOptions options) {
        Request request = null;
        String jsonString = null;

        if (null != okRequest) {
            String url = okRequest.getBaseUrl() + okRequest.getAPIPath();
            Map<String, List<String>> requestFormParamMap = okRequest.getRequestFormParamMap();

            if (null != requestFormParamMap) {
                Map<String, Object> paramMap = new HashMap<>();

                for (Map.Entry<String, List<String>> paramEntry : requestFormParamMap.entrySet()) {
                    if (null != paramEntry) {
                        String paramKey = paramEntry.getKey();
                        List<String> paramValues = paramEntry.getValue();

                        if (paramKey.equals("param")) {
                            if (null != paramValues) {
                                if (paramValues.size() == 1) {
                                    String paramValue = paramValues.get(0);
                                    paramMap.put(paramKey, paramValue);
                                }
                            }
                        }
                    }
                }

                jsonString = JSONObject.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect);
            }

            if (!TextUtils.isEmpty(jsonString)) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
                request = new Request.Builder().url(url).method(options.method, requestBody).build();
            }
        }
        return request;
    }

    /**
     * 传递同步请求
     *
     * @param request         生成请求
     * @param okRequest       网络请求参数
     * @param mResultCallback 结果回调接口
     * @see OkHttpClientManager#saveResponseAsLocalData(Context, String, String)
     * @see com.squareup.okhttp.OkHttpClient#newCall(Request)
     * @see com.squareup.okhttp.Call#execute()
     * @since 0.0.1
     */
    private void deliverSyncResult(final Request request, OkHttpRequest okRequest, final IResultCallback mResultCallback) {
        if (null == request) {
            sendResultCallback(null, RESULT_FAIL, "empty request", mResultCallback);
        } else {
            try {
                Response response = mOkHttpClient.newCall(request).execute();

                // 保存离线数据至数据库
                Context context = OkHttpClientManager.getInstance().getContext();
                String requestUrl = okRequest.getLocalRequestUrl();
                String responseStr = response.body().string();
                OkHttpClientManager.getInstance().saveResponseAsLocalData(context, requestUrl, responseStr);

                sendResultCallback(null, RESULT_SUCCESS, responseStr, mResultCallback);
            } catch (IOException e) {
                e.printStackTrace();
                sendResultCallback(null, RESULT_FAIL, e.getMessage(), mResultCallback);
            }
        }
    }

    /**
     * 传递异步请求
     *
     * @param request         生成请求
     * @param okRequest       网络请求参数
     * @param mResultCallback 结果回调接口
     * @see OkHttpClientManager#saveResponseAsLocalData(Context, String, String)
     * @see com.squareup.okhttp.OkHttpClient#newCall(Request)
     * @see com.squareup.okhttp.Call#enqueue(Callback)
     * @since 0.0.1
     */
    private void deliverAsyncResult(final Request request, final OkHttpRequest okRequest, final IResultCallback mResultCallback) {
        if (null == request) {
            sendResultCallback(null, RESULT_FAIL, "empty request", mResultCallback);
        } else {
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    sendResultCallback(call, RESULT_FAIL, "", mResultCallback);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    // 保存离线数据至数据库
                    Context context = OkHttpClientManager.getInstance().getContext();
                    String requestUrl = okRequest.getLocalRequestUrl();
                    String responseStr = response.body().string();
                    OkHttpClientManager.getInstance().saveResponseAsLocalData(context, requestUrl, responseStr);

                    sendResultCallback(call, RESULT_SUCCESS, responseStr, mResultCallback);
                }
            });
        }
    }

    /**
     * 获取文件的MimeType
     *
     * @param path 文件路径
     * @return 文件的MimeType
     * @since 0.0.1
     */
    private String guessMimeType(String path) {
        FileNameMap mFileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = mFileNameMap.getContentTypeFor(path);

        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 发送返回结果
     *
     * @param call            Call
     * @param resultCode      返回结果状态
     * @param result          返回结果
     * @param mResultCallback 结果回调参数
     * @since 0.0.1
     */
    @Override
    public void sendResultCallback(Call call, final int resultCode, final String result, final IResultCallback mResultCallback) {
        if (null != mResultCallback) {
            mResultCallback.onCall(call);

            if (RESULT_SUCCESS == resultCode) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResultCallback.onResponse(result);
                    }
                });
            } else if (RESULT_FAIL == resultCode) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResultCallback.onFailure(result);
                    }
                });
            }
        }
    }

}
