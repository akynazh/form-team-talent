package com.xdu.formteamtalent.exception;

import com.xdu.formteamtalent.entity.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult handler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : allErrors) {
            sb.append(error).append("&");
        }
        log.error("实体校验异常: {}", e.getMessage());
        return RestResult.fail("400", sb.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public RestResult handler(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage());
        return RestResult.fail("400", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public RestResult handler(IllegalArgumentException e) {
        log.error("Assert异常: {}", e.getMessage());
        return RestResult.fail("400", e.getMessage());
    }
}
