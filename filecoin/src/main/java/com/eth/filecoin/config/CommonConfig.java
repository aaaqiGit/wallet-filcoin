package com.eth.filecoin.config;

import com.eth.filecoin.common.CodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: zhangqi
 * @Date: 2020/2/12 19:03
 * @Description: 通用配置
 */
@Slf4j
@Component
public class CommonConfig extends BaseConfig implements InitializingBean {

    public static String PROJECT_ID;
    public static String SECRET;
    public static String FILECOIN_URL;

    public static String TEST_APP_CALLBACK_URL;
    public static String ONLINE_APP_CALLBACK_URL;

    public static String FILFOX_URL;



    /**
     * 正式 key
     */
    public static String ONLINE_KEY;
    public static String ONLINE_FOCUS_ADDRESS;
    public static String ONLINE_FOCUS_PRIVATE_KEY;
    public static String ONLINE_MAIN_ADDRESS;
    public static String ONLINE_MAIN_KEY;

    /**
     * 测试key
     */
    public static String TEST_KEY;
    public static String TEST_FOCUS_ADDRESS;
    public static String TEST_FOCUS_PRIVATE_KEY;
    public static String TEST_MAIN_ADDRESS;
    public static String TEST_MAIN_KEY;

    @Value("${filfoxUrl}")
    public String filfoxUrl;

    @Value("${test.key}")
    public String testKey;

    /**
     * 测试归集
     */
    @Value("${test.focusAddress}")
    public String testFocusAddress;

    @Value("${test.focusPrivateKey}")
    public String testFocusPrivateKey;

    /**
     * 测试备付
     */
    @Value("${test.mainAddress}")
    public String testMainAddress;

    @Value("${test.mainKey}")
    public String testMainKey;

    @Value("${online.key}")
    public String onlineKey;

    /**
     * 生产归集
     */
    @Value("${online.focusAddress}")
    public String onlineFocusAddress;

    @Value("${online.focusPrivateKey}")
    public String onlineFocusPrivateKey;

    /**
     * 生产备付
     */
    @Value("${online.mainAddress}")
    public String onlineMainAddress;

    @Value("${online.mainKey}")
    public String onlineMainKey;

    @Value("${test.appCallbackURL}")
    public String testAppCallbackURL;

    @Value("${online.appCallbackURL}")
    public String onlineAppCallbackURL;

    @Value("${projectId}")
    public String projectId;

    @Value("${secret}")
    public String secret;

    @Value("${filecoinUrl}")
    public String filecoinUrl;

    /**
     * 归集 Address
     *
     * @return
     */
    public static String commonFocusAddress() {
        String key = CommonConfig.TEST_FOCUS_ADDRESS;
        // 运行环境 1-测试 2-正式
        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
            key = CommonConfig.ONLINE_FOCUS_ADDRESS;
        }
        log.info("commonFocusAddress：" + key);
        return key;
    }

    /**
     * 归集私钥 PrivateKey
     *
     * @return
     */
    public static String commonFocusPrivateKey() {
        String key = CommonConfig.TEST_FOCUS_PRIVATE_KEY;
        // 运行环境 1-测试 2-正式
        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
            key = CommonConfig.ONLINE_FOCUS_PRIVATE_KEY;
        }
        log.info("commonFocusPrivateKey：" + key);
        return key;
    }

    /**
     * 备付 Address
     *
     * @return
     */
    public static String commonMainAddress() {
        String key = CommonConfig.TEST_MAIN_ADDRESS;
        // 运行环境 1-测试 2-正式
        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
            key = CommonConfig.ONLINE_MAIN_ADDRESS;
        }
        log.info("commonMainAddress：" + key);
        return key;
    }

    /**
     * 备付 PrivateKey
     *
     * @return
     */
    public static String commonMainKey() {
        String key = CommonConfig.TEST_MAIN_KEY;
        // 运行环境 1-测试 2-正式
        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
            key = CommonConfig.ONLINE_MAIN_KEY;
        }
        log.info("commonMainKey：" + key);
        return key;
    }

    /**
     * app回调地址
     *
     * @return
     */
    public static String commonAppAddress() {
        String key = CommonConfig.TEST_APP_CALLBACK_URL;
        // 运行环境 1-测试 2-正式
        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
            key = CommonConfig.ONLINE_APP_CALLBACK_URL;
        }
        return key;
    }

    /**
     * 加解密 key
     *
     * @return
     */
    public static String commonAppKey() {
        String key = CommonConfig.TEST_KEY;
        // 运行环境 1-测试 2-正式
        if (CodeConstants.SYS_ENVIRONMENTAL_2 == CommonConfig.SYS_ENVION) {
            key = CommonConfig.ONLINE_KEY;
        }
        return key;
    }

    @Override
    public void afterPropertiesSet() {
        TEST_KEY = this.testKey;
        TEST_MAIN_KEY = this.testMainKey;
        TEST_MAIN_ADDRESS = this.testMainAddress;
        TEST_FOCUS_PRIVATE_KEY = this.testFocusPrivateKey;
        TEST_FOCUS_ADDRESS = this.testFocusAddress;
        ONLINE_KEY = this.onlineKey;
        ONLINE_FOCUS_ADDRESS = this.onlineFocusAddress;
        ONLINE_FOCUS_PRIVATE_KEY = this.onlineFocusPrivateKey;
        ONLINE_MAIN_ADDRESS = this.onlineMainAddress;
        ONLINE_MAIN_KEY = this.onlineKey;

        PROJECT_ID = this.projectId;
        SECRET = this.secret;
        FILECOIN_URL = this.filecoinUrl;

        TEST_APP_CALLBACK_URL = this.testAppCallbackURL;
        ONLINE_APP_CALLBACK_URL = this.onlineAppCallbackURL;

        FILFOX_URL = this.filfoxUrl;
    }
}
