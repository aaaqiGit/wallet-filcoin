package com.eth.filecoin.admin;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * @author ian
 * @Date: 2021/5/3 09:03
 * @Description:
 */
@Data
public class FilWalletInfo {

    /**
     * 钱包 id eg："f0754226"
     */
    private String id;

    /**
     * 地址
     */
    private String robust;

    /**
     * "actor":"account"
     */
    private String actor;

    /**
     * "createHeight":722192
     */
    private long createHeight;

    private long  createTimestamp;

    private String balance;

    private long messageCount;

    private long timestamp;

    private JSONArray ownedMiners;

    private JSONArray workerMiners;
}
