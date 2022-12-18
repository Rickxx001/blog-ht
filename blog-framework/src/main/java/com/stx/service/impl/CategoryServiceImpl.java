package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.CategoryDto;
import com.stx.domain.dto.PageListCategoryDto;
import com.stx.domain.dto.UpdateCategoryDto;
import com.stx.domain.entity.Article;
import com.stx.domain.entity.Category;
import com.stx.domain.vo.CategoryVo;
import com.stx.domain.vo.GetCategoryVo;
import com.stx.domain.vo.PageCategoryVo;
import com.stx.domain.vo.PageVo;
import com.stx.mapper.CategoryMapper;
import com.stx.service.ArticleService;
import com.stx.service.CategoryService;
import com.stx.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-11-20 20:40:13
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto dto) {
        Category category = BeanCopyUtils.copyBean(dto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategory(Long id) {
        Category byId = getById(id);
        GetCategoryVo getCategoryVo = BeanCopyUtils.copyBean(byId, GetCategoryVo.class);
        return ResponseResult.okResult(getCategoryVo);
    }

    @Override
    public ResponseResult saveCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageListCategory(Integer pageNum, Integer pageSize, PageListCategoryDto dto) {
        LambdaQueryWrapper<Category> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Strings.hasText(dto.getStatus()),Category::getStatus,dto.getStatus());
        articleLambdaQueryWrapper.eq(Strings.hasText(dto.getName()),Category::getName,dto.getName());
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page, articleLambdaQueryWrapper);
        List<PageCategoryVo> pageCategoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), PageCategoryVo.class);
        PageVo pageVo = new PageVo( pageCategoryVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getCategoryList() {
//        查询文章表中正式发布的文章的 分类id 且 去重
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
       articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(articleWrapper);
//       对获取的文章list  id去重
        Set<Long> categoryIds = list.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
//        根据1中查询的分类id 查询分类名列表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().
                filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
//          将过滤后的结果封装到vo中
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<Category> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(wrapper);

        return list;
    }
}

