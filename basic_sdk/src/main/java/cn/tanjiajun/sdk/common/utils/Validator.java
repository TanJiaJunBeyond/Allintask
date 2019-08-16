package cn.tanjiajun.sdk.common.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class Validator {

    public static boolean isEmpty(final String str) {
        boolean isEmpty = false;
        if (null == str || 0 == str.trim().length()) {
            isEmpty = true;
        }

        return isEmpty;
    }

    //    public static boolean isIdentifier(final String str) {
    //        boolean isIdentifier = true;
    //        if (isEmpty(str) || !Pattern.matches("^(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])$", str)) {
    //            isIdentifier = false;
    //        }
    //        
    //        return isIdentifier;
    //    }

    public static boolean isIdentifier(final String str) {
        boolean isIdentifier = false;
        if (isEmpty(str) || !Pattern.matches("^(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])$", str)) {
            isIdentifier = false;
        } else {
            if (isIdAddress(str) && isIdDate(str) && isIdSequence(str)) {
                isIdentifier = true;
            } else {
                isIdentifier = false;
            }
            if (str.length() == 18) {
                if (!isLastNumberCorrect(str)) {
                    isIdentifier = false;
                }
            }
        }

        return isIdentifier;
    }

    private static boolean isIdAddress(String ids) {
        String address = ids.substring(0, 6);
        boolean isAddress = false;
        if (Pattern.matches("^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}$", address)) {
            isAddress = true;
        } else {
            isAddress = false;
        }
        return isAddress;
    }

    private static boolean isIdDate(String ids) {
        boolean isDate = false;
        String date = null;
        if (ids.length() == 15) {
            date = "19" + ids.substring(6, 12);
        } else if (ids.length() == 18) {
            date = ids.substring(6, 14);
        }
        if (Pattern.matches("^((19\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(19\\d{2}(0[13578]|1[02])31)|(19\\d{2}02(0[1-9]|1\\d|2[0-8]))|(19([13579][26]|[2468][048]|0[48])0229))$", date)) {
            isDate = true;
        }
        return isDate;
    }

    private static boolean isIdSequence(String ids) {
        boolean isSequence = false;
        String sequence = null;
        if (ids.length() == 15) {
            sequence = ids.substring(12, 15);
        } else if (ids.length() == 18) {
            sequence = ids.substring(14, 17);
        }
        Log.i("testYJ", "sequence-->" + sequence);
        if (Pattern.matches("^\\d{3}$", sequence)) {
            isSequence = true;
        }

        return isSequence;
    }

    protected static boolean isLastNumberCorrect(String ids) {
        int[] ratios = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] results = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        boolean isLastNumberCorrect = false;
        int[] numbers_i = new int[17];
        char[] numbers_c = new char[18];
        int lastNumber = 0;
        numbers_c = ids.toCharArray();
        for (int i = 0; i < 17; i++) {
            numbers_i[i] = Integer.valueOf(String.valueOf(numbers_c[i]));
            lastNumber += numbers_i[i] * ratios[i];
            //            Log.i("testYJ","numbers_c-->"+numbers_c[i]);
        }

        //        Log.i("testYJ","lastNumber1-->"+numbers_c[17]);
        //        Log.i("testYJ","lastNumber2-->"+String.valueOf(getLastNumber(lastNumber % 11)));
        Log.i("testYJ", "sum" + lastNumber);
        Log.i("testYJ", "last" + lastNumber % 11);
        if (String.valueOf(results[lastNumber % 11]).equals(String.valueOf(numbers_c[17]).toUpperCase())) {
            isLastNumberCorrect = true;
        }

        return isLastNumberCorrect;
    }

    /**
     * 上面是验证身份证---------------------------------------------------------
     */

    public static boolean isMobilePhoneNumber(final String str) {
        boolean isMobilePhoneNumber = true;
        if (isEmpty(str) || !Pattern.matches("^(130|131|132|133|134|135|136|137|138|139|145|147|150|151|152|153|155|156|157|158|159|176|177|178|180|181|182|184|185|186|187|188|189)\\d{8}$", str)) {
            isMobilePhoneNumber = false;
        }

        return isMobilePhoneNumber;
    }

    public static boolean isPhoneNumber(final String str) {
        boolean isPhoneNumber = true;
        if (isEmpty(str) || !Pattern.matches("(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}", str)) {
            isPhoneNumber = false;
        }

        return isPhoneNumber;
    }

    // 昵称长度 : 最大14位,中文7字
    public static boolean isUserName(final String str) {
        boolean isUserName = false;
        if (!isEmpty(str) && Pattern.matches("^[a-zA-Z\u4E00-\u9FA5]\\w{0,254}$", str)) {
            isUserName = true;
        }
        return isUserName;
    }

    // 真实姓名长度 : 最大14位,中文7字
    public static boolean isRealName(final String str) {
        boolean isRealName = false;
        if (isEmpty(str) || (!isEmpty(str) && Pattern.matches("^[a-zA-Z\u4E00-\u9FA5]\\w{0,254}$", str))) {
            isRealName = true;
        }
        return isRealName;
    }

    // 密码长度 : 最小长度--最小6位~最大16位;允许字符集：26个英文字母，数字0~9，键盘上常用的字符#@%等（英文半角码）
    public static boolean isPassword(final String str) {
        boolean isPassword = false;
        if (!isEmpty(str) && Pattern.matches("^[a-zA-Z0-9_#@%!?~%$&]{0,254}$", str)) {
            isPassword = true;
        }
        return isPassword;
    }

    public static boolean isChineseCharacter(final String str) {
        boolean isChineseCharacter = false;
        if (!isEmpty(str) && Pattern.matches("[^\\x00-\\xff]*", str)) {
            isChineseCharacter = true;
        }
        return isChineseCharacter;
    }

    public static boolean isEmail(final String str) {
        boolean isEmail = true;
        if (isEmpty(str) || !Pattern.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", str)) {
            isEmail = false;
        }
        return isEmail;
    }

    public static boolean isFloatingPointNumber(final String str) {
        boolean isFloatingPointNumber = true;

        if (isEmpty(str) || !Pattern.matches("^(-?\\d+)(\\.\\d+)?$", str)) {
            isFloatingPointNumber = false;
        }
        return isFloatingPointNumber;
    }

    public static boolean isPostcode(final String str) {
        boolean isPostcode = true;

        if (isEmpty(str) || !Pattern.matches("^[1-9]{1}(\\d+){5}$", str)) {
            isPostcode = false;
        }
        return isPostcode;
    }

    public static boolean isPositiveIntegerNumber(final String str) {

        boolean isPositiveIntegerNumber = true;

        if (isEmpty(str) || !Pattern.matches("^[0-9]*[1-9][0-9]*$", str)) {
            isPositiveIntegerNumber = false;
        }
        return isPositiveIntegerNumber;
    }

    //    public static boolean isLocalPictureValid(final Picture pic) {
    //
    //        if (null == pic) {
    //            return false;
    //        }
    //        
    //        if (!isLocalFilePathValid(pic.getThumbnailLocalPath())) {
    //            return false;
    //        }
    //        
    //        if (!isLocalFilePathValid(pic.getLargeLocalPath())) {
    //            return false;
    //        }
    //        
    //        if (!isLocalFilePathValid(pic.getOrginalLocalPath())) {
    //            return false;
    //        }
    //        
    //        
    //        return true;
    //    }

    public static boolean isLocalFilePathValid(final String pathStr) {
        if (null == pathStr) {
            return false;
        }

        return isLocalFileValid(new File(pathStr));
    }

    public static boolean isLocalFileValid(final File file) {
        if (null == file) {
            return false;
        }

        return file.exists();
    }

    public static boolean isIdValid(final Object id) {
        boolean isIdValid = false;
        if (null != id && id instanceof String) {
            String tmpId = TypeUtils.getString(id);
            if (!TextUtils.isEmpty(tmpId)) {
                if (!"-1".equals(tmpId) && !"".equals(tmpId)) {
                    isIdValid = true;
                }
            }
        }

        return isIdValid;
    }

    public static boolean isCommentValid(String comment) {
        boolean isCommentValid = false;
        if (!TextUtils.isEmpty(comment) && comment.length() <= 140) {
            isCommentValid = true;
        }

        return isCommentValid;
    }

    public static boolean isFeedbackContentValid(String feedbackContent) {
        boolean isFeedbackContentValid = false;
        if (!TextUtils.isEmpty(feedbackContent) && feedbackContent.length() <= 250) {
            isFeedbackContentValid = true;
        }

        return isFeedbackContentValid;
    }

    public static boolean isWishTitleValid(String wishTitle) {
        boolean isWishTitleValid = false;
        if (!TextUtils.isEmpty(wishTitle) && wishTitle.length() <= 20) {
            isWishTitleValid = true;
        }

        return isWishTitleValid;
    }

    public static boolean isWishContentValid(String wishContent) {
        boolean isWishContentValid = false;
        if (!TextUtils.isEmpty(wishContent) && wishContent.length() <= 140) {
            isWishContentValid = true;
        }

        return isWishContentValid;
    }


    /**
     * 判断是否是银行卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

}
