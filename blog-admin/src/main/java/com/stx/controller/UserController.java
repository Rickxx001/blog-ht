package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.ChangeUserDto;
import com.stx.domain.dto.GetUserDto;
import com.stx.domain.dto.SaveUserDto;
import com.stx.domain.dto.updateUserDto;
import com.stx.domain.entity.User;
import com.stx.service.UserService;
import com.stx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;


    @SystemLog(businessName = "修改状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserDto dto) {
        return userService.changeStatus(dto);

    }

    @SystemLog(businessName = "分页获取用户列表")
    @GetMapping("/list")
    public ResponseResult getUser(Integer pageNum, Integer pageSize, GetUserDto dto) {
        return userService.getUser(pageNum, pageSize, dto);
    }

    @SystemLog(businessName = "新增用户")
    @PostMapping
    public ResponseResult saveUser(@RequestBody SaveUserDto dto) {
        User user = BeanCopyUtils.copyBean(dto, User.class);
        return userService.register(user, dto.getRoleIds());
    }

    @SystemLog(businessName = "删除用户")
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @SystemLog(businessName = "回显用户信息")
    @GetMapping("/{id}")
    public ResponseResult getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @SystemLog(businessName = "修改用户信息")
    @PutMapping
    public ResponseResult updateUser(@RequestBody updateUserDto dto) {
        return userService.updateUser(dto);
    }


}
