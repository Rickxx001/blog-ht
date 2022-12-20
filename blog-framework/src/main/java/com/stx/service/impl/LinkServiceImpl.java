package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.AuditLinkDto;
import com.stx.domain.dto.ListLinkDto;
import com.stx.domain.dto.SaveLinkDto;
import com.stx.domain.dto.UpdateLinkDto;
import com.stx.domain.entity.Link;
import com.stx.domain.vo.AllLinkVo;
import com.stx.domain.vo.GetLink;
import com.stx.domain.vo.ListLinkVo;
import com.stx.domain.vo.PageVo;
import com.stx.mapper.LinkMapper;
import com.stx.service.LinkService;
import com.stx.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-11-21 20:11:10
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {


    @Override
    public ResponseResult auditLink(AuditLinkDto auditLinkDto) {
        Link link = BeanCopyUtils.copyBean(auditLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateLink(UpdateLinkDto dto) {
        Link link = BeanCopyUtils.copyBean(dto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLink(Long id) {
        Link byId = getById(id);
        GetLink getLink = BeanCopyUtils.copyBean(byId, GetLink.class);
        return ResponseResult.okResult(getLink);
    }

    @Override
    public ResponseResult saveLink(SaveLinkDto dto) {
        Link link = BeanCopyUtils.copyBean(dto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllLink() {
//        查询所有审核通过的友链
        LambdaQueryWrapper<Link> linkQueryWrapper = new LambdaQueryWrapper<>();
        linkQueryWrapper.eq(Link::getStatus,SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(linkQueryWrapper);
        List<AllLinkVo> allLinkVos = BeanCopyUtils.copyBeanList(list, AllLinkVo.class);
        return ResponseResult.okResult(allLinkVos);
    }

    @Override
    public ResponseResult listLinks(Integer pageNum, Integer pageSize, ListLinkDto dto) {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Strings.hasText(dto.getStatus()),Link::getStatus, dto.getStatus());
        linkLambdaQueryWrapper.eq(Strings.hasText(dto.getName()),Link::getName, dto.getName());
        Page<Link> linkPage = new Page<>(pageNum,pageSize);
        page(linkPage, linkLambdaQueryWrapper);
        List<Link> records = linkPage.getRecords();
        List<ListLinkVo> listLinkVos = BeanCopyUtils.copyBeanList(records, ListLinkVo.class);
        PageVo pageVo = new PageVo(listLinkVos,linkPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}

