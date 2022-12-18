package com.stx.handler.exception;

import com.alibaba.fastjson.JSON;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.handler.security.AccessDeniedHandlerImpl;
import com.stx.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//springmvc 全局异常处理
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    用户异常
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
//        打印异常
        log.error("出现了用户异常!! {}",e);
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

//    登录异常
    @ExceptionHandler( AuthenticationException.class)
public ResponseResult AuthenticationHandler(AuthenticationException e){
        log.error("用户登录 异常 ——{}");
        ResponseResult result;
        e.printStackTrace();
        if (e instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), e.getMessage());

        } else if (e instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), SystemConstants.LOGIN_ERROR);
        }
        return result;
    }
//    权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult AccessDenieddHandle(AccessDeniedException e){
        log.error("用户权限异常");
        e.printStackTrace();
        return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
    }






//    系统未知异常
    @ExceptionHandler(Exception.class)
    public ResponseResult ExceptionHandler(Exception e){
//        打印异常
        log.error("出现了系统未知异常!! {}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }




}
