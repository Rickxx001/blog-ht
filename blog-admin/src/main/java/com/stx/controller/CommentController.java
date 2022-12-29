package com.stx.controller;


import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.GetCommentDto;
import com.stx.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/comment")
public class CommentController {


    @Autowired
    private CommentService commentService;

    //   查询所有评论以及评论文，评论人  todo 未完成
    @SystemLog(businessName = "根据输入获取评论列表。")
    @GetMapping
    public ResponseResult getComment(Integer pageNum, Integer pageSize, GetCommentDto dto) {
        return commentService.getComment(pageNum,pageSize,dto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }



}
