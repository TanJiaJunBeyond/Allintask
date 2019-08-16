package com.allintask.lingdao.presenter;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.utils.CommonJSONParser;
import com.allintask.lingdao.utils.EaseUI;
import com.allintask.lingdao.utils.SignUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.IBaseView;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Call;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tanjiajun.sdk.common.utils.NetworkUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpClientManager;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;
import cn.tanjiajun.sdk.conn.interfaces.IResultCallback;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */

public abstract class BasePresenter<V extends IBaseView> {

    private static final String TAG = "BasePresenter";

    private Reference<V> mView;
    private Dialog dialog;

    private Call mCall;

    public void attachView(V view) {
        this.mView = new WeakReference<>(view);
    }

    public void detachView() {
        if (null != mView) {
            mView.clear();
            mView = null;
        }
    }

    public boolean isViewAttached() {
        return null != this.mView && this.mView.get() != null;
    }

    public V getView() {
        if (null != mView) {
            return this.mView.get();
        }
        return null;
    }

    protected void addRequestAsyncTaskForJsonNoProgress(final boolean isNeedLocalData, final String method, final OkHttpRequest request) {
        if (!isNeedLocalData && !isNetworkAvailable()) {
            if (null != getView()) {
                getView().showNoNetworkView();
            }
            return;
        }

        String accessToken = UserPreferences.getInstance().getAccessToken();
        runRequestAsyncTaskForJson(method, request, accessToken, false, false, null);
    }

    protected void addRequestAsyncTaskForJson(final boolean isNeedLocalData, final String method, final OkHttpRequest request) {
        if (!isNeedLocalData && !isNetworkAvailable()) {
            if (null != getView()) {
                getView().showNoNetworkView();
            }
            return;
        }

        if (null != request) {
            String accessToken = UserPreferences.getInstance().getAccessToken();
            runRequestAsyncTaskForJson(method, request, accessToken, false, true, null);

            if (getView() != null) {
                getView().showProgressDialog();
            }
        }
    }

    protected void addJsonStringRequestAsyncTaskForJson(final boolean isNeedLocalData, final String method, final OkHttpRequest request) {
        if (!isNeedLocalData && !isNetworkAvailable()) {
            if (null != getView()) {
                getView().showNoNetworkView();
            }
            return;
        }

        if (null != request) {
            String accessToken = UserPreferences.getInstance().getAccessToken();
            runRequestAsyncTaskForJson(method, request, accessToken, true, true, null);

            if (getView() != null) {
                getView().showProgressDialog();
            }
        }
    }

    protected void addJsonStringRequestAsyncTaskForJsonNoProgress(final boolean isNeedLocalData, final String method, final OkHttpRequest request) {
        if (!isNeedLocalData && !isNetworkAvailable()) {
            if (null != getView()) {
                getView().showNoNetworkView();
            }
            return;
        }

        if (null != request) {
            String accessToken = UserPreferences.getInstance().getAccessToken();
            runRequestAsyncTaskForJson(method, request, accessToken, true, false, null);
        }
    }

    protected void addRequestAsyncTaskForJson(final boolean isNeedLocalData, final String method, final OkHttpRequest request, final ITaskResultCallback iTaskResultCallback) {
        if (!isNeedLocalData && !isNetworkAvailable()) {
            if (null != getView()) {
                getView().showNoNetworkView();
            }
            return;
        }

        if (null != request) {
            String accessToken = UserPreferences.getInstance().getAccessToken();
            runRequestAsyncTaskForJson(method, request, accessToken, false, true, iTaskResultCallback);

            if (getView() != null) {
                getView().showProgressDialog();
            }
        }
    }

    private void runRequestAsyncTaskForJson(final String method, final OkHttpRequest request, String token, boolean isJsonString, final boolean isShowProgressDialog, final ITaskResultCallback iTaskResultCallback) {
        OkHttpRequestOptions okHttpRequestOptions = new OkHttpRequestOptions.Builder().method(method).build();

        final String requestId = request.getRequestID();

        request.setBaseUrl(ServiceAPIConstant.API_BASE_URL);

        if (request.getAPIPath().equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET)) {
            int userId = UserPreferences.getInstance().getUserId();

            Map<String, List<String>> requestFormParamMap = new HashMap<>();
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(userId));
            requestFormParamMap.put(ApiKey.COMMON_ID, list);

            String sign = SignUtils.getSign(requestFormParamMap, false);

            if (!TextUtils.isEmpty(sign)) {
                request.addRequestFormParam(ApiKey.COMMON_SIGN, sign);
            }
        } else if (request.getAPIPath().equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_VOICE_UPLOAD)) {
            int userId = UserPreferences.getInstance().getUserId();

            Map<String, List<String>> requestFormParamMap = new HashMap<>();
            List<String> valueList = new ArrayList<>();
            valueList.add(String.valueOf(userId));
            requestFormParamMap.put(ApiKey.COMMON_USER_ID, valueList);
            String sign = SignUtils.getSign(requestFormParamMap, false);

            if (!TextUtils.isEmpty(sign)) {
                request.addRequestFormParam(ApiKey.COMMON_SIGN, sign);
            }
        } else if (isJsonString) {
            request.addRequestFormParam(ApiKey.COMMON_ACCESS_TOKEN, "Bearer " + token);

            long timestamp = System.currentTimeMillis();
            request.addRequestFormParam(ApiKey.COMMON_TIMESTAMP, String.valueOf(timestamp));

            Map<String, List<String>> requestFormParamMap = request.getRequestFormParamMap();
            String sign = SignUtils.getSign(requestFormParamMap, true);

            if (!TextUtils.isEmpty(sign)) {
                request.addRequestFormParam(ApiKey.COMMON_PARAM, sign);
            }
        } else {
            Map<String, List<String>> requestFormParamMap = request.getRequestFormParamMap();
            String sign = SignUtils.getSign(requestFormParamMap, false);

            if (!TextUtils.isEmpty(sign)) {
                request.addRequestFormParam(ApiKey.COMMON_SIGN, sign);
            }
        }

        request.showHttpRequestLog();
        OkHttpClientManager.getInstance().commitRequestTask(request, okHttpRequestOptions, token, isJsonString, new IResultCallback() {
            @Override
            public void onCall(Call call) {
                mCall = call;
            }

            @Override
            public void onResponse(String response) {
                Log.i("ServerResponse", "response = " + response);

                if (isShowProgressDialog && getView() != null) {
                    getView().dismissProgressDialog();
                }

                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response);

                        boolean success = TypeUtils.getBoolean(jsonObject.getBoolean(ApiKey.COMMON_SUCCESS), false);
                        int errorCode = TypeUtils.getInteger(jsonObject.getInteger(ApiKey.COMMON_ERROR_CODE), 0);
                        String errorMessage = jsonObject.getString(ApiKey.COMMON_ERROR_MESSAGE);
                        String data = jsonObject.getString(ApiKey.COMMON_DATA);

                        String runningActivityName = getRunningActivityName();

                        if (errorCode == 40001) {
                            if (null != getView()) {
                                getView().showToast("登录状态失效，请重新登录");
                            }

                            if (!TextUtils.isEmpty(runningActivityName) && !getRunningActivityName().equals("com.allintask.lingdao.ui.activity.user.LoginActivity")) {
                                UserPreferences.getInstance().setUserBean(null);
                                EaseUI.getInstance().getNotifier().reset();
                                EMClient.getInstance().logout(true);

                                if (null != getView()) {
                                    if (getView().getParentContext() instanceof MainActivity) {
                                        ((MainActivity) getView().getParentContext()).initUI();
                                    } else {
                                        Intent intent = new Intent(getView().getParentContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getView().getParentContext().startActivity(intent);
                                    }
                                }
                            }
                        } else if (errorCode == 500) {
                            onResponseAsyncTaskRender(requestId, false, errorCode, "服务器忙，请稍后重试", null);
                        } else {
                            onResponseAsyncTaskRender(requestId, success, errorCode, errorMessage, data);
                        }
                    } catch (Exception e) {
                        if (getView() != null) {
                            getView().showToast("请求超时");
                        }

                        e.printStackTrace();
                    }
                } else {
//                    getView().showToast(AllintaskApplication.getInstance().getResources().getString(R.string.error_request));
                }
            }

            @Override
            public void onFailure(String error) {
                if (null != getView()) {
                    getView().dismissProgressDialog();
                    onResponseAsyncTaskRender(requestId, false, -1, null, null);
                    getView().showToast(AllintaskApplication.getInstance().getResources().getString(R.string.error_network));
                    Log.i("TanJiaJun", "error:" + error);
                }
            }
        });
    }

    protected boolean isNetworkAvailable() {
        boolean isNetworkAvailable = false;

        if (NetworkUtils.isNetworkAvaliable(getView().getParentContext())) {
            isNetworkAvailable = true;
        } else {
            getView().showToast(getView().getParentContext().getString(R.string.error_network));
        }
        return isNetworkAvailable;
    }

    private Map<String, Object> convertStringToMap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            return new CommonJSONParser().parse(str);
        }
    }

    public void cancelCall() {
        if (null != mCall) {
            mCall.cancel();
        }
    }

    private String getRunningActivityName() {
        String runningActivityName = null;

        if (null != getView()) {
            ActivityManager activityManager = (ActivityManager) getView().getParentContext().getSystemService(Context.ACTIVITY_SERVICE);

            if (null != activityManager) {
                runningActivityName = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            }
        }
        return runningActivityName;
    }

    public interface ITaskResultCallback {

        void taskResultCallback(final String requestId, final boolean success, final int errorCode, final String errorMessage, final String data);

    }

    protected abstract void onResponseAsyncTaskRender(final String requestId, final boolean success, final int errorCode, final String errorMessage, final String data);

}
