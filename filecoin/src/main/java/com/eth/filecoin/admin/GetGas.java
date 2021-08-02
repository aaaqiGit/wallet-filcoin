package com.eth.filecoin.admin;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGas implements Serializable {
    private static final long serialVersionUID = -7613905048785612754L;
    /**
     * 转账方地址
     */
    private String from;
    /**
     * 收账方地址
     */
    private String to;
    /**
     * 转账金额
     */
    private String value;
}
