package com.stx.service.impl;

import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.LoginDto;
import com.stx.domain.entity.LoginUser;
import com.stx.domain.entity.Menu;
import com.stx.domain.entity.User;
import com.stx.domain.vo.*;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.service.LoginService;
import com.stx.service.MenuService;
import com.stx.service.RoleService;
import com.stx.utils.BeanCopyUtils;
import com.stx.utils.JwtUtil;
import com.stx.utils.RedisCache;
import com.stx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MenuService  menuService;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult login(LoginDto user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw  new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
//        获取user id 生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String UserId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(UserId);
//      把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.ADMIN_LOGIN+UserId,loginUser);
//        把token 和userinfo 封装 返回
        HashMap<String,String> UserHashMap = new HashMap<>();
        UserHashMap.put(SystemConstants.HEADER_TOKEN,jwt);
        return ResponseResult.okResult(UserHashMap);

    }

    @Override
    public ResponseResult<AdminUserInfoVo> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = loginUser.getUser();
//        查询权限信息
        List<String> parms= menuService.selectPermsByUserId(user.getId());
//        查询用户 角色
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(user.getId());
//        List<String> roleKeyList = null;
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfo = new AdminUserInfoVo(parms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfo);
    }

    @Override
    public ResponseResult<AdminUserInfoVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
       List<MenuVo> menus =  menuService.selectRouterMenuTreeByUserId(userId);

        return ResponseResult.okResult(new RoutersVo(menus));
    }

    //
    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        Long userId = principal.getUser().getId();
//        删除 redis 中对应id 的用户信息
        redisCache.deleteObject(SystemConstants.ADMIN_LOGIN+userId);

        return ResponseResult.okResult();
    }
}
