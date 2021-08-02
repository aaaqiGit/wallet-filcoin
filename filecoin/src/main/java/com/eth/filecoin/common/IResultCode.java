package com.eth.filecoin.common;

public interface IResultCode {

  int getCode();

  String getDesc();

  /**
   * 转换成 Result
   * @param <T> 泛型标记
   * @return {Result}
   */
  default <T> Result<T> toResult() {
    Result<T> result = new Result<>();
    result.setCode(this.getCode());
    result.setMsg(this.getDesc());
    return result;
  }
}
