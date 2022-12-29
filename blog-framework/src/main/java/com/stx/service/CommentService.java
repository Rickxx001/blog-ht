package com.stx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.GetCommentDto;
import com.stx.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-11-23 15:31:27
 */
public interface CommentService extends IService<Comment> {


    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

    ResponseResult getComment(Integer pageNum, Integer pageSize,GetCommentDto dto);

    ResponseResult deleteComment(Long id);
}

