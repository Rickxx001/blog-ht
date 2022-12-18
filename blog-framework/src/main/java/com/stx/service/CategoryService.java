package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.CategoryDto;
import com.stx.domain.dto.PageListCategoryDto;
import com.stx.domain.dto.UpdateCategoryDto;
import com.stx.domain.entity.Category;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-11-20 20:40:12
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<Category> listAllCategory();

    ResponseResult pageListCategory(Integer pageNum, Integer pageSize, PageListCategoryDto category);

    ResponseResult saveCategory(CategoryDto categoryDto);

    ResponseResult getCategory(Long id);

    ResponseResult updateCategory(UpdateCategoryDto dto);

    ResponseResult deleteCategory(Long id);
}

