package com.eth.filecoin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @author: aqi
 * @Date: 2021/4/26 15:11
 * @Description:
 */
@Slf4j
public class DemoMain {

    public static void main(String[] args) {
        // 0.01 FIL
        // 0.010000000000000000L
//        String fil = "10000000000000000";
        // 0.1
//        String fil = "100000000000000000";

        // 0.10000000 0000000000
        // 22.10000000 0000000000

        String fil = "666510000000000000000";
        String filString = "666.51000000";

        String filcoin = filString.replace(".","")+"0000000000";
        System.out.println(filcoin);




//        BigDecimal bd=new BigDecimal(fil).setScale(2,RoundingMode.HALF_UP);


        BigDecimal bigDecimal1 = filUtil("666510000000000000000");
//        System.out.println(bigDecimal1);

        System.out.println();
        String str = "shy say123456";
        int length = fil.length();

        if (length < 18) {
            int len = 18 - length;
            for (int i = 0; i < len; i++) {
                fil = "0" + fil;
            }
            log.info("file.length:" + fil.length());
//            fileSubstring(fil);
        } else if (length == 18) {
//            fileSubstring(fil);
        } else if (length > 18) {
            int filMax = length - 18;
            StringBuilder stringBuilder = new StringBuilder(fil);
            stringBuilder.insert(filMax, ".");
            AtomicInteger num = new AtomicInteger();
            int index = stringBuilder.indexOf(".");
            log.info("index：" + index);
            String substring = stringBuilder.substring(0, index + 1 + 8);
            log.info("stringBuilder:" + substring);
        }


        System.out.println(String.format("%-3s", 7).replace(" ", "0"));
        System.out.println(String.format("%02d", BigInteger.valueOf(10000000000000000L)));
        moveToLeft("07", 2);

        BigDecimal bigDecimal = new BigDecimal(fil);
//        log.info("bigDecimal:" + bigDecimal);
    }


    public static BigDecimal filUtil(String fil) {
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


    //返回左移n位字符串方法
    private static void moveToLeft(String str, int position) {
        String str1 = str.substring(position);
        String str2 = str.substring(0, position);
        if (StringUtils.isEmpty(str1)) {
            str1 = "0";
        }
        System.out.println(str1 + "." + str2);
    }


//    public class Main {
//        public static void main(String args[]) {
//            int i;
//
//            for (i = 0; i < 10; i = i + 1)
//                System.out.println("This is i: " + i);
//        }
//    }

    public static Integer id() {
        int intFlag = 0;
        for (int i = 0; i <= 200; i++) {
            intFlag = (int) (Math.random() * 1000000);
            String flag = String.valueOf(intFlag);
            if (flag.length() == 6 && flag.substring(0, 1).equals("9")) {
                System.out.println(intFlag);
                return intFlag;
            }
            intFlag = intFlag + 100000;
            System.out.println(intFlag);
            return intFlag;
        }
        return intFlag;
    }

}
