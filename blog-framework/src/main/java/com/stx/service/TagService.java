package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.TagDto;
import com.stx.domain.entity.Tag;
import com.stx.domain.vo.PageVo;
import com.stx.domain.vo.TagVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-12-08 21:49:34
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> listTag(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult deleteTag(Long tagId);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(TagVo tagDto);

    ResponseResult tagList();
}

