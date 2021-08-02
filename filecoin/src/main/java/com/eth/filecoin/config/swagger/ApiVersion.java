package com.eth.filecoin.config.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author catface
 * @date 2020/5/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiVersion {

  /**
   * 接口版本号(对应swagger中的group)
   *
   * @return String[]
   */
  String[] group();
}
