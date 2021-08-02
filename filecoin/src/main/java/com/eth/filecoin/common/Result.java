package com.eth.filecoin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.annotation.Nullable;
import lombok.Data;
import net.dreamlu.mica.core.result.SystemCode;

/**
 * 结果集封装
 *
 * @param <T> 泛型标记
 * @author L.cm
 */
@Data
@ApiModel(description = "Result模型")
public class Result<T> implements Serializable {
  private static final long serialVersionUID = -1160662278280275915L;

  @ApiModelProperty(value = "code码[500:失败,200:成功]", required = true)
  private int code;
  @ApiModelProperty("数据")
  @Nullable
  private T data;
  @ApiModelProperty("消息")
  @Nullable
  private String msg;

  boolean isSuccess() {
    return this.code == SystemCode.SUCCESS.getCode();
  }
}
