package com.ayi.ayiojbackendcommom.exception;

import lombok.extern.slf4j.Slf4j;
import com.ayi.ayiojbackendcommom.common.BaseResponse;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

//    @ExceptionHandler(UnauthorizedException.class)
//    public BaseResponse<?> noPermissionHandler(RuntimeException e) {
//        log.error("UnauthorizedException", e);
//        return ResultUtils.error(ErrorCode.FORBIDDEN_ERROR, "您没有操作权限");
//    }
}
