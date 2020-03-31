package com.lzp.dao;

import com.lzp.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 根据博客id查询所有父级评论列表
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

}
