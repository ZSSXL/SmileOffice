package com.zss.smile.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author ZSS
 * @date 2019/12/3 13:50
 * @desc 密码加密工具
 */
@SuppressWarnings("unused")
public class EncryptionUtil {

    /**
     * 加盐
     */
    private static final String SALT = "wecojkjncuskcusdcslzjuuencsldcidh)_sdslcsd";

    /**
     * 密码加密
     *
     * @param origin 原密码
     * @return String
     */
    public static String encrypt(String origin) {
        return md5Encode(origin + SALT);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 返回大写的MD5
     *
     * @param origin 加盐后的密码
     * @return String
     */
    private static String md5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString.toUpperCase();
    }

    private static final String[] HEX_DIGITS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
    };

}
