package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.ArticleUpdateDto;
import com.stx.domain.dto.ListArticleDto;
import com.stx.domain.dto.WriteArticleDto;
import com.stx.domain.entity.Article;
import com.stx.domain.entity.ArticleTag;
import com.stx.domain.entity.Category;
import com.stx.domain.entity.Tag;
import com.stx.domain.vo.*;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.mapper.ArticleDao;
import com.stx.service.ArticleService;
import com.stx.service.CategoryService;
import com.stx.utils.BeanCopyUtils;
import com.stx.utils.RedisCache;
import com.stx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-11-19 22:45:39
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagServiceImpl articleTagService;

    @Override
    public ResponseResult deleteArticle(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(ArticleUpdateDto dto) {
        Article article = BeanCopyUtils.copyBean(dto, Article.class);
        updateById(article);
        List<ArticleTag> collect = dto.getTags().stream()
                .map(tag -> new ArticleTag(dto.getId(), tag))
                .collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,dto.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectArticle(Long id) {
        Article byId = getById(id);
        ArticleUpdateVo articleUpdateVo = BeanCopyUtils.copyBean(byId, ArticleUpdateVo.class);
        LambdaQueryWrapper<ArticleTag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> list = articleTagService.list(tagLambdaQueryWrapper);
        List<Long> collect = list.stream()
                .map(a -> a.getTagId())
                .collect(Collectors.toList());
        articleUpdateVo.setTags(collect);
        return ResponseResult.okResult(articleUpdateVo);
    }


    @Override
    public ResponseResult listArticleAdmin(Integer pageNum, Integer pageSize, ListArticleDto dto) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.like(StringUtils.hasText(dto.getTitle()), Article::getTitle, dto.getTitle());
        articleLambdaQueryWrapper.like(StringUtils.hasText(dto.getSummary()), Article::getSummary, dto.getSummary());
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, articleLambdaQueryWrapper);
        List<Article> records = page.getRecords();
        List<ListArticleVo> listArticleVos = BeanCopyUtils.copyBeanList(records, ListArticleVo.class);
        PageVo pageVo = new PageVo(listArticleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //  查询所有文章
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    // 热门文章查询
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> records = page.getRecords();
//        使用vo 处理
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
//        根据id 查询文章

        Article byId = getById(id);
//

        Category category = categoryService.getById(byId.getCategoryId());
        if (category != null) {
            byId.setCategoryName(category.getName());
        }
//        根据分类id 查询分类名

//        转换为vo

        return ResponseResult.okResult(byId);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW, id.toString(), 1);
        return ResponseResult.okResult();
    }


    @Override
    @Transactional
    public ResponseResult writeArticle(WriteArticleDto writeArticle) {
        if (!StringUtils.hasText(writeArticle.getTitle()) && !StringUtils.hasText(writeArticle.getContent())) {
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOTNULL);
        }

        Article article = BeanCopyUtils.copyBean(writeArticle, Article.class);
        save(article);   //mp 再调用后会返回最新的数据给原本的对象

        List<ArticleTag> collect = writeArticle.getTags().stream()
                .map(tagid -> new ArticleTag(article.getId(), tagid))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }
}

