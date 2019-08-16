package com.allintask.lingdao.version;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.utils.FileDownloadUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class DownloadService extends Service {

    public static final String APP_DOWNLOAD_TYPE = "APP_DOWNLOAD_TYPE";
    public static final String APP_DOWNLOAD_PATH = "APP_DOWNLOAD_PATH";
    public static final String APP_LOCAL_PATH = "APP_LOCAL_PATH";
    public static final String APP_DOWNLOAD_TITLE = "APP_DOWNLOAD_TITLE";

    public static final int DOWNLOAD_APK = 0;
    public static final int DOWNLOAD_VOICE_DEMO = 1;
    public static final int DOWNLOAD_VOICE = 2;

    private int downloadType = -1;

    private FileDownloadUtils fileDownloadUtil;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private int oldPercent = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            downloadType = intent.getIntExtra(APP_DOWNLOAD_TYPE, DOWNLOAD_APK);
            String appDownloadPath = intent.getExtras().getString(APP_DOWNLOAD_PATH);
            String appLocalPath = intent.getExtras().getString(APP_LOCAL_PATH);
            String appDownloadTitle = intent.getExtras().getString(APP_DOWNLOAD_TITLE);

            Log.i("TanJiaJun", "onStartCommand:appDownloadPath = " + appDownloadPath);
            Log.i("TanJiaJun", "onStartCommand:appLocalPath = " + appLocalPath);
            Log.i("TanJiaJun", "onStartCommand:appDownloadTitle = " + appDownloadTitle);

            if (!TextUtils.isEmpty(appDownloadPath) && !TextUtils.isEmpty(appLocalPath)) {
                File file = new File(appLocalPath);
                if (file.exists()) {
                    file.delete();
                }
                startDownloadApp(appDownloadPath, appLocalPath, appDownloadTitle);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownloadApp(final String appDownloadPath, final String appLocalPath, final String appDownloadTitle) {
        AllintaskApplication.getInstance().setUpdateDownloading(true);

        if (downloadType == DOWNLOAD_APK) {
            builder = new NotificationCompat.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setContentTitle(appDownloadTitle).setSmallIcon(R.mipmap.ic_launcher);
        }

        NotifiHolder notifiHolder = new NotifiHolder();
        notifiHolder.setTotalByte(0);
        notifiHolder.setCurReadByte(0);
        notifiHolder.setCurProgress(0);
        notifiHolder.setCurDownloadSpeed(null);
        notifiHolder.setDownloadState(FileDownloadUtils.DOWNLOAD_FILE_START);

        fileDownloadUtil = new FileDownloadUtils(this, notifiHolder);
        fileDownloadUtil.setOnDownLoadListener(new FileDownloadUtils.OnDownLoadListener() {

            @Override
            public void onStop(NotifiHolder notifiHolder) {
                AllintaskApplication.getInstance().setUpdateDownloading(false);
                stopSelf();
            }

            @Override
            public void onStart(NotifiHolder notifiHolder) {
            }

            @Override
            public void onSpeedChange(NotifiHolder notifiHolder) {
            }

            @Override
            public void onLoading(NotifiHolder notifiHolder) {
                if (downloadType == DOWNLOAD_APK) {
                    updateProgress(notifiHolder);
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onComplete(NotifiHolder notifiHolder) {
                notifiHolder.setCurProgress(notifiHolder.getTotalByte());
                notifiHolder.setCurDownloadSpeed("0KB/s");

                //下载完成修改文件名，去掉dtmp
                File downloadFile = new File(appLocalPath);
                File localFile = null;
                if (downloadFile.exists()) {
                    String newFilename = downloadFile.getName().replace(".dtmp", "");
                    localFile = new File(downloadFile.getParent(), newFilename);
                    downloadFile.renameTo(localFile);
                }

                if (downloadType == DOWNLOAD_APK) {
                    Intent installIntent = new Intent();
                    installIntent.setAction(Intent.ACTION_VIEW);

                    Uri uri;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(AllintaskApplication.getInstance().getApplicationContext(), getApplicationContext().getPackageName() + ".provider", localFile);
                    } else {
                        uri = Uri.parse("file://" + localFile.getAbsolutePath());
                    }

                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent pIntent = PendingIntent.getActivity(DownloadService.this, notifiHolder.getNotifiID(), installIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentText(getString(R.string.app_download_success)).setProgress(0, 0, false);
                    builder.setContentIntent(pIntent);
                    notificationManager.notify(0, builder.build());

                    AllintaskApplication.getInstance().setUpdateDownloading(false);

                    if (localFile.exists()) {
                        installApp(uri);
                    }

                    // 超过APK文件时删除
                    if (null != localFile) {
                        File[] downloadFiles = localFile.getParentFile().listFiles();
                        if (null != downloadFiles && downloadFiles.length >= 5) {
                            Arrays.sort(downloadFiles, new CompratorByLastModified());
                            for (int i = 0; i < downloadFiles.length - 1; i++) {
                                File tempFile = downloadFiles[i];
                                if (tempFile.exists()) {
                                    tempFile.delete();
                                }
                            }
                        }
                    }
                }

                Intent intent = new Intent();

                switch (downloadType) {
                    case DOWNLOAD_APK:
                        intent.setAction(CommonConstant.ACTION_DOWNLOAD_APK_SUCCESS);
                        break;

                    case DOWNLOAD_VOICE_DEMO:
                        intent.setAction(CommonConstant.ACTION_DOWNLOAD_VOICE_DEMO_SUCCESS);
                        break;

                    case DOWNLOAD_VOICE:
                        intent.setAction(CommonConstant.ACTION_DOWNLOAD_VOICE_SUCCESS);
                        break;
                }

                sendBroadcast(intent);

                stopSelf();
            }

            @Override
            public void onPause(NotifiHolder notifiHolder) {

            }

        });

        fileDownloadUtil.downloadFile(appDownloadPath, appLocalPath);
    }

    private void updateProgress(NotifiHolder notifiHolder) {
        int totalByte = notifiHolder.getTotalByte();
        int progress = notifiHolder.getCurProgress();
        int percent = (int) (((double) (progress) / (double) totalByte) * 100);

        if (percent != oldPercent) {
            //"正在下载:" + progress + "%"
            builder.setContentText(this.getString(R.string.download_progress, percent)).setProgress(100, percent, false);
            //setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
            PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingintent);

            notificationManager.notify(0, builder.build());
        }

        oldPercent = percent;
    }

    private void installApp(final Uri appFileUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(appFileUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivity(intent);
    }

    class CompratorByLastModified implements Comparator<File> {

        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }

        public boolean equals(Object obj) {
            return true;
        }

    }

}
