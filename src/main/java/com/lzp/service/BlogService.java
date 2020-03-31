package com.lzp.service;

import com.lzp.po.Blog;
import com.lzp.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    // 根据id查询博客
    Blog getBlogById(Long id);
    // 组合条件分页查询博客列表
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);
    // 新增博客
    Blog saveBlog(Blog blog);
    // 修改博客
    Blog updateBlog(Long id, Blog blog);
    // 删除博客
    void deleteBlog(Long id);
    // 分页查询博客列表
    Page<Blog> listBlog(Pageable pageable);
    // 首页最新推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);
    // 搜索博客
    Page<Blog> listBlog(String query, Pageable pageable);
    // 获取博客并转换成html格式
    Blog getBlogAndConvert(Long id);
    // 根据标签id获取所有博客列表
    Page<Blog> listBlog(Long tagId, Pageable pageable);
    // 根据年份归档博客
    Map<String, List<Blog>> archiveBlog();
    // 统计博客条数
    Long countBlog();
}
