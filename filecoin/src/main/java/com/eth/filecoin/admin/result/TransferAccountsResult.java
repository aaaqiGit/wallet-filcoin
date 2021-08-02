package com.eth.filecoin.admin.result;

import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/27 13:45
 * @Description: 转账
 */
@Data
public class TransferAccountsResult {

    private String jsonrpc;

    private Integer id;

    /**
     * 收款地址
     */
    private String to;
    /**
     * 转账方地址
     */
    private String from;
    /**
     * nonce值
     */
    private Long nonce;
    /**
     * 转账金额
     */
    private String value;
    /**
     * 该笔交易能消耗的最大Gas量
     */
    private Long gasLimit;
    /**
     * 用户选择支付的总手续费率
     */
    private String gasFeeCap;
    /**
     * 用户选择支付给矿工的手续费率
     */
    private String gasPremium;
    private Long method;
    private String params;
    private String cid;
    private Integer version;
    private String orderId;
}
