package com.yb.aiot.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

/**
 * <p>
 * 全局异常处理
 * <p>
 *
 * @author author
 * @date 2022/11/14 16:09
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 拦截：未登录异常
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerException(NotLoginException e) {
        // 打印堆栈，以供调试
        e.printStackTrace();
        // 返回给前端
        return SaResult.error("登陆过期,请先登录!").setCode(203);
    }

    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerException(NotPermissionException e) {
        e.printStackTrace();
        return SaResult.error("缺少权限").setCode(201);
    }

}
