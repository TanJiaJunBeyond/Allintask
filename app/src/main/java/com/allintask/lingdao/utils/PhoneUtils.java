package com.allintask.lingdao.utils;

/**
 * Created by TanJiaJun on 2016/4/23.
 */

public class PhoneUtils {

    /**
     * 验证手机格式
     */
    public static boolean isPhone(String phone) {
        String telRegex = "[1][3456789]\\d{9}";

        if (phone.length() == 11) {
            return phone.matches(telRegex);
        } else {
            return false;
        }
    }

    private static boolean hasMaskPhone(String phone) {
        if (phone.length() == 11 && phone.contains("*")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 手机号脱敏
     *
     * @param phone 手机号
     * @return 脱敏手机号
     */
    public static String maskPhone(String phone) {
        String maskingPhone = null;

        if (hasMaskPhone(phone)) {
            maskingPhone = phone;
        } else {
            if (isPhone(phone)) {
                maskingPhone = phone.replaceAll("(?<=[\\d]{3})\\d(?=[\\d]{4})", "*");
            }
        }
        return maskingPhone;
    }

}
