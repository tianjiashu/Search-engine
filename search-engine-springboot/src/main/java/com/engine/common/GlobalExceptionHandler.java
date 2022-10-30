package com.engine.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/*
    全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e){
        return R.error(500,e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public R<String> exceptionHandler(IOException e){
        return R.error(500,"Socket通信过程中发生未知错误！");
    }
}
