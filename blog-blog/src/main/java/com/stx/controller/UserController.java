package com.stx.controller;

import com.stx.domain.ResponseResult;
import com.stx.domain.entity.User;
import com.stx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();

    }

//    TODO  封装dto对象
    @PutMapping("/userInfo")
    public ResponseResult updateuserInfo(@RequestBody User user){
       return userService.updateuserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
