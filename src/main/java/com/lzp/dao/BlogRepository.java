package com.lzp.dao;

import com.lzp.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    // 首页最新推荐博客列表
    @Query("select t from Blog t where t.recommend = true ")
    List<Blog> listRecommendBlogTop(Pageable pageable);

    // 搜索博客
    @Query("select t from Blog t where t.title like ?1 or t.content like ?1")
    Page<Blog> listQueryBlog(String query, Pageable pageable);

    // 更新浏览次数
    @Transactional
    @Modifying
    @Query("update Blog t set t.views = t.views + 1 where t.id = ?1")
    int updateViews(Long id);

    // 根据博客更新年份分组
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    // 根据博客更新年份查询博客集合
    @Query("select b from Blog b where function('date_format', b.updateTime, '%Y') = ?1 ")
    List<Blog> findByYear(String year);
}
