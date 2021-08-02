package com.eth.filecoin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FilTake", description = "提现")
public class FilTake {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 提币地址
     */
    private String fromAddress;

    /**
     * 收币地址
     */
    private String toAddress;

    /**
     * 数量
     */
    private BigDecimal count;

    /**
     * hash
     */
    private String hash;

    /**
     * 是否已经同步[0:是，1:否]
     */
    private Integer isSync;

    /**
     * 同步次数，最多50次
     */
    private Integer syncCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 推送调用方地址
     */
    private String callBackUrl;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 交易 id
     */
    private String orderId;

    /**
     *  天芯 memberId;
     */
    private Integer currency;

    /**
     * 是否锁定[0:正常,1:锁定]
     */
    private Integer locked;

    /**
     * 状态[0:正常,1:失效]
     */
    private Integer status;

    /**
     * 数据状态[0 天芯 1 双擎 app]
     */
    private String state;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;
}