package com.eth.filecoin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "FilAddress", description = "钱包地址")
public class FilAddressDto implements Serializable {
    private static final long serialVersionUID = -9074664312038903117L;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true, example = "")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "回调 url", required = true, example = "")
    @NotBlank
    private String callBackUrl;

    @ApiModelProperty(value = "标识：双擎 app = 1", required = true, example = "")
    private String type;

//    @ApiModelProperty(value = "秘钥", required = true, example = "")
//    @NotBlank
//    private String sign;
}