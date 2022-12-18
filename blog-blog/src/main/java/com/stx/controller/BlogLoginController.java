package com.stx.controller;

import com.stx.domain.ResponseResult;
import com.stx.domain.entity.User;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
       if(!StringUtils.hasText(user.getUserName())){
//           抛出自定义异常
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
       }

        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
//        删除redis中的用户信息
        return blogLoginService.logout();
    }
}
