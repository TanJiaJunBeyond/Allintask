package cn.tanjiajun.sdk.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 哈希加密工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */

public class HashUtils {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String HASH_ALGORITHM_MD5 = "MD5";
    private static final String HASH_ALGORITHM_SHA1 = "SHA1";

    /**
     * 获得十六进制数
     *
     * @param buf
     * @return
     */
    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private static byte[] hash(byte[] data, final String hashAlgorithm) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {

        }
        return hash;
    }

    private static String hash(final String srcStr, final String hashAlgorithm) {
        String hashStr = srcStr;
        if (null != srcStr) {
            try {
                byte[] hashBytes = hash(srcStr.getBytes(CHARSET_UTF8), hashAlgorithm);
                hashStr = toHex(hashBytes);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } // if (null != srcStr)

        return hashStr;
    }

    public static String hashMD5(final String srcStr) {
        return hash(srcStr, HASH_ALGORITHM_MD5);
    }

    public static String hashSHA1(final String srcStr) {
        return hash(srcStr, HASH_ALGORITHM_SHA1);
    }

    public static void main(String[] args) {
        String srcStr = "我是中国人。";
        String hashStr = HashUtils.hashMD5(srcStr);
        System.out.println("srcStr = " + srcStr);
        System.out.println("hashStr = " + hashStr);
    }

}
