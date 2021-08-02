package com.eth.filecoin.test;

import static com.eth.filecoin.config.CommonConfig.FILFOX_URL;
import static com.eth.filecoin.config.CommonConfig.commonAppAddress;
import static com.eth.filecoin.config.CommonConfig.commonAppKey;
import static com.eth.filecoin.config.CommonConfig.commonFocusPrivateKey;
import static com.eth.filecoin.config.CommonConfig.commonMainAddress;
import static com.eth.filecoin.config.CommonConfig.commonMainKey;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.eth.filecoin.admin.AuthenRequest;
import com.eth.filecoin.admin.FilfoxInfo;
import com.eth.filecoin.admin.Transaction;
import com.eth.filecoin.admin.result.AppCommonResult;
import com.eth.filecoin.common.CodeConstants;
import com.eth.filecoin.entity.FilAddress;
import com.eth.filecoin.entity.FilTake;
import com.eth.filecoin.exception.AssertException;
import com.eth.filecoin.mapper.FilAddressMapper;
import com.eth.filecoin.mapper.FilTakeMapper;
import com.eth.filecoin.service.impl.FilecoinTaskServiceImpl;
import com.eth.filecoin.utils.CacheRedis;
import com.eth.filecoin.utils.FileUtil;
import com.eth.filecoin.utils.MD5Util;
import com.eth.filecoin.utils.MD5Utils;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.ResponseSpec;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: zhangqi
 * @Date: 2021/4/24 11:48
 * @Description:
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class Demo {

    @Autowired
    private CacheRedis cacheRedis;

    @Autowired
    private FilecoinTaskServiceImpl filecoinTaskService;

    @Resource
    private FilAddressMapper filAddressMapper;

    @Autowired
    private FilTakeMapper filTakeMapper;

    @Test
    public void contextLoads() {
        log.info("start!");
    }


    @Test
    public void demo1() {
        cacheRedis.set("demo", "test-redis");
    }

    @Test
    public void demo2() {
        log.info("commonAppAddress();:" + commonAppAddress());
        log.info("commonFocusPrivateKey();:" + commonFocusPrivateKey());
        log.info("commonMainAddress();:" + commonMainAddress());
        log.info("commonMainKey();:" + commonMainKey());
        log.info("commonAppKey();:" + commonAppKey());
    }

    @Test
    public void demo3() throws Exception {
        String data = String.format(
                "address=%s&amount=%s&hash=%s&orderId=%s&userId=%s&coin=%s&type=%s",
                "地址", "币数量", "hash", "订单 id", "用户 id", "币种", "充值1"
        );
// address=地址&amount=币数量&hash=hash&orderId=订单 id&userId=用户 id&coin=币种&type=充值1&sgin=0783309e6b7f42a892182c529b6fdf76
//        log.info("data:"+data);

        String sign = data + "&sign=" + "1111";
        log.info("sign:" + sign);

        String md5 = MD5Utils.md5(sign);
        log.info("md5:" + md5);

        data = data + "&sign=" + md5;
        log.info("加密：" + data);

        String encodeStr = Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        log.info("base64:" + encodeStr);
    }

    @Test
    public void demo4() throws JSONException {
        String json = "{\"totalCount\":1,\"messages\":[{\"cid\":\"cid\",\"height\":695359,\"timestamp\":1619167170,\"from\":\"from\",\"to\":\"to\",\"nonce\":0,\"value\":\"10000000000000000\",\"method\":\"Send\",\"receipt\":{\"exitCode\":0}}],\"methods\":[\"Send\"]}";

        log.info("json:" + json);

        JSONObject jsonObject = JSONObject.parseObject(json);//转JSONObject对象

        log.info("" + JSONObject.toJSONString(jsonObject));

        JSONArray messages = jsonObject.getJSONArray("messages");

        JSONObject jo = (JSONObject) messages.get(0);

        FilfoxInfo filfoxInfo = JSONObject.toJavaObject(jo, FilfoxInfo.class);

        log.info("" + JSONObject.toJSONString(filfoxInfo));
    }

    @Test
    public void demo5() {
//        String key = "address=地址&amount=币数量&hash=hash&orderId=订单 id&userId=用户 id&coin=币种&type=充值1&sign=0783309e6b7f42a892182c529b6fdf76";
        String key = "address=地址&amount=币数量&hash=hash&orderId=订单 id&userId=用户 id&coin=币种&type=充值1";
        String sign = filecoinTaskService.getSign(key);
        log.info("sign:" + sign);
    }

    @Test
    public void demo6() {
        String execute = "{\"jsonrpc\":\"2.0\",\"result\":{\"Version\":0,\"To\":\"to\",\"From\":\"from\",\"Nonce\":0,\"Value\":\"10000000000000000\",\"GasLimit\":2626647,\"GasFeeCap\":\"2664994572\",\"GasPremium\":\"99878\",\"Method\":0,\"Params\":null,\"CID\":{\"/\":\"cid\"}},\"id\":1}";
        JSONObject executeJson = JSON.parseObject(execute);
        JSONObject resultInfo = executeJson.getJSONObject("result");
        Transaction result = JSONObject.toJavaObject(resultInfo, Transaction.class);
        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void demo7() {
//        headers.add("appKey", appKey);
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
        long time = System.currentTimeMillis();
//        //组装请求体
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//        map.add("appKey", appKey);
//        map.add("timestamp", time + "");
//        map.add("sign", MD5Util.md5(appKey + time + appSecret));
//        map.add("appId", appIdk);
//        log.info("getToken_K=-----"+appIdk);

        AuthenRequest authenContext = new AuthenRequest();
        authenContext.setCallBackUrl("http://www.baidu.com");
        authenContext.setUserId("111");
        authenContext.setFilKey("333");

        authenContext.setFilId("222");
        authenContext.setTimestamp(time);
        authenContext.setFilSecret("444");
        authenContext.setSign(MD5Util.md5("222" + time + "444"));

        System.out.println(JSONObject.toJSONString(authenContext));

        Optional<String> optional = HttpRequest.post("http://localhost:8088/api+" + "/222" + "+/auth")
                .addHeader()
                .bodyJson(authenContext)
                .execute()
                .onSuccessOpt(ResponseSpec::asString);

        log.info("");
    }

    @Test
    public void demo8() {
        Optional<String> optional = HttpRequest.post(URI.create("www.baidu.com"))
                .disableSslValidation()
                .addHeader("Content-Type", "application/json")
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .execute()
                .onSuccessOpt(ResponseSpec::asString);

        System.out.println("optional:" + optional.get());
    }

    @Test
    public void demo9() {
        String data = String.format(
                "address=%s&amount=%s&hash=%s&orderId=%s&userId=%s&coin=%s&type=%s&from=%s&to=%s",
                "111111", "22.66", "cid", "orderId",
                "22222",
                "FIL", CodeConstants.ONE, "from()", "to()"
        );
        String encodeStr = getSign(data);

        log.info("encodeStr:" + encodeStr);
//                    String callBackUrl = Optional.ofNullable(filAddress.getCallBackUrl())
//                            .orElse(commonAppAddress());
        // 通知 app 后台
        AppCommonResult result = HttpRequest.post("www.baidu.com")
                .addHeader("Content-Type", "application/json")
                .connectTimeout(Duration.ofMinutes(1))
                .formBuilder()
                .add("input", encodeStr)
                .execute()
                .asValue(AppCommonResult.class);
        if (Objects.isNull(result.getSuccess()) || !result.getSuccess()) {
            log.error("推送 app 失败！");
        }
    }

    // 充值变动
    @Test
    public void demo09() {
        String data = String.format(
                "address=%s&amount=%s&hash=%s&orderId=%s&userId=%s&coin=%s&type=%s&from=%s&to=%s",
                "1111", "2200.66", "cid", "orderId",
                "11111",
                "FIL", CodeConstants.ONE, "from()", "to()"
        );
        String encodeStr = getSign(data);
//        String callBackUrl = Optional.ofNullable(filAddress.getCallBackUrl())
//                .orElse(commonAppAddress());
        // 通知 app 后台
        AppCommonResult result = HttpRequest.post("http://www.baidu.com")
                .addHeader("Content-Type", "application/json")
                .connectTimeout(Duration.ofMinutes(1))
                .formBuilder()
                .add("input", encodeStr)
                .execute()
                .asValue(AppCommonResult.class);
        if (Objects.isNull(result.getSuccess()) || !result.getSuccess()) {
            log.error("推送 app 失败！");
        }
    }

    // 提现
    @Test
    public void demo010() {
        String data = String.format(
                "address=%s&amount=%s&hash=%s&orderId=%s&userId=%s&coin=%s&type=%s&from=%s&to=%s",
                "1111", "2200.66", "cid", "orderId",
                "22222",
                "FIL", CodeConstants.INTEGER_TWO, "from()", "to()"
        );
        String encodeStr = getSign(data);
//        String callBackUrl = Optional.ofNullable(filAddress.getCallBackUrl())
//                .orElse(commonAppAddress());
        // 通知 app 后台
        AppCommonResult result = HttpRequest.post("http://www.baidu.com")
                .addHeader("Content-Type", "application/json")
                .connectTimeout(Duration.ofMinutes(1))
                .formBuilder()
                .add("input", encodeStr)
                .execute()
                .asValue(AppCommonResult.class);
        if (Objects.isNull(result.getSuccess()) || !result.getSuccess()) {
            log.error("推送 app 失败！");
        }
    }

    @Test
    public void demo10() {
//        FilOrder fileOrder = new FilOrder();
//        fileOrder.setId(IdWorker.getIdStr());
//        fileOrder.setFromAddress("werwerwerwe");
//        fileOrder.setToAddress("werwerwerew");
//        fileOrder.setBalance(new BigDecimal("0.22"));
//        fileOrder.setHash("sfwefwefewf");
//        fileOrder.setRemark("test");
//        fileOrder.setCurrency(0);
//        fileOrder.setCreated(new Date());
//        fileOrder.setUpdated(new Date());
//
//        for (int i = 0; i < 5; i++) {
//            cacheRedis.leftPush("test", fileOrder);
//        }

        cacheRedis.deleteObject("test");
//
//        List<Object> test = cacheRedis.getOpsForList("test");
//
//        List<FilOrder> filOrderList = BeanUtil.copyProperties(test, FilOrder.class);
//
//        System.out.println(JSONObject.toJSONString(filOrderList));


    }

    // 测试交易维护余额
    @Test
    public void demo11() {
        String walletAddress = "testAddress";
        // 维护余额
        FilAddress filAddr = filAddressMapper.selectByAddress(walletAddress);
        BigDecimal turnBalance = FileUtil.filUtil("1000000000000000000");
        // 1，0 充值，2，1 提现, 提现扣除余额
        if ("1".equals(CodeConstants.STRING_TWO)) {
//            filOrder.setLocked(1);
            filAddr.setAddress(walletAddress);
            if (Objects.isNull(filAddr) || Objects.isNull(filAddr.getBalance()) || filAddr.getBalance().compareTo(new BigDecimal(CodeConstants.FIL)) == 0) {
                filAddr.setBalance(turnBalance);
            } else {
                filAddr.setBalance(filAddr.getBalance().subtract(turnBalance));
            }
            filAddressMapper.updateById(filAddr);
        } else {
            filAddr.setBalance(filAddr.getBalance().add(turnBalance));
            filAddressMapper.updateById(filAddr);
        }
    }

    @Test
    public void demo12() {
//        {"callBackUrl":"http://www.baidu.com",
//        "from":"from",
//        "getcashId":0,"orderId":"1111",
//        "to":"to",
//        "type":"1","userId":"11111","value":"0.20000000"}

        FilTake filTake = new FilTake();
        filTake.setId(IdWorker.getIdStr());
        filTake.setFromAddress("1111");
        filTake.setToAddress("2222");
        filTake.setCount(new BigDecimal("0"));
        filTake.setHash("22");
        filTake.setIsSync(1);
        filTake.setCallBackUrl("http://www.baidu.com");
        filTake.setUserId("1111");
        filTake.setOrderId("2222");
        filTake.setState(CodeConstants.STRING_ONE);
        filTake.setCreated(new Date());
        filTake.setUpdated(new Date());

        filTakeMapper.insertSelective(filTake);
    }

    /**
     * 获取签名
     *
     * @param data
     * @return
     */
    public String getSign(String data) {
        try {
            String signature = MD5Utils.md5(data + "&sign=" + commonAppKey());
            data = data + "&sign=" + signature;
        } catch (Exception e) {
            log.error("签名异常：" + e.getMessage());
            throw new AssertException("500", "签名异常");
        }
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }


    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        char[] password = "password".toCharArray(); // 这里可以使用任意密码
//        KeyStore keyStore = newEmptyKeyStore(password);
        KeyStore keyStore = KeyStore.getInstance("password");
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }


    private static class TrustAllCerts implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }


    @Test
    public void demo0909() {
        List<FilfoxInfo> aitia = getFileTransactionInfo("111");
        System.out.println(aitia);
    }

    /**
     * fil 浏览器获取钱包交易信息
     *
     * @param address
     * @return
     */
    public static List<FilfoxInfo> getFileTransactionInfo(String address) {
        List<FilfoxInfo> filfoxInfoes = new ArrayList<>(16);
        // fil 浏览器拿到钱包信息
        String resultFilfoxInfo = HttpRequest.get(FILFOX_URL + address + "/messages")
                .execute()
                .onFailed((request, e) -> {
//                    log.info("爬fil 浏览器钱包交易记录失败：" + e.getMessage());
                })
                .onSuccess(ResponseSpec::asString);
        JSONObject jsonObject = JSONObject.parseObject(resultFilfoxInfo);
        if (jsonObject.getJSONArray("messages").isEmpty()) {
            return null;
        }
        JSONArray messages = jsonObject.getJSONArray("messages");

        for (Object array : messages.stream().toArray()) {
            FilfoxInfo filfoxInfo = JSONObject
                    .toJavaObject((JSONObject) array, FilfoxInfo.class);
            filfoxInfoes.add(filfoxInfo);
        }
        return filfoxInfoes;
    }


}
