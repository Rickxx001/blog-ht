package com.stx.service;

import com.stx.domain.ResponseResult;
import com.stx.domain.dto.LoginDto;
import com.stx.domain.vo.AdminUserInfoVo;
import org.springframework.stereotype.Service;


public interface LoginService {
    ResponseResult login(LoginDto user);

    ResponseResult logout();

    ResponseResult<AdminUserInfoVo> getInfo();

    ResponseResult<AdminUserInfoVo> getRouters();
}
