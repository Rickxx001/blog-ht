package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.ChangeUserDto;
import com.stx.domain.dto.GetUserDto;
import com.stx.domain.dto.updateUserDto;
import com.stx.domain.entity.Role;
import com.stx.domain.entity.User;
import com.stx.domain.entity.UserRole;
import com.stx.domain.vo.*;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.mapper.UserMapper;
import com.stx.service.RoleService;
import com.stx.service.UserRoleService;
import com.stx.service.UserService;
import com.stx.utils.BeanCopyUtils;
import com.stx.utils.SecurityUtils;
import io.jsonwebtoken.lang.Strings;
import org.apache.poi.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-11-23 16:13:58
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;


    @Override
    public ResponseResult changeStatus(ChangeUserDto dto) {
        dto.setId(dto.getUserId());
        User user = BeanCopyUtils.copyBean(dto, User.class);
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateUser(updateUserDto dto) {
        User user = BeanCopyUtils.copyBean(dto, User.class);
        updateById(user);
        deletUserRole(user);
        List<UserRole> collect = dto.getRoleIds().stream()
                .map(rid -> new UserRole(user.getId(), rid))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    private void deletUserRole(User user) {
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(userRoleLambdaQueryWrapper);
    }

    @Override
    public ResponseResult getUser(Long id) {
        List<Long> roleIds = getRoleIds(id);
        List<Role> rolesList = roleService.getRolesList();
        User byId = getById(id);
        UserVO userVO = BeanCopyUtils.copyBean(byId, UserVO.class);
        GetUserXVo getUserXVo = new GetUserXVo(roleIds, rolesList, userVO);
        return ResponseResult.okResult(getUserXVo);
    }

    private List<Long> getRoleIds(Long id) {
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> list = userRoleService.list(userRoleLambdaQueryWrapper);
        List<Long> collect = list.stream()
                .map(entity -> entity.getRoleId())
                .collect(Collectors.toList());
        return collect;

    }

    @Override
    public ResponseResult deleteUser(Long id) {
        if (id.equals(1l)) {
            throw new SystemException(AppHttpCodeEnum.UPDATE_ADMIN_ERROR);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUser(Integer pageNum, Integer pageSize, GetUserDto dto) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(Strings.hasText(dto.getPhonenumber()), User::getPhonenumber, dto.getPhonenumber());
        userLambdaQueryWrapper.like(Strings.hasText(dto.getUserName()), User::getUserName, dto.getUserName());
        userLambdaQueryWrapper.like(Strings.hasText(dto.getStatus()), User::getStatus, dto.getStatus());
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage, userLambdaQueryWrapper);
        List<User> records = userPage.getRecords();
        List<GetUserVo> getUserVos = BeanCopyUtils.copyBeanList(records, GetUserVo.class);
        PageVo pageVo = new PageVo(getUserVos, userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult userInfo() {
//        获取用户当前id
        Long userId = SecurityUtils.getUserId();
//        根据用户id查询用户信息
        User user = getById(userId);
//        封装成userInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    //
    @Override
    public ResponseResult updateuserInfo(User user) {
        Long userId = SecurityUtils.getUserId();
        if (user.getId() != userId) {
            throw new SystemException(AppHttpCodeEnum.USER_ERROR);
        }
        updateById(user);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult register(User user, List<Long> ids) {
//        对数据非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOTNULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOTNULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOTNULL);
        }

//        对数据验重
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (userNickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (userEmailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
//        加密存库
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);
//        添加角色
        if (CollectionUtils.isEmpty(ids)) {
            userRoleService.save(new UserRole(user.getId(), SystemConstants.USER_MOER));
        }
        List<UserRole> collect = ids.stream()
                .map(id -> new UserRole(user.getId(), id))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    private boolean userEmailExist(String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        int count = count(userLambdaQueryWrapper);
        return count > 0;
    }

    private boolean userNickNameExist(String nickName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getNickName, nickName);
        int count = count(userLambdaQueryWrapper);
        return count > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, userName);
        int count = count(userLambdaQueryWrapper);
        return count > 0;
    }
}

