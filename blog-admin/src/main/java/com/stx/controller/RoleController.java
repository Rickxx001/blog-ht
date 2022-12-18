package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.Role;
import com.stx.domain.vo.SaveRoleDto;
import com.stx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @SystemLog(businessName ="分页查询角色列表")
    @GetMapping("/list")
    public ResponseResult listRole(Integer pageNum, Integer pageSize,String status,String roleName){
        return roleService.listRole(pageNum,pageSize,status,roleName);
    }

    @SystemLog(businessName = "改变角色状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Role role){
        return roleService.changeStatus(role);
    }

    @SystemLog(businessName ="添加新用户")
    @PutMapping
    public ResponseResult saveRole(@RequestBody SaveRoleDto saveRole){
        return roleService.saveRole(saveRole);
    }



}
