package cn.tanjiajun.sdk.conn;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cn.tanjiajun.sdk.common.utils.NetworkUtils;
import cn.tanjiajun.sdk.conn.interfaces.IConnectorDatabank;
import cn.tanjiajun.sdk.conn.interfaces.IOkHttpClient;
import cn.tanjiajun.sdk.conn.interfaces.IResultCallback;

/**
 * 网络请求管理类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class OkHttpClientManager {

    private static final String TAG = "OkHttpClientManager";
    /**
     * 网络请求管理器
     */
    private static OkHttpClientManager mOkHttpClientManager;

    /**
     * 网络客户端配置
     */
    private OkHttpClientConfiguration mConfiguration;

    /**
     * 网络客户端
     */
    private IOkHttpClient mOkHttpClient;

    private ExecutorService saveThreadExecutor;

    private OkHttpClientManager() {
        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient();
        }

        if (null == saveThreadExecutor) {
            saveThreadExecutor = Executors.newFixedThreadPool(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setPriority(Thread.NORM_PRIORITY - 1);
                    thread.setName(TAG);
                    return thread;
                }
            });
        }
    }

    public static OkHttpClientManager getInstance() {
        if (null == mOkHttpClientManager) {
            mOkHttpClientManager = new OkHttpClientManager();
        }
        return mOkHttpClientManager;
    }

    /**
     * 初始化配置，必须完成初始化才能开始工作
     *
     * @param mConfiguration 网络客户端配置
     * @since 0.0.1
     */
    public void init(OkHttpClientConfiguration mConfiguration) {
        if (null == mConfiguration) {
            // TODO 尚未处理

        } else {
            if (null == this.mConfiguration) {
                this.mConfiguration = mConfiguration;

                if (null != mOkHttpClient) {
                    mOkHttpClient.prepare(mConfiguration);
                }
            } else {
                Log.w("JNSTesting", "Try to init configuration which has already been initialized before");
            }
        }
    }

    /**
     * 是否初始化完毕
     *
     * @return true表示已经初始化，可以开始发起请求
     */
    public boolean isInit() {
        return (mConfiguration != null);
    }

    /**
     * 提交网络请求任务
     *
     * @param request         网络请求参数
     * @param mResultCallback 结果回调接口
     * @see #commitRequestTask(OkHttpRequest, OkHttpRequestOptions, String, boolean, IResultCallback)
     * @since 0.0.1
     */
    public void commitRequestTask(OkHttpRequest request, String token, boolean jsJsonString, IResultCallback mResultCallback) {
        commitRequestTask(request, null, token, jsJsonString, mResultCallback);
    }

    /**
     * 提交网络请求任务
     *
     * @param request         网络请求参数
     * @param options         网络请求选项
     * @param mResultCallback 结果回调接口
     * @since 0.0.1
     */
    public void commitRequestTask(final OkHttpRequest request, OkHttpRequestOptions options, final String token, final boolean isJsonString, final IResultCallback mResultCallback) {
        if (!isInit()) {
            Log.w("JNSTesting", "OkHttpClientManager has not init");
            return;
        }

        // 若options为空，使用配置中默认的options
        if (null == options) {
            options = mConfiguration.okHttpRequestOptions;
        }

        if (saveThreadExecutor != null && !saveThreadExecutor.isShutdown()) {
            final OkHttpRequestOptions finalOptions = options;
            saveThreadExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    String response = checkNetworkAndGetLocalData(mConfiguration.context, request);

                    if (!TextUtils.isEmpty(response)) {
                        mOkHttpClient.sendResultCallback(null, 0, response, mResultCallback);
                    } else {
                        mOkHttpClient.commitRequestTask(request, finalOptions, token, isJsonString, mResultCallback);
                    }
                }
            });
        }

//        String response = checkNetworkAndGetLocalData(mConfiguration.context, request);
//        if (!TextUtils.isEmpty(response)) {
//            mOkHttpClient.sendResultCallback(0, response, mResultCallback);
//        } else {
//            mOkHttpClient.commitRequestTask(request, options, mResultCallback);
//        }

    }

    /**
     * 判断网络是否可用，false则取出本地缓存数据
     *
     * @param context 上下文环境
     * @param request 网络请求参数
     * @return 本地缓存数据
     * @see NetworkUtils#isNetworkAvaliable(Context)
     * @see ConnectorDatabank#getData(String)
     * @since 0.0.1
     */
    protected String checkNetworkAndGetLocalData(Context context, OkHttpRequest request) {
        String response = null;

        if (null != context && null != request) {
            String requestUrl = request.getLocalRequestUrl();

            if (!NetworkUtils.isNetworkAvaliable(context)) {
                if (mConfiguration.isLocalDatabankSupported) {
                    if (!TextUtils.isEmpty(requestUrl)) {
                        IConnectorDatabank mConnectorDatabank = new ConnectorDatabank(context);
                        response = mConnectorDatabank.getData(requestUrl);
                        mConnectorDatabank.closeDatabase();
                    }
                }
            }
        }
        return response;
    }

    /**
     * 缓存返回结果到本地
     *
     * @param context     上下文环境
     * @param requestUrl  网络请求链接
     * @param responseStr 返回结果
     * @see #saveResponseAsLocalData(Context, Map)
     * @since 0.0.1
     */
    protected void saveResponseAsLocalData(final Context context, final String requestUrl, String responseStr) {
        if (null != requestUrl) {
            final Map<String, String> responseMap = new HashMap<String, String>();
            responseMap.put(requestUrl, responseStr);

            if (saveThreadExecutor != null && !saveThreadExecutor.isShutdown()) {
                saveThreadExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        saveResponseAsLocalData(context, responseMap);
                    }
                });
            }
        }
    }

    /**
     * 缓存返回结果到本地
     *
     * @param context     上下文环境
     * @param responseMap 返回结果集合
     * @since 0.0.1
     */
    protected void saveResponseAsLocalData(Context context, Map<String, String> responseMap) {
        if (null != context && null != responseMap) {
            IConnectorDatabank mConnectorDatabank = new ConnectorDatabank(context);

            for (Map.Entry<String, String> responseEntry : responseMap.entrySet()) {
                if (null != responseEntry) {
                    mConnectorDatabank.saveData(responseEntry.getKey(), responseEntry.getValue());
                }
            }

            mConnectorDatabank.closeDatabase();
        }
    }

    public Context getContext() {
        if (isInit()) {
            return mConfiguration.context;
        } else {
            return null;
        }
    }

}
