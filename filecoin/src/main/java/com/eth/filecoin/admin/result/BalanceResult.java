package com.eth.filecoin.admin.result;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BalanceResult implements Serializable {
    /**
     * 余额
     */
    private BigDecimal balance;

    private String jsonrpc;

    private String result;

    private Integer id;
}

