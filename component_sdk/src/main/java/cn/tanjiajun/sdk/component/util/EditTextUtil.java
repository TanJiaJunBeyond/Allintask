package cn.tanjiajun.sdk.component.util;

import android.view.View;
import android.widget.EditText;

/**
 * EditText工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class EditTextUtil {

    private static <T extends View> T findView(View contentView, int viewId) {
        return (T) contentView.findViewById(viewId);
    }

    public static String getEditTextInput(View layout, int edtID) {
        return getEditTextInput(layout, edtID, "");
    }

    public static String getEditTextInput(View layout, int edtID, String defaultValues) {
        String input = null;

        EditText edt = findView(layout, edtID);
        if (null != edt) {
            input = edt.getText() != null ? edt.getText().toString() : "";
        }

        return input;
    }

    public static void setEditText(View layout, int edtID, String text) {

        EditText edt = findView(layout, edtID);
        if (null != edt) {
            edt.setText(text);
        }

    }

    /**
     * 根据最大的字节长度，限制EditText的输出
     *
     * @param mEditText 输入框
     * @param maxLength 待输入的字符串最大字节数
     */
    public static void doInputLimit(EditText mEditText, long maxLength) {
        String input = mEditText.getText().toString().trim();
        long curLength = calculateInputLength(input);
//        long curLength = input.getBytes().length;     // utf-8编码下，中文占3个字节 ，GBK编码下暂用2个字节

        if (curLength > maxLength) {
            try {
                int end = calculateInputIndex(input, maxLength) + 1;
                input = input.substring(0, end);
                mEditText.setText(input);
                mEditText.setSelection(mEditText.length());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据最大的字节长度，限制EditText的输出
     *
     * @param mEditText 输入框
     * @param start     字符串的开始位置
     * @param maxLength 待输入的字符串最大字节数
     */
    public static void doInputLimit(EditText mEditText, int start, long maxLength) {
        String input = mEditText.getText().toString().trim();
        long curLength = calculateInputLength(input);

        if (curLength > maxLength) {
            try {
                input = input.substring(0, start);
                mEditText.setText(input);
                mEditText.setSelection(start);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 计算字符串所占的字节数大小
     *
     * @param c 待计算的字符串
     * @return 字符串所占的字节数大小
     */
    private static long calculateInputLength(CharSequence c) {
        double len = 0;

        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 1;
            } else {
                len += 2;
            }
        }
        return Math.round(len);
    }

    /**
     * 计算某个字节数长度下，对应输入框的位置
     *
     * @param c            待计算的字符串
     * @param targetLength 规定的字符串字节数大小
     * @return
     */
    private static int calculateInputIndex(CharSequence c, long targetLength) {
        int index = 0;
        long len = 0;

        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 1;
            } else {
                len += 2;
            }

            if (len == targetLength) {
                index = i;
                break;
            }
        }
        return index;
    }

}
