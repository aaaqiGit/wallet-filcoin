package com.eth.filecoin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: aqi
 * @Date: 2021/4/27 17:02
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EasySendDto", description = "提现")
public class EasySendDto implements Serializable {
    private static final long serialVersionUID = 2847312586363258432L;
    /**
     * 收账方地址
     */
    @ApiModelProperty(value = "收账方地址", required = true, example = "")
//    @NotBlank
    private String to;

    /**
     * 收账方地址
     */
    @ApiModelProperty(value = "转账方地址", required = true, example = "")
//    @NotBlank
    private String from;

    /**
     * 转账金额
     */
    @ApiModelProperty(value = "转账金额", required = true, example = "示例：0.10000000")
//    @NotBlank
    private String value;

    /**
     * 调用方推送地址
     */
    @ApiModelProperty(value = "调用方推送地址URL", required = true, example = "")
//    @NotBlank
    private String callBackUrl;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true, example = "")
//    @NotBlank
    private String userId;

    @ApiModelProperty(value = "标识：双擎 app = 1", required = true, example = "")
    private String type;

    @ApiModelProperty(value = "订单 id", required = true, example = "")
    private String orderId;


    @ApiModelProperty(value = "天芯 getcashId", required = true, example = "")
    private int getcashId;


//
//    @ApiModelProperty(value = "秘钥", required = true, example = "")
//    @NotBlank
//    private String sign;
}
