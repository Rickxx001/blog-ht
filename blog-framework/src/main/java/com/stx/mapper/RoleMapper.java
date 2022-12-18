package com.stx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stx.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-09 19:20:12
 */
public interface RoleMapper extends BaseMapper<Role> {


    List<String> selectRoleKeyByUserId(Long userId);
}
