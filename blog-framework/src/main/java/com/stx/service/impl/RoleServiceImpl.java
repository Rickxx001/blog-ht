package com.stx.service.impl;

import com.aliyun.oss.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.UpdateRoleDto;
import com.stx.domain.entity.Role;
import com.stx.domain.entity.RoleMenu;
import com.stx.domain.vo.*;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.mapper.RoleMapper;
import com.stx.service.RoleMenuService;
import com.stx.service.RoleService;
import com.stx.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-12-09 19:20:14
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMenuService roleMenuService;


    @Override
    public ResponseResult listAllRole() {
        List<Role> list = getRolesList();
        List<ListAllRoleVo> listAllRoleVos = BeanCopyUtils.copyBeanList(list, ListAllRoleVo.class);
        return ResponseResult.okResult(listAllRoleVos);
    }

    //  获取角色列表
    public List<Role> getRolesList() {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(roleLambdaQueryWrapper);
        return list;
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        if (id.equals(1l)) {
            throw new SystemException(AppHttpCodeEnum.UPDATE_ADMIN_ERROR);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateRole(UpdateRoleDto dto) {
        Role role = BeanCopyUtils.copyBean(dto, Role.class);
        updateById(role);
        List<RoleMenu> collect = dto.getMenuIds().stream()
                .map(m -> new RoleMenu(role.getId(), m))
                .collect(Collectors.toList());
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuService.remove(roleMenuLambdaQueryWrapper);
        roleMenuService.saveBatch(collect);
        return null;
    }

    @Override
    public ResponseResult getRoleDetails(Long id) {
        Role byId = getById(id);
        GetRoleDetailsVo getRoleDetailsVo = BeanCopyUtils.copyBean(byId, GetRoleDetailsVo.class);
        return ResponseResult.okResult(getRoleDetailsVo);
    }

    @Override
    @Transactional
    public ResponseResult saveRole(SaveRoleDto saveRole) {
        Role role = BeanCopyUtils.copyBean(saveRole, Role.class);
        save(role);
        List<RoleMenu> collect = saveRole.getMenuIds().stream()
                .map(m -> new RoleMenu(role.getId(), m))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
//        角色判断 是就返回admin
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
//        否则查询用户具有的角色信息
        List<String> roleKeys = getBaseMapper().selectRoleKeyByUserId(id);
        return roleKeys;
    }


    @Override
    public ResponseResult changeStatus(Role role) {
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String status, String roleName) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.like(Strings.hasText(status), Role::getStatus, status);
        roleLambdaQueryWrapper.like(Strings.hasText(roleName), Role::getRoleName, roleName);
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, roleLambdaQueryWrapper);
        List<Role> records = page.getRecords();
        List<RoleListVo> roleListVos = BeanCopyUtils.copyBeanList(records, RoleListVo.class);
        PageVo pageVo = new PageVo(roleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}

