package com.eth.filecoin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "FilFocus", description = "归集")
public class FilFocus {
    /**
     *   主键id
     *
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     *   提币地址
     *
     */
    private String fromAddress;

    /**
     *   收币地址
     *
     */
    private String toAddress;

    /**
     *   数量
     *
     */
    private BigDecimal count;

    /**
     *   总手续费率
     *
     */
    private String gasFeeCap;

    /**
     *   给矿工的手续费率
     *
     */
    private String gasPremium;

    /**
     *   消耗的最大Gas量
     *
     */
    private Long gasLimit;

    /**
     *   gas单价
     *
     */
    private BigDecimal gasPrice;

    /**
     *   发送gas成功后的hash
     *
     */
    private String gasHash;

    /**
     *   Gas发送状态，0未发放，1已发放，2发放成功
     *
     */
    private Integer gasState;

    /**
     *   0未归集，1归集成功，2归集失败，3链上归集成功，4链上归集失败
     *
     */
    private Integer state;

    /**
     *   归集hash
     *
     */
    private String hash;

    /**
     *   大区，1 A大区，2 B大区
     *
     */
    private Integer area;

    /**
     *   备注
     *
     */
    private String remark;

    /**
     *   是否锁定[0:正常,1:锁定]
     *
     */
    private Integer locked;

    /**
     *   状态[0:正常,1:失效]
     *
     */
    private Integer status;

    /**
     *   创建时间
     *
     */
    private Date created;

    /**
     *   更新时间
     *
     */
    private Date updated;


    /**
     * 币种类，全大写
     */
    @TableField(exist = false)
    private String coin;
}