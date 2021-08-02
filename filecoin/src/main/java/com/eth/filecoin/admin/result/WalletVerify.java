package com.eth.filecoin.admin.result;

import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/27 14:09
 * @Description:
 */
@Data
public class WalletVerify {

    private String jsonrpc;

    private Boolean result;

    private Integer id;
}
