package com.eth.filecoin.utils;

import com.eth.filecoin.common.CodeConstants;
import java.math.BigDecimal;

/**
 * @author: aqi
 * @Date: 2021/4/26 19:02
 * @Description: fil 转 bigDecimal 8 位小数； 8 位小数String  转 18 位 fil 单位
 */
public class FileUtil {
    /**
     * 18 位 fil 转 8 位BigDecimal
     *
     * @param fil
     * @return
     */
    public static BigDecimal filUtil(String fil) {
        if (fil.equals(CodeConstants.FIL)) {
            return new BigDecimal(CodeConstants.FIL_DECIMAL);
        }
        int length = fil.length();
        String filecoin = null;
        if (length < 18) {
            int len = 18 - length;
            for (int i = 0; i < len; i++) {
                fil = "0" + fil;
            }
            filecoin = fileSubstring(fil);
        } else if (length == 18) {
            filecoin = fileSubstring(fil);
        } else if (length > 18) {
            int filMax = length - 18;
            StringBuilder stringBuilder = new StringBuilder(fil);
            stringBuilder.insert(filMax, ".");
            int index = stringBuilder.indexOf(".");
            filecoin = stringBuilder.substring(0, index + 1 + 8);
        }
        return new BigDecimal(filecoin);
    }

    public static String fileSubstring(String fil) {
        String substring = fil.substring(0, 8);
        String filecoin = "0." + substring;
        return filecoin;
    }

    /**
     * 8 位小数 String 转 18 位 fil 单位
     *
     * @param fil
     * @return
     */
    public static String stringTurnBigDecimal(String fil) {
        String filecoin = fil.replace(".", "");
        while (filecoin.length() <= 18) {
            filecoin += "0";
        }
        return filecoin;
    }

    public static void main(String[] args) {
        String s = stringTurnBigDecimal("0.03");
        System.out.println(s);
    }
}
