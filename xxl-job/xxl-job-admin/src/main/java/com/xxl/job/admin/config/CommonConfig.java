//package com.xxl.job.admin.config;
//
//import com.xxl.job.admin.common.CodeConstants;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * @Auther: zhangqi
// * @Date: 2020/2/12 19:03
// * @Description: 通用配置
// */
//@Component
//public class CommonConfig extends BaseConfig implements InitializingBean {
//
//    public static String TEST_APP_CALLBACK_URL;
//    public static String ONLINE_APP_CALLBACK_URL;
//
//    @Value("$(testAppCallbackURL)")
//    public String testAppCallbackURL;
//
//    @Value("$(onlineAppCallbackURL)")
//    public String onlineAppCallbackURL;
//
//
//    /**
//     * 回调地址
//     *
//     * @return
//     */
//    public static String commonFocusAddress() {
//        String key = CommonConfig.TEST_APP_CALLBACK_URL;
//        // 运行环境 1-测试 2-正式
//        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
//            key = CommonConfig.ONLINE_APP_CALLBACK_URL;
//        }
//        return key;
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        TEST_APP_CALLBACK_URL = this.testAppCallbackURL;
//        ONLINE_APP_CALLBACK_URL = this.onlineAppCallbackURL;
//    }
//}
