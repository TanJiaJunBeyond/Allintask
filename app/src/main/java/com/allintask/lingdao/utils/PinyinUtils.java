package com.allintask.lingdao.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by TanJiaJun on 2018/1/23.
 */

public class PinyinUtils {

    /**
     * 将字符串的第一个汉字转化为拼音 <br>
     * 若该字符串的第一个字为英文字母，则原样返回
     *
     * @param str 欲要将首字转换为拼音的字符串
     * @return String 拼音/英文
     */
    public static String getPinyin(String str) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat(); // 设置格式
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 没有音标
        outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);    // 输出拼音为大写字母

        // 去除中英文标点及空格
        str = str.trim().replaceAll("\\p{P}", "").trim();

        if (str.length() == 0) {
            str = "#";
        }

        String[] strs = null;

        try {
            strs = PinyinHelper.toHanyuPinyinStringArray(str.charAt(0), outputFormat);
        } catch (BadHanyuPinyinOutputFormatCombination e) {

        }

        if (strs != null) {
            str = strs[0];
        }
        return str.toUpperCase();
    }

}
