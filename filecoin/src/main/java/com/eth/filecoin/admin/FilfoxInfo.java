package com.eth.filecoin.admin;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/29 09:52
 * @Description:
 */
@Data
public class FilfoxInfo implements Serializable {

    private static final long serialVersionUID = 3432140458291379002L;

    // hash
    private String cid;

    // 高度
    private Long height;

    // 时间 s
    private Long timestamp;

    // 转账方
    private  String from;

    // 收账方
    private String to;

    private Long nonce;

    // 币数量
    private String value;

    // 方法
    private String method;

}
