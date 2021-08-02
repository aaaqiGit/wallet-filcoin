package com.eth.filecoin.admin;

import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/27 17:59
 * @Description:
 */
@Data
public class TakeCallbackDto {
    /**
     * 收款地址
     */
    private String to;
    /**
     * 转账方地址
     */
    private String from;
    /**
     * 用户 id
     */
    private String userId;
    /**
     * 转账金额
     */
    private String amount;
    /**
     * 交易哈希
     */
    private String hash;
    /**
     * 币种
     */
    private String coin = "FIL";
    /**
     * 订单
     */
    private String orderId;
    /**
     * 签名
     */
    private String sign;
}
