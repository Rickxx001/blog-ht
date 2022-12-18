package com.stx.controller;


import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.ArticleUpdateDto;
import com.stx.domain.dto.ListArticleDto;
import com.stx.domain.dto.WriteArticleDto;
import com.stx.domain.vo.ListArticleVo;
import com.stx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @SystemLog(businessName ="写笔记")
    @PostMapping
    public ResponseResult writeArticle(@RequestBody WriteArticleDto writeArticle) {
        return articleService.writeArticle(writeArticle);
    }

    @SystemLog(businessName ="分页查询笔记详情")
    @GetMapping("/list")
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, ListArticleDto dto) {
        return articleService.listArticleAdmin(pageNum, pageSize, dto);
    }
    @SystemLog(businessName ="回显笔记详情")
    @GetMapping("/{id}")
    public ResponseResult selectArticle(@PathVariable Long id){
        return articleService.selectArticle(id);
    }

    @SystemLog(businessName ="修改笔记")
    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleUpdateDto dto){
        return articleService.updateArticle(dto);
    }
    @SystemLog(businessName ="删除笔记")
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

}
