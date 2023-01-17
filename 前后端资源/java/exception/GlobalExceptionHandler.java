package com.example.raj_liangjian.exception;

import com.example.raj_liangjian.common.CustomException;
import com.example.raj_liangjian.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法，
     * 嘿嘿异常哪里跑
     * @return
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info("异常信息{} 异常:{}",ex.getMessage(),ex.fillInStackTrace());
        //
        if (ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            return R.error(s[2]+"已存在");
        }

        return R.error("未知错误,请联系工程师进行维护");
    }

    /**
     * 自定义的异常，如关联有菜品或套餐走这里
     * @param ex
     * @return
     */
    @ExceptionHandler({CustomException.class})
    public R<String> CustomException(CustomException ex){
        log.info("异常信息{}",ex.getMessage());
        return R.error(ex.getMessage());
    }

}
