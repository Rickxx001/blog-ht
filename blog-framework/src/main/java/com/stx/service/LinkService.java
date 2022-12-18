package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-11-21 20:11:09
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}

