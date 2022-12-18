package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.LoginDto;
import com.stx.domain.entity.User;
import com.stx.domain.vo.AdminUserInfoVo;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
   private LoginService LoginService;

    @SystemLog(businessName ="用户登录")
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody LoginDto user){
       if(!StringUtils.hasText(user.getUserName())){
//           抛出自定义异常
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
       }

        return LoginService.login(user);
    }

    @SystemLog(businessName ="获取用户权限菜单")
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        return LoginService.getInfo();
    }

    @SystemLog(businessName ="获取用户菜单路由地址")
    @GetMapping("getRouters")
    public ResponseResult<AdminUserInfoVo> getRouters(){
        return LoginService.getRouters();
    }

    @SystemLog(businessName ="退出登录")
    @PostMapping("/user/logout")
    public ResponseResult logout(){
//        删除redis中的用户信息
        return LoginService.logout();
    }
}
