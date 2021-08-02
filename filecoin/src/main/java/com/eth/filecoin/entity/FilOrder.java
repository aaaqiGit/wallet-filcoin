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
@ApiModel(value = "FilOrder", description = "订单")
public class FilOrder {
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
    private BigDecimal balance;

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
     *
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
     * 数据状态[0 正常 1 删除]
     * 1，0 充值，2，1 提现, 提现扣除余额
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