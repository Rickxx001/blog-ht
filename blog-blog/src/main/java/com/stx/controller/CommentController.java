package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.AddCommentDto;
import com.stx.domain.entity.Comment;
import com.stx.service.CommentService;
import com.stx.utils.BeanCopyUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论" ,description = "评论相关")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/linkCommentList")
    @SystemLog(businessName= "友链评论列表")
    @ApiOperation(value = "友链评论列表",notes = "获取友链")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pag Num" , value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_LINK,null, pageNum, pageSize);
    }

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_ARTICLE,articleId,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto comment1){
        Comment comment = BeanCopyUtils.copyBean(comment1, Comment.class);
        return commentService.addComment(comment);
    }

}
