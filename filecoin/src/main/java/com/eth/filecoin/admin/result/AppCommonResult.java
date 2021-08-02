package com.eth.filecoin.admin.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/28 14:49
 * @Description:
 */
@Data
@ApiModel(description = "app通用返回")
public class AppCommonResult {

    private String msg;

    private Integer code;

    private Boolean success;

    private Object data;

}
