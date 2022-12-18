package com.stx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stx.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-08 21:49:32
 */
public interface TagMapper extends BaseMapper<Tag> {

}
