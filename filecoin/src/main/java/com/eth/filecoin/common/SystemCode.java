package com.eth.filecoin.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统内置code
 *
 * @author L.cm
 */
@Getter
@AllArgsConstructor
public enum SystemCode implements IResultCode {
  /**
   * 操作失败
   */
  FAILURE(500, "操作失败"),
  /**
   * 操作成功
   */
  SUCCESS(200, "操作成功");

  /**
   * code编码
   */
  final int code;
  /**
   * 中文信息描述
   */
  final String desc;

}
