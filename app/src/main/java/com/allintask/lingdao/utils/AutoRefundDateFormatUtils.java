package com.allintask.lingdao.utils;

/**
 * Created by TanJiaJun on 2018/4/20.
 */

public class AutoRefundDateFormatUtils {

    public static String format(long autoRefundDate) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = autoRefundDate / dd;
        long hour = (autoRefundDate - day * dd) / hh;
        long minute = (autoRefundDate - day * dd - hour * hh) / mi;
        long second = (autoRefundDate - day * dd - hour * hh - minute * mi) / ss;

        String dayString = String.valueOf(day);
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;

        StringBuilder stringBuilder = new StringBuilder();

        if (day != 0) {
            stringBuilder.append(dayString).append("天\r");
        }

        stringBuilder.append(hourString).append(":").append(minuteString).append(":").append(secondString);
        return stringBuilder.toString();
    }

}
