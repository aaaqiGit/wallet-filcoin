package com.eth.filecoin.service.impl;

import static com.eth.filecoin.config.CommonConfig.FILFOX_URL;
import static com.eth.filecoin.config.CommonConfig.commonFocusAddress;
import static com.eth.filecoin.config.CommonConfig.commonMainAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.eth.filecoin.admin.CacheFilCoinInfo;
import com.eth.filecoin.admin.EasySend;
import com.eth.filecoin.admin.FilWalletInfo;
import com.eth.filecoin.admin.FilfoxInfo;
import com.eth.filecoin.admin.TianXinCommonInfo;
import com.eth.filecoin.admin.result.TianXinResult;
import com.eth.filecoin.admin.result.TransferAccountsResult;
import com.eth.filecoin.common.CodeConstants;
import com.eth.filecoin.common.FilecoinCnt;
import com.eth.filecoin.entity.FilAddress;
import com.eth.filecoin.entity.FilFocus;
import com.eth.filecoin.entity.FilOrder;
import com.eth.filecoin.entity.FilTake;
import com.eth.filecoin.exception.AssertException;
import com.eth.filecoin.mapper.FilAddressMapper;
import com.eth.filecoin.mapper.FilFocusMapper;
import com.eth.filecoin.mapper.FilOrderMapper;
import com.eth.filecoin.mapper.FilTakeMapper;
import com.eth.filecoin.service.FilecoinService;
import com.eth.filecoin.service.TianXinFilecoinService;
import com.eth.filecoin.utils.CacheRedis;
import com.eth.filecoin.utils.FileUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.ResponseSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author: aqi
 * @Date: 2021/5/9 10:17
 * @Description:
 */
@Service
@Slf4j
public class TianXinFilecoinServiceImpl implements TianXinFilecoinService {
    @Autowired
    private FilAddressMapper filAddressMapper;

    @Autowired
    private FilOrderMapper filOrderMapper;

    @Autowired
    private FilecoinService filecoinService;

    @Autowired
    private CacheRedis cacheRedis;

    @Autowired
    private FilFocusMapper filFocusMapper;

    @Autowired
    private FilTakeMapper filTakeMapper;

//    @Value("${tianxinURL}")
    private String tianxinURL;

//    @Value("${tianxinLoginURL}")
    private String tianxinLoginURL;

//    @Value("${tianxinRechargeURL}")
    private String tianxinRechargeURL;

//    @Value("${tianxinTakeURL}")
    private String tianxinTakeURL;

    /**
     * 充值
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void filRechargeTask() {
        boolean b = cacheRedis.exists(CodeConstants.LOCK_CACHE_FIL);
        if (b) {
            return;
        }
        String token = (String) cacheRedis.getCacheObject(CodeConstants.TIANXIN_TOKEN);
        if (Objects.isNull(token)) {
            token = tianxinLogin();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", CodeConstants.ZERO);
        queryWrapper.eq("state", CodeConstants.STRING_ZERO);
        List<FilAddress> filAddressesList = filAddressMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(filAddressesList)) {
            return;
        }
        for (FilAddress filAddress : filAddressesList) {
            FilOrder order = null;
            FilAddress address = null;
            String orderId = null;
            BigDecimal oldBalance = null;
            TianXinCommonInfo tXTakeInfo = null;
            CacheFilCoinInfo cacheFilCoinInfo = null;
            // 获取当前钱包地址信息
            List<FilfoxInfo> filfoxInfoes = getFileTransactionInfo(filAddress.getAddress());
            FilWalletInfo fileWalletInfo = getFileWalletInfo(filAddress.getAddress());
            if (CollectionUtils.isEmpty(filfoxInfoes) || Objects.isNull(fileWalletInfo)) {
                continue;
            }
            if (Objects.isNull(filAddress.getBalance()) || filAddress.getBalance().toString().equals("0.00000000")) {
                oldBalance = new BigDecimal("0.00");
            } else {
                oldBalance = filAddress.getBalance();
            }
            BigDecimal sumValue = FileUtil.filUtil(fileWalletInfo.getBalance());
            for (FilfoxInfo filfoxInfo : filfoxInfoes) {
                tXTakeInfo = new TianXinCommonInfo();
                BigDecimal newBalance = FileUtil
                        .filUtil(Optional.ofNullable(filfoxInfo.getValue()).orElse(CodeConstants.FIL));
                // 接收方要是自己，金额变动才是充值
                // 该钱包交易 hash 不存在，则把这笔交易推送给 app
                orderId = IdWorker.getIdStr();

                // 时间毫秒转秒级比较
                BigInteger bigInteger = new BigInteger(String.valueOf(filAddress.getUpdated().getTime()));
                BigInteger bigInteger2 = new BigInteger("1000");
                BigInteger[] bigIntegers = bigInteger.divideAndRemainder(bigInteger2);
                if (filfoxInfo.getTimestamp() > Long.parseLong(String.valueOf(bigIntegers[0]))
                        && filfoxInfo.getTo().equals(filAddress.getAddress())) {
                    //  提现 to 收币
                    tXTakeInfo.setCid(filfoxInfo.getCid());
                    tXTakeInfo.setFrom(filfoxInfo.getFrom());
                    tXTakeInfo.setTo(filfoxInfo.getTo());
                    tXTakeInfo.setBalance(newBalance.toString() + "0");
                    tXTakeInfo.setUserId(filAddress.getUserId());
                    tXTakeInfo.setCoin("FIL");
                    tXTakeInfo.setType(CodeConstants.ONE);
                    TianXinResult result = HttpRequest.post(tianxinRechargeURL)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", token)
                            .bodyJson(tXTakeInfo)
                            .execute()
                            .asValue(TianXinResult.class);
                    if (!result.getCode().equals(200)) {
                        return;
                    }
                    address = new FilAddress();
                    address.setId(filAddress.getId());
                    address.setBalance(oldBalance.add(sumValue));
                    address.setUpdated(new Date());
                    filAddressMapper.updateByPrimaryKeySelective(address);
                    order = new FilOrder();
                    order.setId(orderId);
                    order.setIsSync(0);
                    order.setFromAddress(filfoxInfo.getFrom());
                    order.setToAddress(filfoxInfo.getTo());
                    order.setHash(filfoxInfo.getCid());
                    order.setBalance(FileUtil.filUtil(filfoxInfo.getValue()));
                    order.setToAddress(filAddress.getAddress());
                    order.setState(CodeConstants.STRING_ZERO);
                    order.setCreated(new Date());
                    order.setUpdated(new Date());
                    filOrderMapper.insertSelective(order);
                    cacheFilCoinInfo = new CacheFilCoinInfo();
                    cacheFilCoinInfo.setAddress(filAddress.getAddress());
                    cacheFilCoinInfo.setPrivateKey(filAddress.getPrivateKey());
                    cacheFilCoinInfo.setBalance(FileUtil.filUtil(filfoxInfo.getValue()));
                    cacheRedis.leftPush(CodeConstants.CACHE_FIL_FOCUS, cacheFilCoinInfo);
                }
            }
        }
    }


    /**
     * 归集第二步
     */
    @Override
    public void filTake() {
        EasySend send = null;
        FilFocus filFocus = null;
        List<Object> cacheList = null;
        boolean b = cacheRedis.lockOverdueKey(CodeConstants.LOCK_CACHE_FIL);
        if (b) {
            cacheList = cacheRedis.getOpsForList(CodeConstants.CACHE_FIL_FOCUS);
        }
        if (CollectionUtils.isEmpty(cacheList)) {
            // 释放锁
            cacheRedis.unlockOverdueKey(CodeConstants.LOCK_CACHE_FIL);
            return;
        }
        List<CacheFilCoinInfo> cacheFilCoinInfos = BeanUtil.copyProperties(cacheList, CacheFilCoinInfo.class);
        try {
            // 同步 fil-take
            for (CacheFilCoinInfo filAddres : cacheFilCoinInfos) {
                send = new EasySend();
                filFocus = new FilFocus();
                send.setFrom(filAddres.getAddress());
                send.setPrivatekey(filAddres.getPrivateKey());
                send.setTo(commonFocusAddress());
                BigDecimal subtract = Optional.ofNullable(filAddres.getBalance()).orElse(new BigDecimal(CodeConstants.FIL))
                        .subtract(new BigDecimal("0.01"));
                if (subtract.compareTo(new BigDecimal("0.01")) < 1 ? true : false) {
                    continue;
                }
                send.setValue(filAddres.getBalance().toString());
                send.setAddress(filAddres.getAddress());
                send.setType(CodeConstants.STRING_ONE);
                TransferAccountsResult transferAccounts = filecoinService.transferAccounts(send, FilecoinCnt.DEFAULT_TIMEOUT);
                filFocus.setFromAddress(transferAccounts.getFrom());
                filFocus.setCount(FileUtil.filUtil(transferAccounts.getValue()));
                filFocus.setGasFeeCap(transferAccounts.getGasFeeCap());
                filFocus.setGasPremium(transferAccounts.getGasPremium());
                filFocus.setGasLimit(transferAccounts.getGasLimit());
                filFocus.setGasState(CodeConstants.ONE);
                filFocus.setHash(transferAccounts.getCid());
                filFocus.setState(CodeConstants.ONE);
                filFocus.setUpdated(new Date());
                filFocus.setId(IdWorker.getIdStr());
                filFocus.setToAddress(transferAccounts.getTo());
                filFocus.setCreated(new Date());
                filFocusMapper.insertSelective(filFocus);
            }
        } catch (Exception e) {
            log.error("归集第二步,转账失败：" + e.getMessage());
        } finally {
            cacheRedis.deleteObject(CodeConstants.CACHE_FIL_FOCUS);
            // 释放锁
            cacheRedis.unlockOverdueKey(CodeConstants.LOCK_CACHE_FIL);
        }
    }


    /**
     * 提现
     */
    @Override
    public void filTakeTask() {
        String token = (String) cacheRedis.getCacheObject(CodeConstants.TIANXIN_TOKEN);
        if (Objects.isNull(token)) {
            token = tianxinLogin();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_sync", CodeConstants.STRING_ONE);
        queryWrapper.eq("state", CodeConstants.STRING_ZERO);
        List<FilTake> filTakeList = filTakeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(filTakeList)) {
            return;
        }
        for (FilTake filTake : filTakeList) {
            FilTake filTakeEntity = null;
            TianXinCommonInfo tianXinCommonInfo = null;
            // 未同步
            if (filTake.getIsSync().equals(CodeConstants.ONE)) {
                tianXinCommonInfo = new TianXinCommonInfo();
                // 发送方是备付地址推送
                if (filTake.getFromAddress().equals(commonMainAddress())) {
                    //  提现 to 收币
                    tianXinCommonInfo.setCid(filTake.getHash());
                    tianXinCommonInfo.setFrom(filTake.getFromAddress());
                    tianXinCommonInfo.setTo(filTake.getToAddress());
                    String balance = FileUtil.stringTurnBigDecimal(filTake.getCount().toString()) + "0";
                    BigDecimal bigDecimal = new BigDecimal(balance);
                    tianXinCommonInfo.setBalance(bigDecimal.toString());
                    tianXinCommonInfo.setUserId(filTake.getUserId());
                    tianXinCommonInfo.setGetcashId(filTake.getCurrency());
                    tianXinCommonInfo.setCoin("FIL");
                    tianXinCommonInfo.setType(CodeConstants.INTEGER_TWO);
                    TianXinResult result = HttpRequest.post(tianxinTakeURL)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", token)
                            .bodyJson(tianXinCommonInfo)
                            .execute()
                            .asValue(TianXinResult.class);

                    if (result.getCode().equals(200)) {
                        filTakeEntity = new FilTake();
                        filTakeEntity.setId(filTake.getId());
                        filTakeEntity.setIsSync(0);
                        filTakeEntity.setUpdated(new Date());
                        filTakeMapper.updateByPrimaryKeySelective(filTakeEntity);
                    }
                }
            }
        }
    }

    /**
     * fil 浏览器获取钱包交易信息
     *
     * @param address
     * @return
     */
    public List<FilfoxInfo> getFileTransactionInfo(String address) {
        List<FilfoxInfo> filfoxInfoes = new ArrayList<>(16);
        // fil 浏览器拿到钱包信息
        String resultFilfoxInfo = HttpRequest.get(FILFOX_URL + address + "/messages")
                .execute()
                .onFailed((request, e) -> {
                    log.info("爬fil 浏览器钱包交易记录失败：" + e.getMessage());
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

    /**
     * fil 浏览器获取地址钱包信息
     *
     * @param address
     * @return
     */
    @Override
    public FilWalletInfo getFileWalletInfo(String address) {
        // fil 浏览器拿到钱包信息
        FilWalletInfo resultFilWalletInfo = HttpRequest.get(URI.create(FILFOX_URL + address))
                .execute()
                .onFailed((request, e) -> {
                    log.info("爬fil 浏览器钱包信息失败：" + e.getMessage());
                })
                .asValue(FilWalletInfo.class);
        return resultFilWalletInfo;
    }

    /**
     * 归集同步推送地址, 天芯 old
     */
    @Override
    public void filFocusSynch() {
        FilFocus focuss = null;
        List<FilFocus> filFocusList = new ArrayList<>(16);
        String token = (String) cacheRedis.getCacheObject(CodeConstants.TIANXIN_TOKEN);
        if (Objects.isNull(token)) {
            token = tianxinLogin();
        }
        List<Object> cacheList = null;
        boolean b = cacheRedis.exists(CodeConstants.LOCK_CACHE_FIL);
        cacheList = cacheRedis.getOpsForList(CodeConstants.CACHE_FIL_FOCUS);
        if (b || CollectionUtils.isEmpty(cacheList)) {
            return;
        }
        // 过滤只推送未成功的
        List<FilFocus> cacheFilCoinInfos = BeanUtil.copyProperties(cacheList, FilFocus.class);
        for (FilFocus filFocus : cacheFilCoinInfos) {
            if (Objects.nonNull(filFocus.getState()) && !filFocus.getState().equals(3)) {
                FilFocus filFocusReq = BeanUtil.copyProperties(filFocus, FilFocus.class);
                filFocusList.add(filFocusReq);
            }
        }
        /**
         * 币种类，全大写
         * private String coin;
         *      0、 3
         */
        TianXinResult tianXinResult = HttpRequest.post(tianxinURL)
                .addHeader("Authorization", token)
                .bodyJson(filFocusList)
                .execute()
                .asValue(TianXinResult.class);
        // 成功更新状态
        if (tianXinResult.getCode().equals(200)) {
            focuss = new FilFocus();
            for (FilFocus focus : filFocusList) {
                focuss.setId(focus.getId());
                focuss.setState(3);
                filFocusMapper.updateByPrimaryKeySelective(focuss);
            }
        }
        //  清除 cache
        cacheRedis.cleanOpsForList(CodeConstants.CACHE_FIL_FOCUS);
        log.info("归集同步推送：", "");
    }

    /**
     * 获取天芯 token
     *
     * @return
     */
    public String tianxinLogin() {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("username", "aqi");
        jsonParam.put("password", "123456");
        Optional<String> optional = HttpRequest.post(tianxinLoginURL)
                .bodyJson(jsonParam)
                .execute()
                .onSuccessOpt(ResponseSpec::asString);
        JSONObject jsonObject = Optional.ofNullable(JSONObject.parseObject(optional.get())).orElse(null);
        if (Objects.isNull(jsonObject)) {
            throw new AssertException("天芯获取 token 失败！");
        }
        String token = (String) jsonObject.getJSONObject("data").get("token");
        if (!StringUtil.isBlank(token)) {
            cacheRedis.deleteObject(CodeConstants.TIANXIN_TOKEN);
        }
        cacheRedis.set(CodeConstants.TIANXIN_TOKEN, token, 3600);
        return token;
    }

}
