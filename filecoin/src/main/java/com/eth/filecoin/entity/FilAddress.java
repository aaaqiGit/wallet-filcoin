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
@ApiModel(value = "FilAddress", description = "钱包地址")
public class FilAddress {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * nonce值，起始为0
     */
    private Long nonce;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 钱包私钥
     */
    private String privateKey;

    /**
     * 回调 url
     */
    private String callBackUrl;

    /**
     * 类型[]
     */
    private Integer addressType;

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