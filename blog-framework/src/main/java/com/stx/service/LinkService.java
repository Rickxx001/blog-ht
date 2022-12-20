package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.AuditLinkDto;
import com.stx.domain.dto.ListLinkDto;
import com.stx.domain.dto.SaveLinkDto;
import com.stx.domain.dto.UpdateLinkDto;
import com.stx.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-11-21 20:11:09
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLinks(Integer pageNum, Integer pageSize, ListLinkDto dto);

    ResponseResult saveLink(SaveLinkDto dto);

    ResponseResult getLink(Long id);

    ResponseResult updateLink(UpdateLinkDto dto);

    ResponseResult deleteLink(Long id);

    ResponseResult auditLink(AuditLinkDto auditLinkDto);
}

