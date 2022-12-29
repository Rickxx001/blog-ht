package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/article")
@Api(tags = "文章")
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    @PutMapping("/updateViewCount/{id}")
    @SystemLog(businessName="查询文章浏览量")
    public ResponseResult updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }

    // 热门文章列表

    @SystemLog(businessName ="获取热门文章列表")
    @ApiOperation(value = "热门文章列表" ,notes="获取前十篇热门文章")
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        return articleService.hotArticleList();
    }


    //    分页查询 文章列表
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //    展示文章详情
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable Long id) {
        return articleService.getArticleDetail(id);
    }


    @GetMapping("/getArticleById")
    public ResponseResult getArticle(Integer pageNum,Integer pageSize,String title){
        return articleService.getArticle(pageNum,pageSize,title);
    }

}