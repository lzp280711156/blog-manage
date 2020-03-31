package com.lzp.dao;

import com.lzp.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    // 根据名称查询分类
    Type getTypeByName(String name);

    // 首页分类展示
    @Query("select t from Type t")
    List<Type> listTypeTop(Pageable pageable);
}
