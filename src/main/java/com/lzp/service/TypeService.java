package com.lzp.service;

import com.lzp.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    // 新增分类
    Type saveType(Type type);
    // 根据id查询分类
    Type getType(Long id);
    // 分页
    Page<Type> listType(Pageable pageable);
    // 修改分类
    Type updateType(Long id, Type type);
    // 删除分类
    void deleteType(Long id);
    // 根据名称查询分类
    Type getTypeByName(String name);
    // 查询所有分类
    List<Type> listType();
    // 首页分类展示
    List<Type> listTypeTop(Integer size);
}
