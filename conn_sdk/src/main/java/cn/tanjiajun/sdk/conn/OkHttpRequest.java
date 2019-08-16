package cn.tanjiajun.sdk.conn;

import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络请求参数
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class OkHttpRequest {

    private String baseUrl;
    private String apiPath;
    private String requestID;
    private Map<String, List<String>> requestFormParamMap;
    private Map<String, List<File>> requestFileTypeParamMap;
    private Map<String, Object> additionalArgsMap;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setAPIPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getAPIPath() {
        return (this.apiPath != null) ? this.apiPath : "";
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestID() {
        return this.requestID;
    }

    public String getLocalRequestUrl() {
        StringBuilder localRequestUrl = new StringBuilder();
        localRequestUrl.append(baseUrl.replaceAll("/", "_"));
        localRequestUrl.append(apiPath.replaceAll("/", "_"));

        if (null != requestFormParamMap) {
            for (Map.Entry<String, List<String>> paramEntry : requestFormParamMap.entrySet()) {
                if (null != paramEntry) {
                    String paramKey = paramEntry.getKey();
                    List<String> paramValues = paramEntry.getValue();

                    if (null != paramValues) {
                        for (String value : paramValues) {
                            localRequestUrl.append("_");
                            localRequestUrl.append(paramKey);
                            localRequestUrl.append("_");
                            localRequestUrl.append(value);
                        }
                    } else {
                        localRequestUrl.append("_");
                        localRequestUrl.append(paramEntry.getKey());
                        localRequestUrl.append("_");
                        localRequestUrl.append("");
                    }
                }
            }
        }
        return localRequestUrl.toString();
    }

    public void addRequestFormParam(String key, String value) {
        List<String> values = new ArrayList<>();
        values.add(value);
        addRequestFormParam(key, values);
    }

    public void addRequestFormParam(String key, List<String> values) {
        if (null == requestFormParamMap) {
            requestFormParamMap = new HashMap<>();
        }

        if (null != key) {
            requestFormParamMap.put(key, values);
        }
    }

    public Object getRequestFormParam(String key) {
        if (null != key && null != requestFormParamMap) {
            return requestFormParamMap.get(key);
        } else {
            return null;
        }
    }

    public Map<String, List<String>> getRequestFormParamMap() {
        return this.requestFormParamMap;
    }

    public void addRequestFileTypeParam(String key, File value) {
        List<File> values = new ArrayList<>();
        values.add(value);
        addRequestFileTypeParam(key, values);
    }

    public void addRequestFileTypeParam(String key, List<File> values) {
        if (null == requestFileTypeParamMap) {
            requestFileTypeParamMap = new HashMap<>();
        }

        if (null != key) {
            requestFileTypeParamMap.put(key, values);
        }
    }

    public Object getRequestFileTypeParam(String key) {
        if (null != key && null != requestFileTypeParamMap) {
            return requestFileTypeParamMap.get(key);
        } else {
            return null;
        }
    }

    public Map<String, List<File>> getRequestFileTypeParamMap() {
        return this.requestFileTypeParamMap;
    }

    public void addAdditionalArgs(String key, Object value) {
        if (null == additionalArgsMap) {
            additionalArgsMap = new HashMap<>();
        }

        if (null != key) {
            additionalArgsMap.put(key, value);
        }
    }

    public Object getAdditionalArgsValue(String key) {
        if (null != key && null != additionalArgsMap) {
            return additionalArgsMap.get(key);
        } else {
            return null;
        }
    }

    public void setAdditionalArgsMap(Map<String, Object> additionalArgsMap) {
        this.additionalArgsMap = additionalArgsMap;
    }

    public Map<String, Object> getAdditionalArgsMap() {
        return this.additionalArgsMap;
    }

    public String getRequestParamString() {
        StringBuilder requestParamStringBuilder = new StringBuilder();

        if (null != requestFormParamMap) {
            int i = 0;

            for (Map.Entry<String, List<String>> paramEntry : requestFormParamMap.entrySet()) {
                try {
                    requestParamStringBuilder.append(paramEntry.getKey()).append("=").append(URLEncoder.encode(paramEntry.getValue().get(0).trim(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (i < requestFormParamMap.size() - 1) {
                    requestParamStringBuilder.append("&");
                }

                i++;
            }
        }
        return requestParamStringBuilder.toString();
    }

    public void showHttpRequestLog() {
        String tab = "OkHttpRequest";
        Log.i(tab, "OKHttpRequest URL: " + baseUrl + apiPath);

        if (null != requestFormParamMap) {
            for (Map.Entry<String, List<String>> paramEntry : requestFormParamMap.entrySet()) {
                Log.i(tab, "OKHttpRequest Params : " + paramEntry.getKey() + " = " + paramEntry.getValue());
            }
        }

        if (null != requestFileTypeParamMap) {
            for (Map.Entry<String, List<File>> paramEntry : requestFileTypeParamMap.entrySet()) {
                Log.i(tab, "OKHttpRequest Params : " + paramEntry.getKey() + " = " + paramEntry.getValue());
            }
        }
    }

}
