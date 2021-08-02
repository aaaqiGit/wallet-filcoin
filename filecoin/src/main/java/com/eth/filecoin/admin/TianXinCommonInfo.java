package com.eth.filecoin.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/29 09:52
 * @Description:
 */
@Data
public class TianXinCommonInfo implements Serializable {

    private static final long serialVersionUID = 3432140458291379002L;

    // hash
    private String cid;

    // 转账方
    private String from;

    // 收账方
    private String to;

    // 币数量
    private BigDecimal newBalance;
    private String balance;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 交易 id
     */
    private String orderId;

    /**
     * 币种 FIL
     */
    private String coin;

    /**
     * 1，充值，2，提现
     */
    private Integer type;

    /**
     *   容错字段 天芯  getcashId;
     */
    private int getcashId;

}
