package com.lzp.dao;

import com.lzp.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // 根据标签名称查询标签
    Tag getTagByName(String name);
    // 首页标签展示
    @Query("select t from Tag t")
    List<Tag> listTagTop(Pageable pageable);
}
