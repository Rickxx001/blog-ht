package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stx.constants.SystemConstants;
import com.stx.domain.entity.LoginUser;
import com.stx.domain.entity.User;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.mapper.MenuMapper;
import com.stx.mapper.UserMapper;
import com.stx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//用户详细信息
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        根据用户名查询用户信息
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,s);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
//        判断是否查到用户， 没有就抛出异常
        if(Objects.isNull(user)){
            throw new SystemException(AppHttpCodeEnum.USER_ISNOT);
        }
//        查到用户 返回用户信息
        if(SystemConstants.ADMIN.equals(user.getType())){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        return new LoginUser(user);
    }
}
