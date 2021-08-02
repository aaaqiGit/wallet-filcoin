package com.eth.filecoin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "FilAddress", description = "回调")
public class AddressDto implements Serializable {
    private static final long serialVersionUID = 8666964461682188183L;
    @ApiModelProperty("钱包地址")
    @NotNull
    private String address;

    @ApiModelProperty(value = "用户id", required = true, example = "")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "订单id", required = true, example = "")
    @NotBlank
    private String orderId;

    @ApiModelProperty(value = "1，充值，2，提现", required = true, example = "")
    @NotBlank
    private Integer type;
}
