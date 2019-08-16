package cn.tanjiajun.sdk.common.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

/**
 * 音频工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */

public class AudioUtils {

    /**
     * 获得音频时长
     *
     * @param context   上下文环境
     * @param audioFile 音频文件
     * @return 音频时长
     * @since 0.0.1
     */
    public static final int getDuration(final Context context, final File audioFile) {

        int duration = 0;
        if (null != audioFile && audioFile.exists()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(audioFile.getAbsolutePath()));
            duration = mediaPlayer.getDuration();
        } // if (null != audioFile && audioFile.exists())

        return duration;
    }

    /**
     * 获得音频时长字符串
     *
     * @param context   上下文环境
     * @param audioFile 音频文件
     * @return 音频时长字符串
     * @see #getDuration(Context, File)
     * @since 0.0.1
     */
    public static final String getDurationString(final Context context, final File audioFile) {

        String durationString = null;

        if (null != audioFile && audioFile.exists()) {
            int duration = getDuration(context, audioFile) / 1000;

            int minute = duration / 60;
            int second = duration % 60;
            StringBuilder durationSB = new StringBuilder();
            if (0 != minute) {
                durationSB.append(minute);
                durationSB.append("\'");
            }
            if (0 != second) {
                if (0 != durationSB.length()) {
                    durationSB.append(" ");
                }
                durationSB.append(second);
                durationSB.append("\"");
            }

            durationString = durationSB.toString().toString();
        } // if (null != audioFile && audioFile.exists())

        return durationString;
    }

    /**
     * 获得音频时长字符串
     *
     * @param duration 音频时长
     * @return 音频时长字符串
     * @since 0.0.1
     */
    public static final String getDurationString(int duration) {
        String durationString = null;

        duration /= 1000;

        int minute = duration / 60;
        int second = duration % 60;
        StringBuilder durationSB = new StringBuilder();
        if (0 != minute) {
            durationSB.append(minute);
            durationSB.append("\'");
        }
        if (0 != second) {
            if (0 != durationSB.length()) {
                durationSB.append(" ");
            }
            durationSB.append(second);
            durationSB.append("\"");
        }
        durationString = durationSB.toString().toString();

        return durationString;
    }
}
