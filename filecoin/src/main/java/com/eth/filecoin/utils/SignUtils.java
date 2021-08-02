package com.eth.filecoin.utils;

import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class SignUtils {



    public static Map<String, String> splitMapFromString(String s) {
        String[] res = s.split("&");
        Map<String, String> map = new HashMap<>();
        for (String re : res) {
            String[] values = re.split("=");
            map.put(values[0], values[1]);
        }
        return map;
    }

    /**
     * 验签
     * @param str 被验证字符串  aaa=xxx&bbb=xxxx&sign=xxxx
     * @param appId 加密key
     * @return
     * @throws Exception
     */
    public static boolean isNotValid(String str,String appId) throws Exception {
        //原数据
        String originStr = str.substring(0, str.lastIndexOf("&"));
        //原来的签名
        String originSign = str.substring(str.lastIndexOf("=") + 1);

        //本地根据 原数据和key得到本地的签名
        String sign = getSignature(originStr, appId);
        System.out.println("originStr:" + originStr);
        System.out.println("originSign:" + originSign);
        System.out.println("currentSign:" + sign);

        //验证签名是否一致
        if (sign.equals(originSign)) {
            return false;
        }
        return true;
    }





    public static String getSignature(String data, String key) throws Exception {
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return bytArrayToHex(rawHmac);
    }

    private static String bytArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (byte b : a) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

}
