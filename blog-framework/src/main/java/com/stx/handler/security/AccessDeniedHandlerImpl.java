package com.stx.handler.security;

import com.alibaba.fastjson.JSON;
import com.stx.domain.ResponseResult;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
// 授权异常处理类
@Component
public class AccessDeniedHandlerImpl  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
//        打印异常
        e.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
