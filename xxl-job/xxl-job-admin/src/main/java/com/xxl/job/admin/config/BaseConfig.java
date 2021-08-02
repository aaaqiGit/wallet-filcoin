//package com.xxl.job.admin.config;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * @Auther: zhangqi
// * @Date: 2020/2/12 19:06
// * @Description:  系统环境
// */
//@Component
//public class BaseConfig implements InitializingBean {
//
//    /**
//     * 运行环境 1-测试 2-正式
//     */
//    public static int SYS_ENVION;
//
//    @Value("${system.environmental}")
//    private int sysEnvion;
//
//    @Override
//    public void afterPropertiesSet() {
//        SYS_ENVION = this.sysEnvion;
//    }
//}
