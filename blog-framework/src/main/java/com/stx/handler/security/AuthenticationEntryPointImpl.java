package com.stx.handler.security;

import com.alibaba.fastjson.JSON;
import com.stx.domain.ResponseResult;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @user 20600
 * @Description  废弃  统一在mvc中处理
 * @Date  2022/12/8
 * @Param
 * @return
 **/
//认证错误处理实现类
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//   打印异常信息
        ResponseResult result;
        e.printStackTrace();
        if (e instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), e.getMessage());

        } else if (e instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证失败");
        }

        //        响应给前端 符合规范

        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
