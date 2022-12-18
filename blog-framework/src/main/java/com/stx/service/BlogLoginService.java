package com.stx.service;

import com.stx.domain.ResponseResult;
import com.stx.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
