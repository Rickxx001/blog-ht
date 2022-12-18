package com.stx.service.impl;

import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.LoginUser;
import com.stx.domain.entity.User;
import com.stx.domain.vo.BlogUserLoginVo;
import com.stx.domain.vo.UserInfoVo;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.service.BlogLoginService;
import com.stx.utils.BeanCopyUtils;
import com.stx.utils.JwtUtil;
import com.stx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
//        注册令牌
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        判断是否认证通过
        if(Objects.isNull(authenticate)){
           throw  new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
//        获取user id 生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String UserId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(UserId);
//      把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_LOGIN +UserId,loginUser);
//        把token 和userinfo 封装 返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
//        获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
//        删除 redis 中对应id 的用户信息
        redisCache.deleteObject(SystemConstants.REDIS_LOGIN+userId);

        return ResponseResult.okResult();
    }

}
