package com.allintask.lingdao.utils;

import android.text.TextUtils;

/**
 * Created by TanJiaJun on 2018/1/29.
 */

public class EnglishUtils {

    public static boolean isEnglishCharacterString(String str) {
        boolean isEnglishCharacterString = true;

        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z') && !(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {
                    isEnglishCharacterString = false;
                }
            }
        } else {
            isEnglishCharacterString = false;
        }
        return isEnglishCharacterString;
    }

}
