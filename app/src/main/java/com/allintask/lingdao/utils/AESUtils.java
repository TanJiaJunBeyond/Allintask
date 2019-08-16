package com.allintask.lingdao.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by TanJiaJun on 2018/1/22.
 */

public class AESUtils {

    public static String UTFCODE = "UTF-8";

    /**
     * 输入text与密钥key按AES算法进行加密并返回密文
     *
     * @param text
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String text, String key) throws Exception {
        byte[] raw = key.getBytes(UTFCODE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(text.getBytes(UTFCODE));
        String result = Base64Utils.encode(encrypted);
        return result;
    }

    /**
     * 根据输入text与密钥按AES算法进行解密并返回明文
     *
     * @param text
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String text, String key) throws Exception {
        byte[] raw = key.getBytes(UTFCODE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted = Base64Utils.decode(text);
        byte[] original = cipher.doFinal(encrypted);
        String result = new String(original, UTFCODE);
        return result;
    }

}
