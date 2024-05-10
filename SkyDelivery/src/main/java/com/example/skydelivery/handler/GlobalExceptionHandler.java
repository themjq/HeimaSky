package com.example.skydelivery.handler;


import com.example.skydelivery.constant.MessageConstant;
import com.example.skydelivery.exception.BaseException;

import com.example.skydelivery.result.Result;
import com.sun.net.httpserver.Authenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException exception){
        String message=exception.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split=message.split(" ");
            String username=split[2];
            String msg=username+ MessageConstant.ALREADY_EXUSTS;
            //String msg=username+"已存在";
            log.info("用户名+"+username+"重复"+exception.getMessage());
            return Result.error(msg);
        }else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

    }
}
