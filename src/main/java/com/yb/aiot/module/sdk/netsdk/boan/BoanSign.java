package com.yb.aiot.module.sdk.netsdk.boan;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * <p>
 * sign生成工具
 * <p>
 *
 * @author author
 * @date 2022/8/22 14:43
 */
public class BoanSign {

    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * sign生成
     *
     * @param map map
     * @return java.lang.String
     * @param: signKey
     */
    public static String sign(Map<String, Object> map, String signKey) {
        if (map == null) return null;
        map.remove("sign");
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuilder builder = new StringBuilder();
        for (String key : keyList) {
            Object value = map.get(key);
            builder.append(key).append("=").append(value).append("&");
        }
        String signStr = (builder.substring(0, builder.length() - 1) + signKey).toLowerCase();
        return MD5(signStr);
    }

    /**
     * 生成MD5
     *
     * @param signStr signStr
     * @return java.lang.String
     */
    public static String MD5(String signStr) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.reset();
            byte[] digestBytes = messagedigest.digest(signStr.getBytes(StandardCharsets.UTF_8));
            return byteToString(digestBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * byte[]转String
     *
     * @param digestBytes digestBytes
     * @return java.lang.String
     */
    private static String byteToString(byte[] digestBytes) {
        int i = digestBytes.length;
        char[] ac = new char[i * 2];
        int j = 0;
        for (byte digestByte : digestBytes) {
            ac[j++] = hexDigits[digestByte >>> 4 & 0xf];
            ac[j++] = hexDigits[digestByte & 0xf];
        }
        return new String(ac);
    }

}
