package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-11-23 16:13:57
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateuserInfo(User user);

    ResponseResult register(User user);
}

