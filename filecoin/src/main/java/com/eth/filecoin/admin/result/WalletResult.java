package com.eth.filecoin.admin.result;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResult implements Serializable {
    private static final long serialVersionUID = -4560230642661306410L;
    @ApiModelProperty(value = "钱包地址", required = true, example = "")
    @NotBlank
    private String address;
    @ApiModelProperty(value = "钱包私钥", required = true, example = "")
    @NotBlank
    private String privatekey;
}
