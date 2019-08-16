package com.allintask.lingdao;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.allintask.lingdao.helper.EMChatHelper;
import com.allintask.lingdao.constant.CommonConstant;

import com.allintask.lingdao.network.NetworkDetectionService;
import com.allintask.lingdao.network.NetworkTrafficDetector;
import com.allintask.lingdao.utils.DESUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import cn.tanjiajun.sdk.common.utils.CrashHandler;
import cn.tanjiajun.sdk.component.custom.image.imageloader.ImageLoaderManager;
import cn.tanjiajun.sdk.conn.OkHttpClientConfiguration;
import cn.tanjiajun.sdk.conn.OkHttpClientManager;

public class AllintaskApplication extends MultiDexApplication {

    private static final boolean IS_OPEN_NETWORK_DETECTION = false;
    public static boolean IS_STARTED = false;

    private static AllintaskApplication allintaskApplication;

    private LocalBroadcastManager localBroadcastManager;

    private boolean isUpdateDownloading = false;

    // 声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    // 声明定位回调监听器
    public AMapLocationListener mLocationListener;
    // 声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private double longitude;
    private double latitude;
    private String location;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化OkHttpClientManager
        OkHttpClientConfiguration configuration = OkHttpClientConfiguration.createDefault(this);
        OkHttpClientManager.getInstance().init(configuration);

        // 初始化ImageLoader
        ImageLoaderManager imageLoaderManager = new ImageLoaderManager();
        imageLoaderManager.init(this);

        //调试模式下开启异常捕获模式
        if (CommonConstant.IS_DEBUG_MODE) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }

        allintaskApplication = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(allintaskApplication);

        //init demo helper
        EMChatHelper.getInstance().init(allintaskApplication);

        Config.DEBUG = false;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);

        PlatformConfig.setWeixin(CommonConstant.WECHAT_APP_ID, CommonConstant.WECHAT_APP_SECRET);
        PlatformConfig.setQQZone(CommonConstant.QQ_APP_ID, CommonConstant.QQ_APP_SECRET);
//        PlatformConfig.setSinaWeibo("11111", "11111", "1111111");

        if (IS_OPEN_NETWORK_DETECTION) {
            Intent service = new Intent();
            service.setClass(this, NetworkDetectionService.class);
            startService(service);
        } else {
            NetworkTrafficDetector.getInstance().detect();
        }

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        // 可在其中解析amapLocation获取相应内容。
                        longitude = aMapLocation.getLongitude();
                        latitude = aMapLocation.getLatitude();

                        String province = aMapLocation.getProvince();
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String street = aMapLocation.getStreet();
                        location = province + city + district + street;
                    } else {
                        // 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        Log.i("TanJiaJun", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    }
                }
            }
        };

        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());

        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);

        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        // 设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        // 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        // 启动定位
        mLocationClient.startLocation();

        UMConfigure.init(getApplicationContext(), 0, null);
    }

    public static AllintaskApplication getInstance() {
        return allintaskApplication;
    }

    public LocalBroadcastManager getLocalBroadcastManager() {
        return localBroadcastManager;
    }

    // 语音路径
    public String getVoiceFilePath() {
        return getInstance().getFilesDir().toString() + "/Allintask/voice/";
    }

    public String getAPKFileAddress() {
        return getInstance().getFilesDir().toString() + "/Allintask/apk/";
    }

    private static String getSignNumber(Context context) {
        try {
            String pkName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkName, 64);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signNumber = getSignNumber(sign.toByteArray());
            return signNumber;
        } catch (Exception var6) {
            var6.printStackTrace();
            return "";
        }
    }

    private static String getSignNumber(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            return cert.getSerialNumber().toString();
        } catch (CertificateException var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public String getSharedPreferencesKey() {
        String key = DESUtils.encode(CommonConstant.DES_KEY, getSignNumber(allintaskApplication));

        if (!TextUtils.isEmpty(key)) {
            return key;
        } else {
            return null;
        }
    }

    public boolean isUpdateDownloading() {
        return isUpdateDownloading;
    }

    public void setUpdateDownloading(boolean isUpdateDownloading) {
        this.isUpdateDownloading = isUpdateDownloading;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLocation() {
        return location;
    }

}
