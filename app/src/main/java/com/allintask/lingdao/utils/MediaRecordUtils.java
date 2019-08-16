package com.allintask.lingdao.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;

import java.io.File;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public class MediaRecordUtils {

    public static final int CODE_PERMISSION_FOR_CHATTING = 102;

    private static MediaRecordUtils mInstance;
    private static UIHandler uiHandler;

    private boolean isRecord = false;
    private MediaRecorder mMediaRecorder;

    private UIThread uiThread;

    private int mState = -1;    //-1:没再录制，1：录制amr

    private String fileAddress;
    private String fileName;

    private MediaRecordUtils() {

    }

    private OnRecordProgressListener onRecordProgressListener;

    public synchronized static MediaRecordUtils getInstance() {
        if (mInstance == null)
            mInstance = new MediaRecordUtils();
        return mInstance;
    }

    /**
     * 开始录音
     */
    public void startRecord(int mFlag) {
        if (uiHandler == null)
            uiHandler = new UIHandler(Looper.myLooper());

        if (mState != -1) {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", MediaRecordUtils.ErrorCode.E_STATE_RECODING);
            msg.setData(b);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            return;
        }
        int mResult = -1;

        MediaRecordUtils mRecord_2 = MediaRecordUtils.getInstance();
        // fileAddress = AppDefine.DEFINE_PATH_RECORD + System.currentTimeMillis() + "Audio.amr";
        File file = new File(AllintaskApplication.getInstance().getVoiceFilePath());
        fileName = System.currentTimeMillis() + "Audio.amr";

        if (!file.exists()) {
            file.mkdirs();
        }
        fileAddress = AllintaskApplication.getInstance().getVoiceFilePath() + fileName;

        mResult = mRecord_2.startRecordAndFile(fileAddress);

        if (mResult == MediaRecordUtils.ErrorCode.SUCCESS) {
            if (onRecordProgressListener != null)
                onRecordProgressListener.onSuccessStartListener(mState);
            updateMicStatus();
            uiThread = new UIThread();
            new Thread(uiThread).start();
            mState = mFlag;
        } else {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", mResult);
            msg.setData(b);
            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        if (mState != -1) {

            MediaRecordUtils mRecord_2 = MediaRecordUtils.getInstance();
            mRecord_2.stopRecordAndFile();
            int time = 0;
            if (uiThread != null) {
                time = uiThread.getTimeMill();
                uiThread.stopThread();
            }
            if (uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_STOP);
            b.putInt("msg", time);
            msg.setData(b);
            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            mState = -1;
        }
    }

    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;
    public final static int CMD_VOICE_DEGREE = 2003;

    /**
     * 更新话筒状态
     */
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);

            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_VOICE_DEGREE);
            b.putInt("msg", (int) db);
            msg.setData(b);
            uiHandler.sendMessageDelayed(msg, SPACE);
        }
    }

    class UIHandler extends Handler {
        public UIHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int vCmd = b.getInt("cmd");
            long mSize;
            switch (vCmd) {
                case CMD_RECORDING_TIME:
                    int vTime = b.getInt("msg");
                    if (onRecordProgressListener != null) {
                        onRecordProgressListener.onVoiceChangeTime(vTime);
                    }
                    break;
                case CMD_RECORDFAIL:
                    int vErrorCode = b.getInt("msg");
//                    AppContext.showToast("录音失败：+ vMsg");
                    if (onRecordProgressListener != null)
                        onRecordProgressListener.onFailedStartListener(mState);
                    break;
                case CMD_STOP:
                    // MediaRecordUtils mRecord_2 = MediaRecordUtils.getInstance();
                    // mSize = mRecord_2.getRecordFileSize();
                    // AppContext.showToast("录音已停止.录音文件:" + AudioFileUtils.getAMRFilePath() + "\n文件大小：" + mSize);
                    int time = b.getInt("msg");
                    if (onRecordProgressListener != null)
                        onRecordProgressListener.onStopRecordListener(time);
                    break;
                case CMD_VOICE_DEGREE:
                    //回调录音音量，db是分贝值
                    updateMicStatus();
                    int db = b.getInt("msg");
                    if (onRecordProgressListener != null)
                        onRecordProgressListener.onVoiceDegreeListener(db);
                default:
                    break;
            }
        }
    }

    class UIThread implements Runnable {
        int mTimeMill = 0;
        boolean vRun = true;

        public void stopThread() {
            vRun = false;
        }

        public int getTimeMill() {
            return mTimeMill;
        }

        public void run() {
            while (vRun) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.i("TanJiaJun", "**run error:" + e.toString());
                }
                mTimeMill++;
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd", CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);

                uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            }
        }
    }

    public int startRecordAndFile(String filePath) {
        //判断是否有外部存储设备sdcard
        //if (AudioFileUtils.isSdcardExit()) {
        if (isRecord) {
            return ErrorCode.E_STATE_RECODING;
        } else {
            if (mMediaRecorder == null) {
                createMediaRecord();
            }

//            //目标目录
//            File targetDir = new File(AppContext.getInstance().getExternalCacheDir().toString() + AppDefine.DEFINE_PATH_RECORD);
//            //创建目录
//            if (!targetDir.exists()) {
//                targetDir.mkdirs();
//            }

                  /* 设置输出文件的路径 */
            AudioFileUtils.setAmrFilePath(filePath);
            mMediaRecorder.setOutputFile(AudioFileUtils.getAMRFilePath());

            try {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                // 让录制状态为true
                isRecord = true;
                return ErrorCode.SUCCESS;
            } catch (Exception ex) {
                ex.printStackTrace();
                return ErrorCode.E_UNKOWN;
            }
        }

//        } else {
//            return ErrorCode.E_NOSDCARD;
//        }
    }


    public void stopRecordAndFile() {
        close();
    }

    public long getRecordFileSize() {
        return AudioFileUtils.getFileSize(AudioFileUtils.getAMRFilePath());
    }


    public void createMediaRecord() {
         /* ①Initial：实例化MediaRecorder对象 */
        mMediaRecorder = new MediaRecorder();

        /* setAudioSource/setVedioSource*/
        mMediaRecorder.setAudioSource(AudioFileUtils.AUDIO_INPUT);//设置麦克风
        mMediaRecorder.setAudioSamplingRate(8000);
        /* 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */
        // mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

         /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
        // mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }


    private void close() {
        if (mMediaRecorder != null) {
            System.out.println("stopRecord");
            isRecord = false;
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);

            try {
                mMediaRecorder.stop();
            } catch (Exception e) {
                Log.i("TanJiaJun", "**stop error:" + e.toString());
            }

            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public class ErrorCode {

        public final static int SUCCESS = 1000;
        public final static int E_NOSDCARD = 1001;
        public final static int E_STATE_RECODING = 1002;
        public final static int E_UNKOWN = 1003;

        public String getErrorInfo(Context context, int vType) throws Resources.NotFoundException {
            switch (vType) {
                case SUCCESS:
                    return "success";

                case E_NOSDCARD:
                    return context.getResources().getString(R.string.error_no_sdcard);

                case E_STATE_RECODING:
                    return context.getResources().getString(R.string.error_state_record);

                case E_UNKOWN:
                default:
                    return context.getResources().getString(R.string.error_unknown);

            }
        }
    }

    public void setOnRecordProgressListener(OnRecordProgressListener onRecordProgressListener) {
        this.onRecordProgressListener = onRecordProgressListener;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * 检测是否获取了权限
     *
     * @param contextObject
     * @return
     */
    public boolean checkPermission(final Object contextObject) {
        if (contextObject == null) {
            return false;
        }

        // 未授权
        if (ContextCompat.checkSelfPermission((Activity) contextObject, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission((Activity) contextObject, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission((Activity) contextObject, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission((Activity) contextObject, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission((Activity) contextObject, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions((Activity) contextObject, new String[]{
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.VIBRATE,
                            Manifest.permission.WAKE_LOCK},
                    MediaRecordUtils.CODE_PERMISSION_FOR_CHATTING);
            Log.i("TanJiaJun", "直接申请");
            return false;
        }
    }

    public interface OnRecordProgressListener {

        void onSuccessStartListener(int flag);

        void onVoiceDegreeListener(int flag);

        void onStopRecordListener(int flag);

        void onFailedStartListener(int flag);

        void onVoiceChangeTime(int time);

    }

}
