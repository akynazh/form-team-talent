package com.xdu.formteamtalent.exception;

import com.xdu.formteamtalent.entity.RestfulResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ShiroException.class)
    public RestfulResponse handler(ShiroException e) {
        log.error("身份验证异常: {}", e.getMessage());
        return RestfulResponse.fail(401, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestfulResponse handler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : allErrors) {
            sb.append(error).append("&");
        }
        log.error("实体校验异常: {}", e.getMessage());
        return RestfulResponse.fail(400, sb.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public RestfulResponse handler(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage());
        return RestfulResponse.fail(400, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public RestfulResponse handler(IllegalArgumentException e) {
        log.error("Assert异常: {}", e.getMessage());
        return RestfulResponse.fail(400, e.getMessage());
    }
}