package com.lzp.service;

import com.lzp.po.Comment;

import java.util.List;

public interface CommentService {
    // 根据博客id查询评论列表
    List<Comment> listCommentByBlogId(Long blogId);
    // 发布评论
    Comment saveComment(Comment comment);
}
