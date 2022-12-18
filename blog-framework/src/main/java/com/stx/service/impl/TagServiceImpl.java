package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.TagDto;
import com.stx.domain.entity.Tag;
import com.stx.domain.vo.PageVo;
import com.stx.domain.vo.TagListVo;
import com.stx.domain.vo.TagVo;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.mapper.TagMapper;
import com.stx.service.TagService;
import com.stx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-08 21:49:34
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult listTag(Integer pageNum, Integer pageSize, TagDto tagDto) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//         查询条件
        tagLambdaQueryWrapper.like(StringUtils.hasText(tagDto.getName()), Tag::getName, tagDto.getName());
        tagLambdaQueryWrapper.like(StringUtils.hasText(tagDto.getRemark()), Tag::getRemark, tagDto.getRemark());

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, tagLambdaQueryWrapper);
        List<Tag> tagList = page.getRecords();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        PageVo pageVo = new PageVo(tagVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long tagId) {
        removeById(tagId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        Tag byId = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(byId, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagVo tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        return updateById(tag)?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.FAILURE);
    }

    @Override
    public ResponseResult tagList() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}

