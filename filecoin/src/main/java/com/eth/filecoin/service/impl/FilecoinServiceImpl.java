package com.eth.filecoin.service.impl;

import static com.eth.filecoin.config.CommonConfig.FILECOIN_URL;
import static com.eth.filecoin.config.CommonConfig.PROJECT_ID;
import static com.eth.filecoin.config.CommonConfig.SECRET;
import static com.eth.filecoin.config.CommonConfig.commonMainAddress;
import static com.eth.filecoin.config.CommonConfig.commonMainKey;

import cn.hutool.core.codec.Base32;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.eth.filecoin.admin.AddressDto;
import com.eth.filecoin.admin.EasySend;
import com.eth.filecoin.admin.EasySendDto;
import com.eth.filecoin.admin.FilAddressDto;
import com.eth.filecoin.admin.GetGas;
import com.eth.filecoin.admin.RpcPar;
import com.eth.filecoin.admin.Transaction;
import com.eth.filecoin.admin.result.BalanceResult;
import com.eth.filecoin.admin.result.CommonResult;
import com.eth.filecoin.admin.result.GasResult;
import com.eth.filecoin.admin.result.SendResult;
import com.eth.filecoin.admin.result.TransferAccountsResult;
import com.eth.filecoin.admin.result.WalletResult;
import com.eth.filecoin.admin.result.WalletVerify;
import com.eth.filecoin.common.CodeConstants;
import com.eth.filecoin.common.FilecoinCnt;
import com.eth.filecoin.common.crypto.utils.ECKey;
import com.eth.filecoin.entity.FilAddress;
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
import net.dreamlu.mica.http.BaseAuthenticator;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.ResponseSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import ove.crypto.digest.Blake2b;

@Slf4j
@Service
public class FilecoinServiceImpl implements FilecoinService {

    @Autowired
    private TransactionHandler transactionHandler;
    @Autowired
    private FilAddressMapper filAddressMapper;
    @Autowired
    private FilOrderMapper filOrderMapper;
    @Autowired
    private FilTakeMapper filTakeMapper;
    @Autowired
    private FilecoinTaskService filecoinTaskService;
    @Autowired
    private CacheRedis cacheRedis;
    @Autowired
    private FilFocusMapper filFocusMapper;

    /**
     * 创建钱包
     *
     * @param filAddressDto
     * @return
     */
    @Override
    public WalletResult createWallet(FilAddressDto filAddressDto) {
        ECKey ecKey = new ECKey();
        byte[] privKeyBytes = ecKey.getPrivKeyBytes();
        byte[] pubKey = ecKey.getPubKey();
        if (Objects.isNull(privKeyBytes)) {
            throw new AssertException("create wallet error");
        }
        String filAddress = byteToAddress(pubKey);
        String privatekey = HexUtil.encodeHexStr(privKeyBytes);
        WalletResult walletResult = WalletResult.builder().address(filAddress).privatekey(privatekey).build();
        FilAddress address = new FilAddress();
        address.setAddress(walletResult.getAddress());
        address.setPrivateKey(walletResult.getPrivatekey());
        address.setId(IdWorker.getIdStr());
        address.setUserId(filAddressDto.getUserId());
        address.setCallBackUrl(filAddressDto.getCallBackUrl());
        address.setStatus(0);
        // 数据状态[0 天芯 1 双擎 app]
        if (StringUtil.isNotBlank(filAddressDto.getType()) && filAddressDto.getType().equals(CodeConstants.STRING_ONE)) {
            address.setState(CodeConstants.STRING_ONE);
        }
        address.setCreated(new Date());
        address.setUpdated(new Date());
        filAddressMapper.insertSelective(address);
        return walletResult;
    }

    /**
     * 导入钱包
     *
     * @return WalletResult
     */
    @Override
    public WalletResult importWallet(String privatekey) {
        if (StrUtil.isBlank(privatekey)) {
            throw new AssertException("parameter cannot be empty");
        }
        ECKey ecKey = ECKey.fromPrivate(HexUtil.decodeHex(privatekey));
        byte[] pubKey = ecKey.getPubKey();

        String filAddress = byteToAddress(pubKey);

        return WalletResult.builder().address(filAddress).privatekey(privatekey).build();
    }

    /**
     * 导入钱包
     *
     * @return WalletResult
     */
    @Override
    public WalletResult importWallet(byte[] privatekey) {
        if (ArrayUtil.isEmpty(privatekey)) {
            throw new AssertException("parameter cannot be empty");
        }
        ECKey ecKey = ECKey.fromPrivate(privatekey);
        byte[] pubKey = ecKey.getPubKey();

        String filAddress = byteToAddress(pubKey);

        return WalletResult.builder().address(filAddress).privatekey(HexUtil.encodeHexStr(privatekey))
                .build();
    }

    /**
     * 转账
     *
     * @param transaction 参数
     * @param privatekey  私钥
     * @return SendResult
     */
    @Override
    public SendResult send(Transaction transaction, String privatekey) {
        return null;
    }

    private String byteToAddress(byte[] pub) {
        Blake2b.Digest digest = Blake2b.Digest.newInstance(20);
        String hash = HexUtil.encodeHexStr(digest.digest(pub));

        //4.计算校验和
        String pubKeyHash = "01" + HexUtil.encodeHexStr(digest.digest(pub));

        Blake2b.Digest blake2b3 = Blake2b.Digest.newInstance(4);
        String checksum = HexUtil.encodeHexStr(blake2b3.digest(HexUtil.decodeHex(pubKeyHash)));
        //5.生成地址

        return "f1" + Base32.encode(HexUtil.decodeHex(hash + checksum)).toLowerCase();
    }

    /**
     * 查询地址余额
     *
     * @param address 钱包地址
     * @param timeout 时时间 单位：ms
     * @return BalanceResult
     */
    @Override
    public BalanceResult getBalance(String address, int timeout) {
        BalanceResult result = new BalanceResult();
        if (StrUtil.isBlank(address)) {
            throw new AssertException("parameter cannot be empty");
        }
        List<Object> params = new ArrayList<>();
        params.add(address);
        RpcPar par = RpcPar.builder().id(1)
                .jsonrpc("2.0")
                .method(FilecoinCnt.GET_BALANCE)
                .params(params).build();

        BalanceResult execute = HttpRequest.post(URI.create(FILECOIN_URL))
                .addHeader("Content-Type", "application/json")
                .authenticator(new BaseAuthenticator(PROJECT_ID, SECRET))
                .bodyJson(par)
                .execute()
                .asValue(BalanceResult.class);
        result.setBalance(FileUtil.filUtil(execute.getResult()));
        return result;
    }

    private String execute(RpcPar par, int timeout) {
        Optional<String> result = HttpRequest.post(URI.create(FILECOIN_URL))
                .addHeader("Content-Type", "application/json")
                .authenticator(new BaseAuthenticator(PROJECT_ID, SECRET))
                .bodyJson(par)
                .execute()
                .onSuccessOpt(ResponseSpec::asString);
        log.info("接口返回result:" + com.alibaba.fastjson.JSONObject.toJSONString(result));
        return result.get();
    }

    @Override
    public FilOrder send(Transaction transaction, EasySend send, int timeout) {
        if (transaction == null || StrUtil.isBlank(transaction.getFrom())
                || StrUtil.isBlank(transaction.getTo())
                || StrUtil.isBlank(transaction.getGasFeeCap())
                || StrUtil.isBlank(transaction.getGasPremium())
                || StrUtil.isBlank(transaction.getValue())
                || transaction.getGasLimit() == null
                || transaction.getMethod() == null
                || transaction.getNonce() == null
                || StrUtil.isBlank(send.getPrivatekey())) {
            throw new AssertException("parameter cnanot be empty");
        }
        BigInteger account = new BigInteger(transaction.getValue());
        if (account.compareTo(BigInteger.ZERO) < 1) {
            throw new AssertException("the transfer amount must be greater than 0");
        }
        byte[] cidHash = null;
        try {
            cidHash = transactionHandler.transactionSerialize(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertException("transaction entity serialization failed");
        }
        //签名
        ECKey ecKey = ECKey.fromPrivate(HexUtil.decodeHex(send.getPrivatekey()));
        String sing = Base64.encode(ecKey.sign(cidHash).toByteArray());
        log.info("私钥sing：" + sing);

        List<Object> params = new ArrayList<>();
        JSONObject signatureJson = new JSONObject();
        JSONObject messageJson = new JSONObject(transaction);
        JSONObject json = new JSONObject();
        messageJson.putOpt("version", 0);
        signatureJson.putOpt("data", sing);
        signatureJson.putOpt("type", 1);
        json.putOpt("message", messageJson);
        json.putOpt("signature", signatureJson);
        json.putOpt("CID", new JSONObject(transaction.getCID()));

        params.add(json);
        RpcPar rpcPar = RpcPar.builder().id(1).jsonrpc("2.0").method(FilecoinCnt.BOARD_TRANSACTION)
                .params(params).build();
        log.info("入参rpcPar:" + com.alibaba.fastjson.JSONObject.toJSONString(rpcPar));
        String execute = execute(rpcPar, timeout);
        JSONObject executeJson = new JSONObject(execute);
        String result = Optional.ofNullable(executeJson.getJSONObject("result").getStr("/"))
                .orElse("500");
        // 1，0 充值，2，1 提现, 提现扣除余额
        FilOrder filOrder = new FilOrder();
        filOrder.setId(IdWorker.getIdStr());
        filOrder.setFromAddress(transaction.getFrom());
        filOrder.setToAddress(transaction.getTo());
        filOrder.setBalance(FileUtil.filUtil(transaction.getValue()));
        filOrder.setHash(result);
        filOrder.setSyncCount(0);
        filOrder.setCurrency(0);
        filOrder.setState(CodeConstants.STRING_ONE);
        filOrder.setCreated(new Date());
        filOrder.setUpdated(new Date());
        filOrderMapper.insertSelective(filOrder);
        return filOrder;
    }

    @Override
    public TransferAccountsResult transferAccounts(EasySend send, int timeout) {
        BigDecimal subtract = new BigDecimal(send.getValue()).subtract(new BigDecimal("0.01"));
        // 减去 0.01 后不能小于 0.01
        if (subtract.compareTo(new BigDecimal("0.01")) < 1 ? true : false) {
            log.error("500:", "余额不足！");
            throw new AssertException("500", "余额不足！");
        }
        send.setValue(FileUtil.stringTurnBigDecimal(send.getValue()));
        // 获取gas
        GasResult gas = getGas(GetGas.builder().from(send.getFrom())
                .to(send.getTo())
                .value(send.getValue()).build(), timeout);
//        log.info("获取gas:" + gas);
        //获取nonce
        int nonce = getNonce(send.getFrom(), timeout);
//        log.info("nonce:" + nonce);
        //拼装交易参数
        Transaction transaction = Transaction.builder().from(send.getFrom())
                .to(send.getTo())
                .gasFeeCap(gas.getGasFeeCap())
                .gasLimit(gas.getGasLimit().longValue() * 2)
                .gasPremium(gas.getGasPremium())
                .CID(gas.getCID())
                .method(0L)
                .nonce((long) nonce)
                .params("")
                .value(send.getValue()).build();
        log.info("拼装交易参数 transaction:" + transaction);
        FilOrder filOrder = send(transaction, send, timeout);
        TransferAccountsResult transferResult = BeanUtil.copyProperties(transaction, TransferAccountsResult.class);
        transferResult.setCid(filOrder.getHash());
        transferResult.setOrderId(filOrder.getId());
        return transferResult;
    }

    @Override
    public int getNonce(String address) {
        return 0;
    }

    @Override
    public int getNonce(String address, int timeout) {
        if (StrUtil.isBlank(address)) {
            throw new AssertException("parameter cannot be empty");
        }
        List<Object> params = new ArrayList<>();
        params.add(address);
        RpcPar par = RpcPar.builder().id(1).jsonrpc("2.0").method(FilecoinCnt.GET_NONCE)
                .params(params).build();
        String execute = execute(par, timeout);
        JSONObject result = new JSONObject(execute);
        Integer num = Optional.ofNullable(result.getInt("result")).orElse(500);
        return num;
    }

    @Override
    public GasResult getGas(GetGas gas) {
        return null;
    }

    @Override
    public GasResult getGas(GetGas gas, int timeout) {
        if (gas == null || StrUtil.isBlank(gas.getFrom())
                || StrUtil.isBlank(gas.getTo())
                || gas.getValue() == null) {
            throw new AssertException("paramter cannot be empty");
        }
        // 转账需要数量大于手续费
        if (false) {
            throw new AssertException("transfer failed when deducting funds!");
        }
        List<Object> params = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.putOpt("From", gas.getFrom());
        json.putOpt("To", gas.getTo());
        json.putOpt("Value", gas.getValue());
        params.add(json);
        params.add(null);
        params.add(null);
        RpcPar par = RpcPar.builder().id(1).jsonrpc("2.0")
                .params(params)
                .method(FilecoinCnt.GET_GAS).build();
        String execute = execute(par, timeout);
        GasResult gasResult = null;
        JSONObject result = new JSONObject(execute);
        if (Objects.nonNull(result.getJSONObject("error"))) {
            Object errorMessage = result.getJSONObject("error").get("message");
            log.error("GasErrorMessage:" + errorMessage);
            throw new AssertException("GasErrorMessage", errorMessage.toString());
        }
        JSONObject jsonObject = result.getJSONObject("result");
        gasResult = GasResult.builder().gasFeeCap(jsonObject.getStr("GasFeeCap"))
                .gasLimit(jsonObject.getBigInteger("GasLimit"))
                .gasPremium(jsonObject.getStr("GasPremium"))
                .CID(jsonObject.getJSONObject("CID")).build();
        return gasResult;
    }

    @Override
    public CommonResult balanceOf(String address) {
        return null;
    }

    /**
     * 验证签名
     *
     * @return
     */
    @Override
    public Boolean walletVerify(EasySend send, int timeout) {
        if (send == null || StrUtil.isBlank(send.getFrom())
                || StrUtil.isBlank(send.getTo())
                || StrUtil.isBlank(send.getPrivatekey())
                || send.getValue() == null) {
            throw new AssertException("parameter cannot be empty");
        }
        //获取gas
        GasResult gas = getGas(GetGas.builder().from(send.getFrom())
                .to(send.getTo())
                .value(send.getValue()).build(), timeout);
        log.info("gas:" + gas);
        //获取nonce
        int nonce = getNonce(send.getFrom(), timeout);
        log.info("nonce:" + nonce);
        //拼装交易参数
        Transaction transaction = Transaction.builder().from(send.getFrom())
                .to(send.getTo())
                .gasFeeCap(gas.getGasFeeCap())
                .gasLimit(gas.getGasLimit().longValue() * 2)
                .gasPremium(gas.getGasPremium())
                .CID(gas.getCID())
                .method(0L)
                .nonce((long) nonce)
                .params("")
                .value(send.getValue()).build();
        log.info("transaction:" + transaction);

        byte[] cidHash = null;
        try {
            cidHash = transactionHandler.transactionSerialize(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertException("transaction entity serialization failed");
        }
        //签名
        ECKey ecKey = ECKey.fromPrivate(HexUtil.decodeHex(send.getPrivatekey()));
        String sing = Base64.encode(ecKey.sign(cidHash).toByteArray());
//        log.info("私钥sing：" + sing);

        List<Object> params = new ArrayList<>();
        JSONObject signatureJson = new JSONObject();
        JSONObject messageJson = new JSONObject(transaction);
        JSONObject json = new JSONObject();
        messageJson.putOpt("version", 0);
        signatureJson.putOpt("Type", 1);
        signatureJson.putOpt("Data", sing);
//    json.putOpt("Signature", signatureJson);

        params.add(send.getTo());
        params.add(sing);
        params.add(signatureJson);
        RpcPar par = RpcPar.builder().id(1).jsonrpc("2.0").method(FilecoinCnt.WALLET_VERIFY)
                .params(params).build();

//        log.info("钱包校验入参：" + com.alibaba.fastjson.JSONObject.toJSONString(par));
        WalletVerify result = HttpRequest.post(URI.create(FILECOIN_URL))
                .addHeader("Content-Type", "application/json")
                .authenticator(new BaseAuthenticator(PROJECT_ID, SECRET))
                .bodyJson(par)
                .execute()
                .asValue(WalletVerify.class);
        Assert.isTrue(result.getResult(), "signature did not match");
        return true;
    }

    /**
     * 回调
     * <p>
     * 1，充值，2，提现
     *
     * @param request
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void takeCallback(AddressDto request) {
        if (request.getType().equals(CodeConstants.INTEGER_TWO)) {
            FilOrder selectById = filOrderMapper.selectById(request.getOrderId());
            FilTake filTake = new FilTake();
            filTake.setId(IdWorker.getIdStr());
            filTake.setFromAddress(selectById.getFromAddress());
            filTake.setToAddress(selectById.getToAddress());
            filTake.setCount(selectById.getBalance());
            filTake.setHash(selectById.getHash());
            filTake.setIsSync(0);
            filTake.setCreated(new Date());
            filTake.setUpdated(new Date());
            filTakeMapper.insertSelective(filTake);
        }
        FilOrder filOrder = new FilOrder();
        filOrder.setId(request.getOrderId());
        filOrder.setIsSync(0);
        filOrder.setUpdated(new Date());
        filOrderMapper.updateByPrimaryKey(filOrder);
    }

    /**
     * 提现
     *
     * @param easySendDto
     */
    @Override
    @Transactional
    public Boolean take(EasySendDto easySendDto) {
//        EasySend easySend = new EasySend();
//        easySend.setFrom("f1yxjxb63d5gd6ttuiob5yev57hrqdo2fcaxkdmmi");
//        easySend.setPrivatekey("a1452fa83fd5e5844c66132fc8afcc3dce3124ba4e413cc9eb1ac602bec8174e");
//        easySend.setTo("f1g6ls7jlfrqu65vi4rphg7eesgs245jck47aitia");
//        easySend.setType(CodeConstants.STRING_TWO);
//        easySend.setValue("1.16000000");
//        TransferAccountsResult transferAccountsResult = transferAccounts(easySend, FilecoinCnt.DEFAULT_TIMEOUT);
////    filecoin.transferAccounts(send,FilecoinCnt.DEFAULT_TIMEOUT);
//        System.out.println("transferAccountsResult:" + com.alibaba.fastjson.JSONObject.toJSONString(transferAccountsResult));
//        return true;

        log.info("easySendDto:" + com.alibaba.fastjson.JSONObject.toJSONString(easySendDto));
        // 1，0 充值，2，1 提现, 提现扣除余额

        if (StringUtil.isBlank(easySendDto.getType())){
            easySendDto.setType(CodeConstants.STRING_ZERO);
        }
        List<FilOrder> filFromAddress = null;
        List<FilOrder> filToList = null;
        //  出账 from(检查余额)   收账 to
        if (easySendDto.getType().equals(CodeConstants.STRING_ONE)) {
            filFromAddress = filOrderMapper.selectByFromAddress(easySendDto.getFrom());
            filToList = filOrderMapper.selectByTo(easySendDto.getFrom());
        } else {
            filFromAddress = filOrderMapper.selectByFromAddress(easySendDto.getTo());
            filToList = filOrderMapper.selectByTo(easySendDto.getTo());
        }
        BigDecimal fromBalance = null;
        BigDecimal toBalance = null;
        if (CollectionUtils.isEmpty(filFromAddress)) {
            fromBalance = new BigDecimal("0.00");
        } else {
            fromBalance = filFromAddress.stream().map(FilOrder::getBalance).reduce(BigDecimal::add).get();
        }
        if (CollectionUtils.isEmpty(filToList)) {
            toBalance = new BigDecimal("0.00");
        } else {
            toBalance = filToList.stream().map(FilOrder::getBalance).reduce(BigDecimal::add).get();
        }
        // 校验余额是否足够
        if (fromBalance.compareTo(toBalance) < 1) {

            // 转账
            EasySend easySend = new EasySend();
            easySend.setFrom(commonMainAddress());
            easySend.setPrivatekey(commonMainKey());
            easySend.setTo(easySendDto.getTo());
            easySend.setType(CodeConstants.STRING_TWO);
            easySend.setValue(FileUtil.filUtil(easySendDto.getValue()).toString());
            if (easySendDto.getType().equals(CodeConstants.STRING_ONE)) {
                easySend.setValue(easySendDto.getValue());
            }
            TransferAccountsResult result = transferAccounts(easySend, FilecoinCnt.DEFAULT_TIMEOUT);
            FilTake filTake = new FilTake();
            filTake.setId(IdWorker.getIdStr());
            filTake.setUserId(easySendDto.getUserId());
            filTake.setOrderId(result.getOrderId());
            filTake.setHash(result.getCid());
            filTake.setFromAddress(commonMainAddress());
            filTake.setToAddress(easySendDto.getTo());
            filTake.setCount(FileUtil.filUtil(easySendDto.getValue()));
            filTake.setIsSync(1);
            filTake.setCallBackUrl(easySendDto.getCallBackUrl());
            filTake.setCurrency(easySendDto.getGetcashId());
            if (easySendDto.getType().equals(CodeConstants.STRING_ONE)) {
                filTake.setState(CodeConstants.STRING_ONE);
                filTake.setCount(new BigDecimal(easySendDto.getValue()));
                filTake.setToAddress(easySendDto.getFrom());
                filTake.setOrderId(easySendDto.getOrderId());
            }
            filTake.setCreated(new Date());
            filTake.setUpdated(new Date());
            filTakeMapper.insertSelective(filTake);
            return true;
        }
        return false;
    }
}
