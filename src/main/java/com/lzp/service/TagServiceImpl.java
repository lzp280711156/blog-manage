package com.lzp.service;

import com.lzp.NotFoundException;
import com.lzp.dao.TagRepository;
import com.lzp.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Ids;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * 分页查询标签列表
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        Page<Tag> tags = this.tagRepository.findAll(pageable);
        return tags;
    }

    /**
     * 根据标签名称查询标签
     *
     * @param name
     * @return
     */
    @Override
    public Tag getTagByName(String name) {
        Tag resultTag = this.tagRepository.getTagByName(name);
        return resultTag;
    }

    /**
     * 新增标签
     *
     * @param tag
     * @return
     */
    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return this.tagRepository.save(tag);
    }

    /**
     * 删除标签
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteTag(Long id) {
        this.tagRepository.deleteById(id);
    }

    /**
     * 修改标签
     *
     * @param id
     * @param tag
     * @return
     */
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag returnTag = this.getTagById(id);
        if (returnTag == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag,returnTag);
        return this.tagRepository.save(returnTag);
    }

    /**
     * 根据id查询标签
     *
     * @param id
     * @return
     */
    @Override
    public Tag getTagById(Long id) {
        Tag tag = new Tag();
        tag.setId(id);
        Example<Tag> example = Example.of(tag);
        Optional<Tag> optional = this.tagRepository.findOne(example);
        if (!optional.isPresent()) {
            throw new NotFoundException("不存在该标签");
        }
        Tag tag1 = optional.get();
        return tag1;
    }

    /**
     * 查询所有标签
     *
     * @return
     */
    @Override
    public List<Tag> listTag() {
        List<Tag> tags = this.tagRepository.findAll();
        return tags;
    }

    /**
     * 根据ids查询标签集合
     *
     * @param tagIds
     * @return
     */
    @Override
    public List<Tag> listTag(String tagIds) {
        List<Tag> tags = this.tagRepository.findAllById(convertToList(tagIds));
        return tags;
    }

    // 首页标签展示
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Tag> tags = this.tagRepository.listTagTop(pageable);
        return tags;
    }

    /**
     * 将ids转list
     *
     * @param ids
     * @return
     */
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {
                list.add(Long.valueOf(idArray[i]));
            }
        }
        return list;
    }

}
