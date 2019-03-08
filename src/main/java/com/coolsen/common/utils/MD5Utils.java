package com.coolsen.common.utils;

import java.security.MessageDigest;

/**
 * Created by YeKai on 2019/3/8
 */
public class MD5Utils {
    private final static char[] HEXD = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public final static String encode(String s) {
        return encode(s, null);
    }

    public final static String encode(String s, final String encoding) {
        final char[] hexDigits = HEXD;
        try {
            byte[] strTemp = (encoding == null ? s.getBytes() : s.getBytes(encoding));
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
