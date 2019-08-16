package com.allintask.lingdao.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.bean.upload.UploadRequestBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.database.UploadDao;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by TanJiaJun on 2018/3/15.
 */

public class SocketUploader {

    private static final String UPLOAD = "upload";

    public static final int UPLOAD_FILE_REQUEST_CODE = 0;

    private Context context;

    private UploadDao uploadDao;
    private ExecutorService executorService;

    private boolean totalStatus = false;

    private String flagId;

    private OnUploadStatusListener onUploadStatusListener;

    public SocketUploader(Context context) {
        this.context = context;

        uploadDao = new UploadDao(context);
        executorService = Executors.newFixedThreadPool(9);
    }

    public void uploadFile(final int userId, final File uploadFile, Activity activity) {
        if (null != executorService && !executorService.isShutdown()) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                String deviceId = telephonyManager.getDeviceId();
                long timestamp = System.currentTimeMillis();
                flagId = deviceId + String.valueOf(timestamp);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, UPLOAD_FILE_REQUEST_CODE);
                return;
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(ServiceAPIConstant.SOCKET_HOST, ServiceAPIConstant.SOCKET_PORT);
                        OutputStream outputStream = socket.getOutputStream();

                        String sourceId = TypeUtils.getString(uploadDao.getSourceId(uploadFile), "");

                        Map<String, List<String>> map = new HashMap<>();

                        List<String> contentLengthList = new ArrayList<>();
                        contentLengthList.add(String.valueOf(uploadFile.length()));
                        map.put("contentLength", contentLengthList);

                        List<String> fileNameList = new ArrayList<>();
                        fileNameList.add(uploadFile.getName());
                        map.put("fileName", fileNameList);

                        List<String> userIdList = new ArrayList<>();
                        userIdList.add(String.valueOf(userId));
                        map.put("userId", userIdList);

                        List<String> sourceIdList = new ArrayList<>();
                        sourceIdList.add(sourceId);
                        map.put("sourceId", sourceIdList);

                        List<String> flagIdList = new ArrayList<>();
                        flagIdList.add(flagId);
                        map.put("flagId", flagIdList);

                        List<String> codeIdList = new ArrayList<>();
                        codeIdList.add("0");
                        map.put("codeId", codeIdList);

                        List<String> versionList = new ArrayList<>();
                        versionList.add("V1.0");
                        map.put("version", versionList);

                        String sign = SignUtils.getSign(map, false);

                        UploadRequestBean uploadRequestBean = new UploadRequestBean();
                        uploadRequestBean.contentLength = String.valueOf(uploadFile.length());
                        uploadRequestBean.fileName = uploadFile.getName();
                        uploadRequestBean.userId = String.valueOf(userId);
                        uploadRequestBean.sourceId = sourceId;
                        uploadRequestBean.flagId = flagId;
                        uploadRequestBean.codeId = "0";
                        uploadRequestBean.version = "V1.0";
                        uploadRequestBean.sign = sign;
                        String head = JSONObject.toJSONString(uploadRequestBean, SerializerFeature.DisableCircularReferenceDetect) + "\r\n";
                        outputStream.write(head.getBytes());

                        PushbackInputStream pushbackInputStream = new PushbackInputStream(socket.getInputStream());
                        String response = StreamUtils.readLine(pushbackInputStream);

                        if (!TextUtils.isEmpty(response)) {
                            String[] items = response.split(";");
                            String responseId = items[0].substring(items[0].indexOf("=") + 1);
                            String position = items[1].substring(items[1].indexOf("=") + 1);

                            Log.i("TanJiaJun", responseId + ":" + position);

                            if (TextUtils.isEmpty(sourceId)) {
                                uploadDao.save(sourceId, uploadFile);
                            }

                            RandomAccessFile randomAccessFile = new RandomAccessFile(uploadFile, "r");
                            randomAccessFile.seek(Integer.valueOf(position));

                            byte[] buffer = new byte[1024];
                            int len = -1;
                            int length = Integer.valueOf(position);

                            while ((len = randomAccessFile.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, len);
                                length += len;
                            }

                            if (length == uploadFile.length()) {
                                uploadDao.delete(uploadFile);

                                if (null != onUploadStatusListener) {
                                    onUploadStatusListener.onSuccess(flagId);
                                }
                            }

                            randomAccessFile.close();
                        } else {
                            if (null != onUploadStatusListener) {
                                onUploadStatusListener.onFail();
                            }
                        }

                        outputStream.close();
                        pushbackInputStream.close();
                        socket.close();
                    } catch (Exception e) {
                        if (null != onUploadStatusListener) {
                            onUploadStatusListener.onFail();
                        }

                        e.printStackTrace();
                    }
                }
            });

            thread.setPriority(Thread.NORM_PRIORITY - 1);
            thread.setName(UPLOAD);
            executorService.submit(thread);
        }
    }

    public void uploadFileList(final int userId, List<File> uploadFileList, Activity activity) {
        if (null != executorService && !executorService.isShutdown()) {
            final List<Boolean> uploadStatusList = new ArrayList<>();

            for (int i = 0; i < uploadFileList.size(); i++) {
                uploadStatusList.add(false);
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                String deviceId = telephonyManager.getDeviceId();
                long timestamp = System.currentTimeMillis();
                flagId = deviceId + String.valueOf(timestamp);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, UPLOAD_FILE_REQUEST_CODE);
                return;
            }

            for (int i = 0; i < uploadFileList.size(); i++) {
                final int uploadStatusPosition = i;
                final File uploadFile = uploadFileList.get(i);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(ServiceAPIConstant.SOCKET_HOST, ServiceAPIConstant.SOCKET_PORT);
                            OutputStream outputStream = socket.getOutputStream();

                            String sourceId = TypeUtils.getString(uploadDao.getSourceId(uploadFile), "");

                            Map<String, List<String>> map = new HashMap<>();

                            List<String> contentLengthList = new ArrayList<>();
                            contentLengthList.add(String.valueOf(uploadFile.length()));
                            map.put("contentLength", contentLengthList);

                            List<String> fileNameList = new ArrayList<>();
                            fileNameList.add(uploadFile.getName());
                            map.put("fileName", fileNameList);

                            List<String> userIdList = new ArrayList<>();
                            userIdList.add(String.valueOf(userId));
                            map.put("userId", userIdList);

                            List<String> sourceIdList = new ArrayList<>();
                            sourceIdList.add(sourceId);
                            map.put("sourceId", sourceIdList);

                            List<String> flagIdList = new ArrayList<>();
                            flagIdList.add(flagId);
                            map.put("flagId", flagIdList);

                            List<String> codeIdList = new ArrayList<>();
                            codeIdList.add(String.valueOf(uploadStatusPosition));
                            map.put("codeId", codeIdList);

                            List<String> versionList = new ArrayList<>();
                            versionList.add("V1.0");
                            map.put("version", versionList);

                            String sign = SignUtils.getSign(map, false);

                            UploadRequestBean uploadRequestBean = new UploadRequestBean();
                            uploadRequestBean.contentLength = String.valueOf(uploadFile.length());
                            uploadRequestBean.fileName = uploadFile.getName();
                            uploadRequestBean.userId = String.valueOf(userId);
                            uploadRequestBean.sourceId = sourceId;
                            uploadRequestBean.flagId = flagId;
                            uploadRequestBean.codeId = String.valueOf(uploadStatusPosition);
                            uploadRequestBean.version = "V1.0";
                            uploadRequestBean.sign = sign;
                            String head = JSONObject.toJSONString(uploadRequestBean, SerializerFeature.DisableCircularReferenceDetect) + "\r\n";
                            outputStream.write(head.getBytes());

                            PushbackInputStream pushbackInputStream = new PushbackInputStream(socket.getInputStream());
                            String response = StreamUtils.readLine(pushbackInputStream);

                            if (!TextUtils.isEmpty(response)) {
                                String[] items = response.split(";");
                                String responseId = items[0].substring(items[0].indexOf("=") + 1);
                                String position = items[1].substring(items[1].indexOf("=") + 1);

                                Log.i("TanJiaJun", responseId + ":" + position);

                                if (TextUtils.isEmpty(sourceId)) {
                                    uploadDao.save(sourceId, uploadFile);
                                }

                                RandomAccessFile randomAccessFile = new RandomAccessFile(uploadFile, "r");
                                randomAccessFile.seek(Integer.valueOf(position));

                                byte[] buffer = new byte[1024];
                                int len = -1;
                                int length = Integer.valueOf(position);

                                while ((len = randomAccessFile.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, len);
                                    length += len;
                                }

                                if (length == uploadFile.length()) {
                                    uploadDao.delete(uploadFile);
                                    uploadStatusList.set(uploadStatusPosition, true);

                                    for (int j = 0; j < uploadStatusList.size(); j++) {
                                        boolean status = TypeUtils.getBoolean(uploadStatusList.get(j), false);

                                        if (status) {
                                            totalStatus = true;
                                        } else {
                                            totalStatus = false;
                                            break;
                                        }
                                    }

                                    if (null != onUploadStatusListener && totalStatus) {
                                        onUploadStatusListener.onSuccess(flagId);
                                    }
                                }

                                randomAccessFile.close();
                            } else {
                                if (null != onUploadStatusListener) {
                                    uploadStatusList.set(uploadStatusPosition, false);
                                    onUploadStatusListener.onFail();
                                }
                            }

                            outputStream.close();
                            pushbackInputStream.close();
                            socket.close();
                        } catch (Exception e) {
                            if (null != onUploadStatusListener) {
                                uploadStatusList.set(uploadStatusPosition, false);
                                onUploadStatusListener.onFail();
                            }

                            e.printStackTrace();
                        }
                    }
                });

                thread.setPriority(Thread.NORM_PRIORITY - 1);
                thread.setName(UPLOAD + "_" + String.valueOf(i));
                executorService.submit(thread);
            }
        }
    }

    public void uploadVoiceFile(final int userId, final File uploadFile, final String filename, Activity activity) {
        if (null != executorService && !executorService.isShutdown()) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                String deviceId = telephonyManager.getDeviceId();
                long timestamp = System.currentTimeMillis();
                flagId = deviceId + String.valueOf(timestamp);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, UPLOAD_FILE_REQUEST_CODE);
                return;
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(ServiceAPIConstant.SOCKET_HOST, ServiceAPIConstant.SOCKET_PORT);
                        OutputStream outputStream = socket.getOutputStream();

                        String sourceId = TypeUtils.getString(uploadDao.getSourceId(uploadFile), "");

                        Map<String, List<String>> map = new HashMap<>();

                        List<String> contentLengthList = new ArrayList<>();
                        contentLengthList.add(String.valueOf(uploadFile.length()));
                        map.put("contentLength", contentLengthList);

                        List<String> fileNameList = new ArrayList<>();
                        fileNameList.add(filename);
                        map.put("fileName", fileNameList);

                        List<String> userIdList = new ArrayList<>();
                        userIdList.add(String.valueOf(userId));
                        map.put("userId", userIdList);

                        List<String> sourceIdList = new ArrayList<>();
                        sourceIdList.add(sourceId);
                        map.put("sourceId", sourceIdList);

                        List<String> flagIdList = new ArrayList<>();
                        flagIdList.add(flagId);
                        map.put("flagId", flagIdList);

                        List<String> codeIdList = new ArrayList<>();
                        codeIdList.add("0");
                        map.put("codeId", codeIdList);

                        List<String> versionList = new ArrayList<>();
                        versionList.add("V1.0");
                        map.put("version", versionList);

                        String sign = SignUtils.getSign(map, false);

                        UploadRequestBean uploadRequestBean = new UploadRequestBean();
                        uploadRequestBean.contentLength = String.valueOf(uploadFile.length());
                        uploadRequestBean.fileName = filename;
                        uploadRequestBean.userId = String.valueOf(userId);
                        uploadRequestBean.sourceId = sourceId;
                        uploadRequestBean.flagId = flagId;
                        uploadRequestBean.codeId = "0";
                        uploadRequestBean.version = "V1.0";
                        uploadRequestBean.sign = sign;
                        String head = JSONObject.toJSONString(uploadRequestBean, SerializerFeature.DisableCircularReferenceDetect) + "\r\n";
                        outputStream.write(head.getBytes());

                        PushbackInputStream pushbackInputStream = new PushbackInputStream(socket.getInputStream());
                        String response = StreamUtils.readLine(pushbackInputStream);

                        if (!TextUtils.isEmpty(response)) {
                            String[] items = response.split(";");
                            String responseId = items[0].substring(items[0].indexOf("=") + 1);
                            String position = items[1].substring(items[1].indexOf("=") + 1);

                            Log.i("TanJiaJun", responseId + ":" + position);

                            if (TextUtils.isEmpty(sourceId)) {
                                uploadDao.save(sourceId, uploadFile);
                            }

                            RandomAccessFile randomAccessFile = new RandomAccessFile(uploadFile, "r");
                            randomAccessFile.seek(Integer.valueOf(position));

                            byte[] buffer = new byte[1024];
                            int len = -1;
                            int length = Integer.valueOf(position);
                            Log.i("TanJiaJun", "length:" + length);

                            while ((len = randomAccessFile.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, len);
                                length += len;
                            }

                            if (length == uploadFile.length()) {
                                uploadDao.delete(uploadFile);

                                if (null != onUploadStatusListener) {
                                    onUploadStatusListener.onSuccess(flagId);
                                }
                            }

                            randomAccessFile.close();
                        } else {
                            if (null != onUploadStatusListener) {
                                onUploadStatusListener.onFail();
                            }
                        }

                        outputStream.close();
                        pushbackInputStream.close();
                        socket.close();
                    } catch (Exception e) {
                        if (null != onUploadStatusListener) {
                            onUploadStatusListener.onFail();
                        }

                        e.printStackTrace();
                    }
                }
            });

            thread.setPriority(Thread.NORM_PRIORITY - 1);
            thread.setName(UPLOAD);
            executorService.submit(thread);
        }
    }

    public interface OnUploadStatusListener {

        void onSuccess(String flagId);

        void onFail();

    }

    public void setOnUploadStatusListener(OnUploadStatusListener onUploadStatusListener) {
        this.onUploadStatusListener = onUploadStatusListener;
    }

}
