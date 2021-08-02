package com.eth.filecoin.admin.result;

import cn.hutool.json.JSONObject;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GasResult implements Serializable {
    /**
     * 用户选择支付的总手续费率
     */
    private String gasFeeCap;

    /**
     * 用户选择支付给矿工的手续费率
     */
    private String gasPremium;

    /**
     * 该笔交易能消耗的最大Gas量
     */
    private BigInteger gasLimit;

    private JSONObject CID;
}
