package com.eth.filecoin.admin.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/25 12:44
 * @Description:
 */
@Data
@ApiModel(description = "通用返回")
public class CommonResult {

    private String jsonrpc;

    private String result;

    private Integer id;

    @ApiModelProperty("数量")
    private BigDecimal balance;
}
