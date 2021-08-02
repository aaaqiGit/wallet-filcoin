package com.eth.filecoin.service;

import com.eth.filecoin.admin.AddressDto;
import com.eth.filecoin.admin.EasySend;
import com.eth.filecoin.admin.EasySendDto;
import com.eth.filecoin.admin.FilAddressDto;
import com.eth.filecoin.admin.GetGas;
import com.eth.filecoin.admin.Transaction;
import com.eth.filecoin.admin.result.BalanceResult;
import com.eth.filecoin.admin.result.CommonResult;
import com.eth.filecoin.admin.result.GasResult;
import com.eth.filecoin.admin.result.SendResult;
import com.eth.filecoin.admin.result.TransferAccountsResult;
import com.eth.filecoin.admin.result.WalletResult;
import com.eth.filecoin.entity.FilOrder;


public interface FilecoinService {

    /**
     * 创建钱包
     *
     * @return WalletResult
     */
    WalletResult createWallet(FilAddressDto filAddressDto);

    /**
     * 导入钱包
     *
     * @return WalletResult
     */
    WalletResult importWallet(String privatekey);

    /**
     * 导入钱包
     *
     * @return WalletResult
     */
    WalletResult importWallet(byte[] privatekey);

    /**
     * 转账
     *
     * @param transaction 参数
     * @param privatekey  私钥
     * @return SendResult
     */
    SendResult send(Transaction transaction, String privatekey);

    /**
     * 转账
     *
     * @param transaction 参数
     * @param timeout     请求超时时间 单位：ms
     * @return SendResult
     */
    FilOrder send(Transaction transaction, EasySend send, int timeout);

    /**
     * 简单转账
     *
     * @param send    参数
     * @param timeout 超时时间 单位：ms
     * @return SendResult
     */
    TransferAccountsResult transferAccounts(EasySend send, int timeout);

    /**
     * 获取nonce值
     *
     * @param address 地址
     * @return int
     */
    int getNonce(String address);

    /**
     * 获取nonce值
     *
     * @param address 地址
     * @param timeout 超时时间 单位：ms
     * @return int
     */
    int getNonce(String address, int timeout);

    /**
     * 获取gas价格
     *
     * @param gas 参数
     * @return GasResult
     */
    GasResult getGas(GetGas gas);

    /**
     * 获取gas价格
     *
     * @param gas     参数
     * @param timeout 时时间 单位：ms
     * @return GasResult
     */
    GasResult getGas(GetGas gas, int timeout);

    /**
     * 查询地址余额
     *
     * @param address 钱包地址
     * @return BalanceResult
     */
    CommonResult balanceOf(String address);

    /**
     * 查询地址余额
     *
     * @param address 钱包地址
     * @param timeout 时时间 单位：ms
     * @return BalanceResult
     */
    BalanceResult getBalance(String address, int timeout);

    /**
     * 验证签名
     *
     * @return
     */
    Boolean walletVerify(EasySend send, int timeout);


    /**
     * 提现回调
     *
     * @param request
     */
    void takeCallback(AddressDto request);

    /**
     * 提现
     *
     * @param easySendDto
     */
    Boolean take(EasySendDto easySendDto);
}
