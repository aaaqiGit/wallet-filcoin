package com.eth.filecoin.admin;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/5/7 17:29
 * @Description:
 */
@Data
public class CacheFilCoinInfo {
    /**
     * 交易 id
     */
    private String orderId;

    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * nonce值，起始为0
     */
    private Long nonce;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 钱包私钥
     */
    private String privateKey;

    /**
     * 回调 url
     */
    private String callBackUrl;

    /**
     * 类型[]
     */
    private Integer addressType;

    /**
     * 是否锁定[0:正常,1:锁定]
     */
    private Integer locked;

    /**
     * 状态[0:正常,1:失效]
     */
    private Integer status;

    /**
     * 数据状态[0 正常 1 删除]
     */
    private String state;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

}
