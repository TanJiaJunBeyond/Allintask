package cn.tanjiajun.sdk.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class NetworkUtils {

    public static final int TYPE_NULL = 0;
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_WIFI = 2;

    /**
     * 判断当前网络是否可用
     *
     * @param context 上下文环境
     * @return true表示网络可用
     * @since 0.0.1
     */
    public static boolean isNetworkAvaliable(Context context) {
        boolean isNetworkAvaliable = false;
        if (null != context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                isNetworkAvaliable = true;
            } else {
                isNetworkAvaliable = false;
            }
        }
        return isNetworkAvaliable;
    }

    /**
     * 获得当前网络状态
     *
     * @param context 上下文环境
     * @return 网络状态
     * @since 0.0.1
     */
    public static int getNetworkState(Context context) {
        int networkState = 0;
        if (null != context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE:
                        networkState = TYPE_MOBILE;
                        break;

                    case ConnectivityManager.TYPE_WIFI:
                        networkState = TYPE_WIFI;
                        break;

                    default:
                        networkState = TYPE_NULL;
                        break;
                }
            } else {
                networkState = TYPE_NULL;
            }
        }
        return networkState;
    }

}
