package com.allintask.lingdao.utils;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public class MediaRecordPlayerUtils {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = false;//是否为暂停播放
    private static MediaRecordPlayerUtils mInstance;
    private static OnMediaRecordPlayerListener onMediaRecordPlayerListener;

    public synchronized static MediaRecordPlayerUtils getInstance() {
        if (mInstance == null)
            mInstance = new MediaRecordPlayerUtils();
        return mInstance;
    }

    // 播放音乐
    public static boolean playSound(String fileName, boolean isNormal) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    Log.i("TanJiaJun", "****play reset");
                    return false;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i("TanJiaJun", "****play finish");
                    if (onMediaRecordPlayerListener != null) {
                        onMediaRecordPlayerListener.setOnCompletionListener();
                    }
                }
            });
        } else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setAudioStreamType(isNormal ? AudioManager.STREAM_MUSIC : AudioManager.STREAM_VOICE_CALL);
            mMediaPlayer.setDataSource(fileName);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 播放Raw文件夹的音乐
    public static boolean playRawSound(AssetFileDescriptor assetFileDescriptor, boolean isNormal) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    Log.i("TanJiaJun", "****play reset");
                    return false;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i("TanJiaJun", "****play finish");
                    if (onMediaRecordPlayerListener != null) {
                        onMediaRecordPlayerListener.setOnCompletionListener();
                    }
                }
            });
        } else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setAudioStreamType(isNormal ? AudioManager.STREAM_MUSIC : AudioManager.STREAM_VOICE_CALL);
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 暂停播放
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    // 复位播放
    public static void reset() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    // 释放资源
    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void set0nMediaRecordPlayerListener(OnMediaRecordPlayerListener onMediaRecordPlayerListener) {
        this.onMediaRecordPlayerListener = onMediaRecordPlayerListener;
    }

    public interface OnMediaRecordPlayerListener {
        void setOnCompletionListener();
    }

}
