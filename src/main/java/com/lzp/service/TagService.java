package com.lzp.service;

import com.lzp.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    // 分页查询标签列表
    Page<Tag> listTag(Pageable pageable);
    // 根据标签名称查询标签
    Tag getTagByName(String name);
    // 新增标签
    Tag saveTag(Tag tag);
    // 删除标签
    void deleteTag(Long id);
    // 修改标签
    Tag updateTag(Long id, Tag tag);
    // 根据id查询标签
    Tag getTagById(Long id);
    // 查询所有标签
    List<Tag> listTag();
    // 根据ids查询标签集合
    List<Tag> listTag(String tagIds);
    //　首页标签展示
    List<Tag> listTagTop(Integer size);
}
