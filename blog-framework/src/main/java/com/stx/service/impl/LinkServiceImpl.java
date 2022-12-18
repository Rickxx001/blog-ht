package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.Link;
import com.stx.domain.vo.AllLinkVo;
import com.stx.mapper.LinkMapper;
import com.stx.service.LinkService;
import com.stx.utils.BeanCopyUtils;
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
    public ResponseResult getAllLink() {
//        查询所有审核通过的友链
        LambdaQueryWrapper<Link> linkQueryWrapper = new LambdaQueryWrapper<>();
        linkQueryWrapper.eq(Link::getStatus,SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(linkQueryWrapper);
        List<AllLinkVo> allLinkVos = BeanCopyUtils.copyBeanList(list, AllLinkVo.class);
        return ResponseResult.okResult(allLinkVos);
    }
}

