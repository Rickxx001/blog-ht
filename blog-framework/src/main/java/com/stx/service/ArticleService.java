package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.ArticleUpdateDto;
import com.stx.domain.dto.ListArticleDto;
import com.stx.domain.dto.WriteArticleDto;
import com.stx.domain.entity.Article;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-11-19 22:45:37
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult writeArticle(WriteArticleDto writeArticle);

    ResponseResult listArticleAdmin(Integer pageNum, Integer pageSize, ListArticleDto dto);

    ResponseResult selectArticle(Long id);

    ResponseResult updateArticle(ArticleUpdateDto dto);

    ResponseResult deleteArticle(Long id);
}

