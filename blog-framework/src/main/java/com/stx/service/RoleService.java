package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.UpdateRoleDto;
import com.stx.domain.entity.Role;
import com.stx.domain.vo.SaveRoleDto;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-12-09 19:20:14
 */
public interface RoleService extends IService<Role> {

     List<Role> getRolesList();
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRole(Integer pageNum, Integer pageSize, String status, String roleName);

    ResponseResult changeStatus(Role role);

    ResponseResult saveRole(SaveRoleDto saveRole);

    ResponseResult getRoleDetails(Long id);

    ResponseResult updateRole(UpdateRoleDto dto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();
}

