package com.eth.filecoin.exception;

import com.eth.filecoin.common.Result;
import com.eth.filecoin.common.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: aqi
 * @Date: 2021/4/25 17:19
 * @Description: 全局统一处理异常
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler
    @ResponseBody
    public static Result handle(Exception e) {
        if (e instanceof AssertException) {
            AssertException assertException = (AssertException) e;
            log.error("【业务异常】" + assertException.getCode() + "," + assertException.getMsg());
            return Results.failure(assertException.getMsg());
        }
        e.printStackTrace();
        log.error("【系统异常】:"+ e.getMessage());
        return Results.failure(e.getMessage());
    }
}
