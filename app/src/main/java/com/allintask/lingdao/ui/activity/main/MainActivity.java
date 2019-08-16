package com.allintask.lingdao.ui.activity.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.bean.user.AppUpdateBean;
import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.helper.EMChatHelper;
import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.main.MainPresenter;
import com.allintask.lingdao.ui.activity.CheckPermissionsActivity;
import com.allintask.lingdao.ui.activity.user.LoginActivity;
import com.allintask.lingdao.ui.fragment.main.DemandManagementFragment;
import com.allintask.lingdao.ui.fragment.main.MineFragment;
import com.allintask.lingdao.ui.fragment.main.RecommendFragment;
import com.allintask.lingdao.ui.fragment.main.MessageFragment;
import com.allintask.lingdao.ui.fragment.main.ServiceManagementFragment;
import com.allintask.lingdao.utils.NotificationUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.version.DownloadService;
import com.allintask.lingdao.view.main.IMainView;
import com.allintask.lingdao.widget.AppUpdateDialog;
import com.allintask.lingdao.widget.reveallayout.CircularRevealButton;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.chat.EMClient;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.NetworkUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.custom.dialog.BasicDialog;
import cn.tanjiajun.sdk.component.util.DialogUtils;

/**
 * 主页面
 * rebuild by TanJiaJun on 2018/1/3.
 */

public class MainActivity extends CheckPermissionsActivity implements IMainView {

    @BindView(R.id.ll_content)
    LinearLayout contentLL;
    @BindView(R.id.rl_message)
    RelativeLayout messageRL;
    @BindView(R.id.crb_message)
    CircularRevealButton messageCRB;
    @BindView(R.id.unread_message_number)
    TextView unreadMessageNumberTv;
    @BindView(R.id.crb_demand_management)
    CircularRevealButton demandManagementCRB;
    @BindView(R.id.crb_recommend)
    CircularRevealButton recommendCRB;
    @BindView(R.id.crb_service_management)
    CircularRevealButton serviceManagementCRB;
    @BindView(R.id.crb_mine)
    CircularRevealButton mineCRB;

    private MainPresenter mainPresenter;

    private String fragmentName;
    private int fragmentStatus;

    private MainActivityBroadcastReceiver mainActivityBroadcastReceiver;

    private BasicDialog openNotificationDialog;

    private boolean isForcedUpdate = false;
    private AppUpdateDialog appUpdateDialog;

    private long preBackTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter CreatePresenter() {
        mainPresenter = new MainPresenter();
        return mainPresenter;
    }

    @Override
    protected void init() {
        registerMainActivityReceiver();
        initUI();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
    }

    public void initUI() {
        boolean isLogin = UserPreferences.getInstance().isLogin();
        Intent intent = getIntent();

        if (isLogin) {
            if (null != intent) {
                fragmentName = intent.getStringExtra(CommonConstant.EXTRA_FRAGMENT_NAME);
                fragmentStatus = intent.getIntExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_DEFAULT);

                if (!TextUtils.isEmpty(fragmentName)) {
                    if (fragmentName.equals(CommonConstant.RECOMMEND_FRAGMENT)) {
                        selectRecommend(fragmentStatus);
                    } else if (fragmentName.equals(CommonConstant.DEMAND_MANAGEMENT_FRAGMENT)) {
                        selectDemandManagement(fragmentStatus);
                    } else if (fragmentName.equals(CommonConstant.MESSAGE_FRAGMENT)) {
                        selectMessage();
                    } else if (fragmentName.equals(CommonConstant.SERVICE_MANAGEMENT_FRAGMENT)) {
                        selectServiceManagement(fragmentStatus);
                    } else if (fragmentName.equals(CommonConstant.MINE_FRAGMENT)) {
                        selectMine();
                    }
                } else {
                    selectRecommend(CommonConstant.RECOMMEND_STATUS_DEFAULT);
                }
            }
        } else {
            Intent loginIntent = new Intent(getParentContext(), LoginActivity.class);
            startActivity(loginIntent);

            finish();
        }
    }

    private void initData() {
        if (null != openNotificationDialog && openNotificationDialog.isShowing()) {
            openNotificationDialog.dismiss();
        }

        if (!NotificationUtils.isNotificationEnabled(getParentContext())) {
            openNotificationDialog = DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "请开启应用通知", getString(R.string.go_to_set), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        boolean isLogin = UserPreferences.getInstance().isLogin();
        mainPresenter.appUpdateRequest(getParentContext());

        if (isLogin) {
            initEMChat();
            updateUnreadMessageNumber();

            mainPresenter.fetchPersonalInfoRequest();
            mainPresenter.fetchSettingRequest();

            double longitude = AllintaskApplication.getInstance().getLongitude();
            double latitude = AllintaskApplication.getInstance().getLatitude();
            String location = AllintaskApplication.getInstance().getLocation();

            mainPresenter.updateLoginTimeAndLocationRequest(longitude, latitude, null, null, location);
        }
    }

    private void selectRecommend(int recommendStatus) {
        messageCRB.setonSelected(false, true);
        demandManagementCRB.setonSelected(false, true);
        recommendCRB.setonSelected(true, true);
        serviceManagementCRB.setonSelected(false, true);
        mineCRB.setonSelected(false, true);

        if (recommendStatus != CommonConstant.RECOMMEND_STATUS_DEFAULT) {
            if (null != mFragmentMap) {
                if (mFragmentMap.containsKey(DemandManagementFragment.class.getName())) {
                    ((RecommendFragment) mFragmentMap.get(RecommendFragment.class.getName())).selectLobby(recommendStatus);
                }
            }
        }

        openFragment(RecommendFragment.class.getName(), null);
    }

    private void selectDemandManagement(int demandStatus) {
        messageCRB.setonSelected(false, true);
        demandManagementCRB.setonSelected(true, true);
        recommendCRB.setonSelected(false, true);
        serviceManagementCRB.setonSelected(false, true);
        mineCRB.setonSelected(false, true);

        if (demandStatus != CommonConstant.DEMAND_STATUS_DEFAULT) {
            if (null != mFragmentMap) {
                if (mFragmentMap.containsKey(DemandManagementFragment.class.getName())) {
                    ((DemandManagementFragment) mFragmentMap.get(DemandManagementFragment.class.getName())).setViewPagerCurrentItem(demandStatus);
                    openFragment(DemandManagementFragment.class.getName(), null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConstant.EXTRA_DEMAND_STATUS, demandStatus);
                    openFragment(DemandManagementFragment.class.getName(), bundle);
                }
            }
        } else {
            openFragment(DemandManagementFragment.class.getName(), null);
        }

    }

    private void selectMessage() {
        messageCRB.setonSelected(true, true);
        demandManagementCRB.setonSelected(false, true);
        recommendCRB.setonSelected(false, true);
        serviceManagementCRB.setonSelected(false, true);
        mineCRB.setonSelected(false, true);

        openFragment(MessageFragment.class.getName(), null);
    }

    private void selectServiceManagement(int serviceStatus) {
        messageCRB.setonSelected(false, true);
        demandManagementCRB.setonSelected(false, true);
        recommendCRB.setonSelected(false, true);
        serviceManagementCRB.setonSelected(true, true);
        mineCRB.setonSelected(false, true);

        if (serviceStatus != CommonConstant.SERVICE_STATUS_DEFAULT) {
            if (null != mFragmentMap) {
                if (mFragmentMap.containsKey(ServiceManagementFragment.class.getName())) {
                    ((ServiceManagementFragment) mFragmentMap.get(ServiceManagementFragment.class.getName())).setViewPagerCurrentItem(serviceStatus);
                    openFragment(ServiceManagementFragment.class.getName(), null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConstant.EXTRA_SERVICE_STATUS, serviceStatus);
                    openFragment(ServiceManagementFragment.class.getName(), bundle);
                }
            }
        } else {
            openFragment(ServiceManagementFragment.class.getName(), null);
        }

    }

    private void selectMine() {
        messageCRB.setonSelected(false, true);
        demandManagementCRB.setonSelected(false, true);
        recommendCRB.setonSelected(false, true);
        serviceManagementCRB.setonSelected(false, true);
        mineCRB.setonSelected(true, true);

        openFragment(MineFragment.class.getName(), null);
    }

    private void initEMChat() {
        int userId = UserPreferences.getInstance().getUserId();
        String userIdStr = String.valueOf(userId);

        String emChatPassword = UserPreferences.getInstance().getEMChatPassword();
        EMClient.getInstance().login(userIdStr, emChatPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("TanJiaJun", "onSuccess");
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
            }

            @Override
            public void onProgress(int i, String s) {
                Log.i("TanJiaJun", "onProgress:" + s);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("TanJiaJun", "onError:" + s);
            }
        });
    }

    private void showAppUpdateDialog(String content, final boolean isForcedUpdate, final String versionName, String updateContent, String url, final boolean isDownloading) {
        appUpdateDialog = new AppUpdateDialog(getParentContext(), content, isForcedUpdate, versionName, updateContent, url, isDownloading);

        if (isForcedUpdate) {
            appUpdateDialog.setCancelable(false);
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;

        Window window = appUpdateDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        appUpdateDialog.show();
        appUpdateDialog.setOnClickListener(new AppUpdateDialog.OnClickListener() {
            @Override
            public void onClick(String content, final String url) {
                boolean isUpdateDownloading = AllintaskApplication.getInstance().isUpdateDownloading();

                if (!isUpdateDownloading) {
                    if (content.equals(getString(R.string.install_now))) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri uri = FileProvider.getUriForFile(getParentContext(), getApplicationContext().getPackageName() + ".provider", getAppUpdateAPKFile("Allintask", versionName));
                            installApp(uri);
                        } else {
                            File file = getAppUpdateAPKFile("Allintask", versionName);

                            if (file.exists()) {
                                Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                                installApp(uri);
                            }
                        }
                    } else if (content.equals(getString(R.string.update_at_once)) && !TextUtils.isEmpty(url)) {
                        int networkState = NetworkUtils.getNetworkState(getParentContext());
                        switch (networkState) {
                            case NetworkUtils.TYPE_NULL:
                                showToast("网络未连接，请检查网络");
                                break;
                            case NetworkUtils.TYPE_MOBILE:
                                BasicDialog.Builder alertDialogBuilder = new BasicDialog.Builder(getParentContext());
                                alertDialogBuilder.setTitle("提示");
                                alertDialogBuilder.setMessage(R.string.isMobileNetworkTips);
                                alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDownloadAPK(getString(R.string.app_name), versionName, url);
                                        dialog.dismiss();
                                    }
                                });
                                alertDialogBuilder.create().show();
                                break;
                            case NetworkUtils.TYPE_WIFI:
                                startDownloadAPK(getString(R.string.app_name), versionName, url);
                                break;
                            default:
                                break;
                        }
                    }

                    if (null != appUpdateDialog) {
                        appUpdateDialog.setDownloadingAPK(isDownloading);
                    }

                    if (null != appUpdateDialog && appUpdateDialog.isShowing() && !isForcedUpdate) {
                        appUpdateDialog.dismiss();
                    }
                } else {
                    if (null != appUpdateDialog) {
                        appUpdateDialog.setDownloadingAPK(true);
                    }
                }
            }
        });

        appUpdateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (null != appUpdateDialog && appUpdateDialog.isShowing() && !isForcedUpdate) {
                        appUpdateDialog.dismiss();
                    } else {
                        finish();
                    }
                }
                return false;
            }
        });
    }

    private void startDownloadAPK(String appName, String versionName, String downloadUrl) {
        showToast("开始下载，下载结束自动安装，请稍后");
        startDownloadService(downloadUrl, appName, getDesitinationPath("Allintask", versionName));
        boolean isUpdateDownloading = AllintaskApplication.getInstance().isUpdateDownloading();

        if (null != appUpdateDialog) {
            appUpdateDialog.setDownloadingAPK(isUpdateDownloading);
        }
    }

    private void startDownloadService(String downloadURL, String title, String desitinationPath) {
        Intent updateIntent = new Intent(getParentContext(), DownloadService.class);
        updateIntent.setAction("com.allintask.lingdao.version.DownloadService");
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TYPE, DownloadService.DOWNLOAD_APK);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_PATH, downloadURL);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TITLE, title);
        updateIntent.putExtra(DownloadService.APP_LOCAL_PATH, desitinationPath);
        startService(updateIntent);
    }

    private File getAppUpdateCacheDir() {
        return FileUtils.getCacheDir(getParentContext(), "AppUpdateCache");
    }

    private File getAppUpdateAPKFile(String appName, String versionName) {
        return new File(getAppUpdateCacheDir(), File.separator + appName + "_" + versionName + "_" + AnalyticsConfig.getChannel(getParentContext()) + ".apk");
    }

    private File getAppUpdateAPKDownloadTemporaryFile(String appName, String versionName) {
        return new File(getAppUpdateCacheDir(), File.separator + appName + "_" + versionName + "_" + AnalyticsConfig.getChannel(getParentContext()) + ".dtmp.apk");
    }

    private boolean isAppFileExistent(String appName, String versionName) {
        boolean isAppFileExistent = false;
        File appUpdateAPKFile = getAppUpdateAPKFile(appName, versionName);
        if (appUpdateAPKFile.exists()) {
            isAppFileExistent = true;
        }
        return isAppFileExistent;
    }

    private String getDesitinationPath(String appName, String versionName) {
        File appUpdateAPKDownloadTemporaryFile = getAppUpdateAPKDownloadTemporaryFile(appName, versionName);
        return appUpdateAPKDownloadTemporaryFile.getPath();
    }

    private void installApp(final Uri appFileUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(appFileUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    /**
     * update unread message count
     */
    public void updateUnreadMessageNumber() {
        int count = getUnreadMsgCountTotal();

        if (count > 0) {
            unreadMessageNumberTv.setText(String.valueOf(count));
            unreadMessageNumberTv.setVisibility(View.VISIBLE);
        } else {
            unreadMessageNumberTv.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.crb_recommend, R.id.crb_demand_management, R.id.rl_message, R.id.crb_service_management, R.id.crb_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crb_recommend:
                selectRecommend(CommonConstant.RECOMMEND_STATUS_DEFAULT);
                break;

            case R.id.crb_demand_management:
                selectDemandManagement(CommonConstant.DEMAND_STATUS_DEFAULT);
                break;

            case R.id.rl_message:
                selectMessage();
                break;

            case R.id.crb_service_management:
                selectServiceManagement(CommonConstant.SERVICE_STATUS_DEFAULT);
                break;

            case R.id.crb_mine:
                selectMine();
                break;
        }
    }

    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {

        }
    };

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerMainActivityReceiver() {
        mainActivityBroadcastReceiver = new MainActivityBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_REFRESH_FRAGMENT);
        intentFilter.addAction(CommonConstant.ACTION_LOGOUT);
        registerReceiver(mainActivityBroadcastReceiver, intentFilter);
    }

    private class MainActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (!TextUtils.isEmpty(action)) {
                    if (action.equals(CommonConstant.ACTION_REFRESH_FRAGMENT)) {
                        String refreshFragment = intent.getStringExtra(CommonConstant.EXTRA_REFRESH_FRAGMENT);

                        if (!TextUtils.isEmpty(refreshFragment)) {
                            updateUnreadMessageNumber();

                            if (refreshFragment.equals(CommonConstant.MESSAGE_FRAGMENT)) {
                                if (null != mFragmentMap) {
                                    if (mFragmentMap.containsKey(MessageFragment.class.getName())) {
                                        ((MessageFragment) mFragmentMap.get(MessageFragment.class.getName())).refresh();
                                    }
                                }
                            }
                        }
                    }

                    if (action.equals(CommonConstant.ACTION_LOGOUT)) {
                        initUI();
                    }
                }
            }
        }

    }

    @Override
    public void onShowPersonalInformationBean(PersonalInformationBean personalInformationBean) {
        if (null != personalInformationBean) {
            final String realName = TypeUtils.getString(personalInformationBean.realName, "");
            String avatarImageUrl = TypeUtils.getString(personalInformationBean.avatarImageUrl, "");

            UserPreferences.getInstance().setNickname(realName);
            String headPortraitUrl = null;

            if (!TextUtils.isEmpty(avatarImageUrl)) {
                headPortraitUrl = "https:" + avatarImageUrl;
            }

            UserPreferences.getInstance().setHeadPortraitUrl(headPortraitUrl);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    EMClient.getInstance().updateCurrentUserNick(realName);
                }
            });

            thread.start();
        }
    }

    @Override
    public void onShowAppUpdateBean(AppUpdateBean appUpdateBean) {
        boolean isUpdateDownloading = AllintaskApplication.getInstance().isUpdateDownloading();

        if (null != appUpdateBean) {
            int isShow = TypeUtils.getInteger(appUpdateBean.isShow, CommonConstant.NOT_SHOW_APP_UPDATE_DIALOG);
            int tempIsForceUpdate = TypeUtils.getInteger(appUpdateBean.isForceUpdate, CommonConstant.NOT_FORCE_UPDATE);
            String newVersion = TypeUtils.getString(appUpdateBean.newVersion, "");
            String tip = TypeUtils.getString(appUpdateBean.tip, "");
            String url = TypeUtils.getString(appUpdateBean.url, "");

            if (isShow == CommonConstant.SHOW_APP_UPDATE_DIALOG) {
                if (tempIsForceUpdate == CommonConstant.NOT_FORCE_UPDATE) {
                    isForcedUpdate = false;
                } else if (tempIsForceUpdate == CommonConstant.FORCE_UPDATE) {
                    isForcedUpdate = true;
                }

                if (null != appUpdateDialog && appUpdateDialog.isShowing()) {
                    appUpdateDialog.dismiss();
                }

                if (isAppFileExistent("Allintask", newVersion)) {
                    showAppUpdateDialog(getString(R.string.install_now), isForcedUpdate, newVersion, tip, url, isUpdateDownloading);
                } else {
                    showAppUpdateDialog(getString(R.string.update_at_once), isForcedUpdate, newVersion, tip, url, isUpdateDownloading);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curBackTime = System.currentTimeMillis();

            if (curBackTime - preBackTime >= 2000) {
                preBackTime = curBackTime;
                showToast(getString(R.string.press_again_to_close_application));
            } else {
                AllintaskApplication.IS_STARTED = false;

                // TODO 清空所有Activity
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (null != intent) {
            boolean isLogout = intent.getBooleanExtra(CommonConstant.ACTION_LOGOUT, false);
            fragmentName = intent.getStringExtra(CommonConstant.EXTRA_FRAGMENT_NAME);
            fragmentStatus = intent.getIntExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_DEFAULT);

            if (isLogout) {
                boolean isLogin = UserPreferences.getInstance().isLogin();

                if (!isLogin) {
                    Intent loginIntent = new Intent(getParentContext(), LoginActivity.class);
                    startActivity(loginIntent);

                    finish();
                }
            }

            if (!TextUtils.isEmpty(fragmentName)) {
                if (fragmentName.equals(CommonConstant.RECOMMEND_FRAGMENT)) {
                    selectRecommend(fragmentStatus);
                } else if (fragmentName.equals(CommonConstant.DEMAND_MANAGEMENT_FRAGMENT)) {
                    selectDemandManagement(fragmentStatus);
                } else if (fragmentName.equals(CommonConstant.MESSAGE_FRAGMENT)) {
                    selectMessage();
                } else if (fragmentName.equals(CommonConstant.SERVICE_MANAGEMENT_FRAGMENT)) {
                    selectServiceManagement(fragmentStatus);
                } else if (fragmentName.equals(CommonConstant.MINE_FRAGMENT)) {
                    selectMine();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        initData();

        // unregister this event listener when this activity enters the
        // background
        EMChatHelper emChatHelper = EMChatHelper.getInstance();
        emChatHelper.pushActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EMClient.getInstance().removeClientListener(clientListener);
        EMChatHelper emChatHelper = EMChatHelper.getInstance();
        emChatHelper.popActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (null != mainActivityBroadcastReceiver) {
            unregisterReceiver(mainActivityBroadcastReceiver);
        }

        super.onDestroy();
    }

}
