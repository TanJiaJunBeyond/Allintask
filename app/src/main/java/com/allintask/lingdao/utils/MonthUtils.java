package com.allintask.lingdao.utils;

/**
 * Created by TanJiaJun on 2018/3/19.
 */

public class MonthUtils {

    public static String getMonthChinese(int month) {
        String monthChinese = null;

        switch (month) {
            case 1:
                monthChinese = "一月";
                break;

            case 2:
                monthChinese = "二月";
                break;

            case 3:
                monthChinese = "三月";
                break;

            case 4:
                monthChinese = "四月";
                break;

            case 5:
                monthChinese = "五月";
                break;

            case 6:
                monthChinese = "六月";
                break;

            case 7:
                monthChinese = "七月";
                break;

            case 8:
                monthChinese = "八月";
                break;

            case 9:
                monthChinese = "九月";
                break;

            case 10:
                monthChinese = "十月";
                break;

            case 11:
                monthChinese = "十一月";
                break;

            case 12:
                monthChinese = "十二月";
                break;
        }
        return monthChinese;
    }

}
