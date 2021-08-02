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
public class EasySend implements Serializable {
    private static final long serialVersionUID = -8350636237263203362L;
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
    /**
     * 私钥
     */
    private String privatekey;

    /**
     * 校验地址余额是否充足
     */
    private String address;

    /**
     * 1，充值，2，提现
     */
    private String type;

    /**
     * 调用方推送地址
     */
    private String callBackUrl;

    /**
     * 用户id
     */
    private String userId;

}
