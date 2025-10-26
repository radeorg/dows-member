package org.dows.member.biz.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;


public class SignatureUtils {
    private static final String MERCHANT_KEY = "";
    public static String generateHmacSHA256(Map<String, String> params, String apiKey) {
        // 参数按ASCII排序
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        // 拼接参数
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb.append("key=").append(apiKey);

        // HMAC-SHA256加密
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            sha256.init(new SecretKeySpec(apiKey.getBytes(), "HmacSHA256"));
            byte[] bytes = sha256.doFinal(sb.toString().getBytes());
            return Hex.encodeHexString(bytes).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }
    public static boolean verifySignature(Map<String, String> params) {
        try {
            String receivedSign = params.get("sign");
            params.remove("sign");

            // 参数按ASCII码排序
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);

            // 拼接参数字符串
            StringBuilder sb = new StringBuilder();
            for (String key : keys) {
                String value = params.get(key);
                if (value != null && !value.isEmpty()) {
                    sb.append(key).append("=").append(value).append("&");
                }
            }
            sb.append("key=").append(MERCHANT_KEY);

            // 生成MD5签名
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(sb.toString().getBytes("UTF-8"));
            String computedSign = bytesToHex(digest).toUpperCase();

            return computedSign.equals(receivedSign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}