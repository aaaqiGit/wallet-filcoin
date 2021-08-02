package com.eth.filecoin.service.impl;

import static com.eth.filecoin.config.CommonConfig.FILFOX_URL;
import static com.eth.filecoin.config.CommonConfig.commonAppAddress;
import static com.eth.filecoin.config.CommonConfig.commonAppKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.eth.filecoin.admin.CacheFilCoinInfo;
import com.eth.filecoin.admin.EasySend;
import com.eth.filecoin.admin.FilWalletInfo;
import com.eth.filecoin.admin.FilfoxInfo;
import com.eth.filecoin.admin.result.AppCommonResult;
import com.eth.filecoin.common.CodeConstants;
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
import com.eth.filecoin.service.FilecoinTaskService;
import com.eth.filecoin.utils.CacheRedis;
import com.eth.filecoin.utils.FileUtil;
import com.eth.filecoin.utils.MD5Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.ResponseSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author: aqi
 * @Date: 2021/4/28 10:03
 * @Description:
 */
@Service
@Slf4j
public class FilecoinTaskServiceImpl implements FilecoinTaskService {

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

    /**
     * 充值
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void filRechargeTask() {
        boolean b = cacheRedis.exists(CodeConstants.LOCK_CACHE_FIL_SQ);
        if (b) {
            return;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", CodeConstants.ZERO);
        queryWrapper.eq("state", CodeConstants.STRING_ONE);
        List<FilAddress> filAddressesList = filAddressMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(filAddressesList)) {
            return;
        }
        for (FilAddress filAddress : filAddressesList) {
            FilOrder order = null;
            FilAddress address = null;
            String orderId = null;
            BigDecimal oldBalance = null;
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
                BigDecimal newBalance = FileUtil
                        .filUtil(Optional.ofNullable(filfoxInfo.getValue()).orElse(CodeConstants.FIL));
                // -1小于，0相等，1大于;coin 币种  FIL;type  1，充值，2，提现
                // 接收方是自己，并且交易记录时间大于自己的更新时间
                orderId = IdWorker.getIdStr();
                // 时间毫秒转秒级比较
                BigInteger bigInteger = new BigInteger(String.valueOf(filAddress.getUpdated().getTime()));
                BigInteger bigInteger2 = new BigInteger("1000");
                BigInteger[] bigIntegers = bigInteger.divideAndRemainder(bigInteger2);
//                if (filfoxInfo.getTimestamp() > Long.parseLong(String.valueOf(bigIntegers[0]))
//                        && filfoxInfo.getTo().equals(filAddress.getAddress())) {
                if (true) {
                    String data = String.format(
                            "address=%s&amount=%s&hash=%s&orderId=%s&userId=%s&coin=%s&type=%s&from=%s&to=%s",
                            filAddress.getAddress(), newBalance, filfoxInfo.getCid(), orderId,
                            filAddress.getUserId(),
                            "FIL", CodeConstants.ONE, filfoxInfo.getFrom(), filfoxInfo.getTo()
                    );
                    String encodeStr = getSign(data);
                    String callBackUrl = Optional.ofNullable(filAddress.getCallBackUrl())
                            .orElse(commonAppAddress());
                    // 通知 app 后台
//                    AppCommonResult result = HttpRequest.post(callBackUrl)
//                            .addHeader("Content-Type", "application/json")
//                            .connectTimeout(Duration.ofMinutes(1))
//                            .formBuilder()
//                            .add("input", encodeStr)
//                            .execute()
//                            .asValue(AppCommonResult.class);
//                    if (Objects.isNull(result.getSuccess()) || !result.getSuccess()) {
//                        log.error("推送 app 失败！");
//                    }
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
//                    filOrderMapper.insertSelective(order);
                    cacheFilCoinInfo = new CacheFilCoinInfo();
                    cacheFilCoinInfo.setAddress(filAddress.getAddress());
                    cacheFilCoinInfo.setPrivateKey(filAddress.getPrivateKey());
                    cacheFilCoinInfo.setBalance(FileUtil.filUtil(filfoxInfo.getValue()));
                    cacheRedis.leftPush(CodeConstants.CACHE_FIL_FOCUS_SQ, cacheFilCoinInfo);
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
        boolean b = cacheRedis.lockOverdueKey(CodeConstants.LOCK_CACHE_FIL_SQ);
        if (b) {
            cacheList = cacheRedis.getOpsForList(CodeConstants.CACHE_FIL_FOCUS_SQ);
        }
        if (CollectionUtils.isEmpty(cacheList)) {
            // 释放锁
            cacheRedis.unlockOverdueKey(CodeConstants.LOCK_CACHE_FIL_SQ);
            return;
        }
        List<CacheFilCoinInfo> cacheFilCoinInfos = BeanUtil.copyProperties(cacheList, CacheFilCoinInfo.class);
        try {
            // 同步 fil-take
            for (CacheFilCoinInfo filAddres : cacheFilCoinInfos) {
//            if (true) {
                send = new EasySend();
                filFocus = new FilFocus();
                send.setFrom(filAddres.getAddress());
                send.setPrivatekey(filAddres.getPrivateKey());
//                send.setFrom("f15tdfj3cq3uykihnc5l7lbj6ftg3xtd2cqn47dfq");
//                send.setPrivatekey("d95c9784912fc81f96cbefa1bd43a63cfde3af7e421ff4dbf2ced5d73ab88ff0");
//                send.setValue("0.3000000");
//                send.setTo("f1g6ls7jlfrqu65vi4rphg7eesgs245jck47aitia");
//                send.setTo(commonFocusAddress());
                BigDecimal subtract = Optional.ofNullable(filAddres.getBalance()).orElse(new BigDecimal(CodeConstants.FIL))
                        .subtract(new BigDecimal("0.01"));
                if (subtract.compareTo(new BigDecimal("0.01")) < 1 ? true : false) {
                    continue;
                }
                send.setValue(filAddres.getBalance().toString());
                send.setAddress(filAddres.getAddress());
                send.setType(CodeConstants.STRING_ONE);
//                Thread.sleep(30000L);
//                TransferAccountsResult transferAccounts = filecoinService.transferAccounts(send, FilecoinCnt.DEFAULT_TIMEOUT);
                filFocus.setFromAddress("transferAccounts.getFrom()");
//                filFocus.setCount(FileUtil.filUtil(transferAccounts.getValue()));
                filFocus.setGasFeeCap("transferAccounts.getGasFeeCap()");
                filFocus.setGasPremium("transferAccounts.getGasPremium()");
//                filFocus.setGasLimit("transferAccounts.getGasLimit()");
                filFocus.setGasState(CodeConstants.ONE);
                filFocus.setHash("transferAccounts.getCid()");
                filFocus.setState(CodeConstants.ONE);
                filFocus.setUpdated(new Date());
                filFocus.setId(IdWorker.getIdStr());
                filFocus.setToAddress("transferAccounts.getTo()");
                filFocus.setCreated(new Date());
                filFocusMapper.insertSelective(filFocus);
            }
        } catch (Exception e) {
            log.error("归集第二步,转账失败：" + e.getMessage());
        } finally {
            cacheRedis.deleteObject(CodeConstants.CACHE_FIL_FOCUS_SQ);
            // 释放锁
            cacheRedis.unlockOverdueKey(CodeConstants.LOCK_CACHE_FIL_SQ);
        }
    }

    /**
     * 提现
     */
    @Override
    public void filTakeTask() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_sync", CodeConstants.STRING_ONE);
        queryWrapper.eq("state", CodeConstants.STRING_ONE);
        List<FilTake> filTakeList = filTakeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(filTakeList)) {
            return;
        }
        for (FilTake filTake : filTakeList) {
            FilTake filTakeEntity = null;
            // 未同步
            if (filTake.getIsSync().equals(CodeConstants.ONE)) {
                //  提现 to 收币
                String data = String.format(
                        "address=%s&amount=%s&hash=%s&orderId=%s&userId=%s&coin=%s&type=%s&from=%s&to=%s",
                        filTake.getToAddress(), filTake.getCount(), filTake.getHash(), filTake.getOrderId(),
                        filTake.getUserId(),
                        "FIL", CodeConstants.INTEGER_TWO, filTake.getFromAddress(), filTake.getToAddress()
                );
                String encodeStr = getSign(data);
                String callBackUrl = Optional.ofNullable(filTake.getCallBackUrl())
                        .orElse(commonAppAddress());
                AppCommonResult result = HttpRequest.post(callBackUrl)
                        .addHeader("Content-Type", "application/json")
                        .connectTimeout(Duration.ofMinutes(1))
                        .formBuilder()
                        .add("input", encodeStr)
                        .execute()
                        .asValue(AppCommonResult.class);
                log.info("result: " + JSONObject.toJSONString(result));
                if (result.getSuccess()) {
                    filTakeEntity = new FilTake();
                    filTakeEntity.setId(filTake.getId());
                    filTakeEntity.setIsSync(0);
                    filTakeEntity.setUpdated(new Date());
                    filTakeMapper.updateByPrimaryKeySelective(filTakeEntity);
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
}
