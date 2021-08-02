package com.eth.filecoin.common;


import java.util.Optional;
import org.springframework.lang.Nullable;

/**
 * 结果集返回工具
 *
 * @author L.cm
 */
public class Results {

  /**
   * 判断返回是否为成功
   *
   * @param result Result
   * @return 是否成功
   */
  public static boolean isSuccess(@Nullable Result<?> result) {
    return Optional.ofNullable(result)
        .map(Result::isSuccess)
        .orElse(Boolean.FALSE);
  }

  /**
   * 获取data
   *
   * @param result Result
   * @param <T>    泛型标记
   * @return 泛型对象
   */
  @Nullable
  public static <T> T getData(@Nullable Result<T> result) {
    return Optional.ofNullable(result)
        .filter(Result::isSuccess)
        .map(Result::getData)
        .orElse(null);
  }

  /**
   * 数据库操作结果
   *
   * @param status 数据库操作结果
   * @param <T>    泛型标记
   * @return Result
   */
  public static <T> Result<T> status(boolean status) {
    return status ? Results.success() : Results.failure();
  }

  /**
   * 成功-携带数据
   *
   * @param data 数据
   * @param <T>  泛型标记
   * @return Result
   */
  public static <T> Result<T> success(T data) {
    IResultCode resultCode = SystemCode.SUCCESS;
    return result(resultCode.getCode(), resultCode.getDesc(), data);
  }

  /**
   * 返回成功
   *
   * @param <T> 泛型标记
   * @return Result
   */
  public static <T> Result<T> success() {
    return SystemCode.SUCCESS.toResult();
  }

  /**
   * 返回失败信息
   *
   * @param msg 失败信息
   * @param <T> 泛型标记
   * @return Result
   */
  public static <T> Result<T> failure(String msg) {
    return result(SystemCode.FAILURE.getCode(), msg, null);
  }

  /**
   * 返回失败信息
   *
   * @param resultCode 失败信息
   * @param <T>        泛型标记
   * @return Result
   */
  public static <T> Result<T> failure(IResultCode resultCode) {
    return resultCode.toResult();
  }

  /**
   * 返回失败信息
   *
   * @param <T> 泛型标记
   * @return Result
   */
  public static <T> Result<T> failure() {
    return SystemCode.FAILURE.toResult();
  }

  private static <T> Result<T> result(int code, String msg, @Nullable T data) {
    Result<T> result = new Result<>();
    result.setCode(code);
    result.setMsg(msg);
    result.setData(data);
    return result;
  }
}

