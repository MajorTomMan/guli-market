package com.atguigu.gulimall.product.exception;

import java.util.HashMap;
import java.util.Map;

import com.atguigu.gulimall.common.exception.BizCodeEmum;
import com.atguigu.gulimall.common.utils.R;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
public class ExceptionControllerAdvice {
    private Map<String,String> errorMap=new HashMap<>();
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        for ( FieldError error : e.getBindingResult().getFieldErrors()) {
            log.error("错误字段出现在:{} 错误原因:{}\n",error.getField(),error.getDefaultMessage());
        }
        log.error("异常类:{}",e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach((fieldError)->{
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEmum.VALID_EXCEPTION.getCode(),BizCodeEmum.VALID_EXCEPTION.getMsg()).put("data",errorMap);
    }
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable t){
        log.error("错误打印:{}\n异常类打印:{}\n",t.getMessage(),t.getClass());
        return R.error(BizCodeEmum.UNKNOWN_EXCEPTION.getCode(),BizCodeEmum.UNKNOWN_EXCEPTION.getMsg());
    }
    @ExceptionHandler(value = NullPointerException.class)
    public R handleException(NullPointerException nullPtrs){
        log.error("错误打印:{}\n异常类打印:{}\n",nullPtrs.getMessage(),nullPtrs.getClass());
        log.error("报错原因:{}\n",nullPtrs.getCause().getMessage());
        log.error("打印堆栈:");
        nullPtrs.printStackTrace();
        return R.error(BizCodeEmum.UNKNOWN_EXCEPTION.getCode(),BizCodeEmum.UNKNOWN_EXCEPTION.getMsg());
    }
}
