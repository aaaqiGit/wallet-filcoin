package com.eth.filecoin.service;

import com.eth.filecoin.admin.FilWalletInfo;


public interface TianXinFilecoinService {
    /**
     * 充值
     */
    void filRechargeTask();

    /**
     * 提现
     */
    void filTakeTask();

    /**
     * 归集 第一步
     */
//    void filFocusTask();

    /**
     * 归集 第二步
     */
    void filTake();

    /**
     * 获取钱包信息
     *
     * @param address
     * @return
     */
    FilWalletInfo getFileWalletInfo(String address);

    /**
     * 归集同步推送地址, 天芯 old
     */
    void filFocusSynch();
}
