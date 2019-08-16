package com.allintask.lingdao.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TanJiaJun on 2018/1/29.
 */

public class KeywordUtils {

    /**
     * 多个关键字高亮变色
     *
     * @param color        变化的色值
     * @param text         文字
     * @param keywordArray 文字中的关键字数组
     * @return
     */
    public static SpannableStringBuilder matcherSearchInformation(int color, String text, String[] keywordArray) {
        String[] keyword = new String[keywordArray.length];
        System.arraycopy(keywordArray, 0, keyword, 0, keywordArray.length);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        String regex;

        for (int i = 0; i < keyword.length; i++) {
            String key = "";

            if (keyword[i].contains("*") || keyword[i].contains("(") || keyword[i].contains(")")) {
                char[] chars = keyword[i].toCharArray();
                for (int k = 0; k < chars.length; k++) {
                    if (chars[k] == '*' || chars[k] == '(' || chars[k] == ')') {
                        key = key + "\\" + String.valueOf(chars[k]);
                    } else {
                        key = key + String.valueOf(chars[k]);
                    }
                }
                keyword[i] = key;
            }

            regex = "(?i)" + keyword[i];
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableStringBuilder;
    }

}
