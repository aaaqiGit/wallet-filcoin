package com.eth.filecoin.admin;

import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/5/13 10:22
 * @Description:
 */
@Data
public class AuthenRequest {
    /**
     * 调用方推送地址
     */
    private String callBackUrl;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 调用方
     */
    private String filId;

    /**
     * 调用方 key
     */
    private String filKey;

    /**
     * 调用方秘钥
     */
    private String filSecret;

    /**
     * 当前时间，Unix 毫秒时间戳
     */
    private long timestamp;

    /**
     * 签名
     */
    private String sign;
}
