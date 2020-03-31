package com.lzp.service;

import com.lzp.NotFoundException;
import com.lzp.dao.TypeRepository;
import com.lzp.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    /**
     * 新增分类
     *
     * @param type
     * @return
     */
    @Transactional
    @Override
    public Type saveType(Type type) {
        return this.typeRepository.save(type);
    }

    /**
     * 根据id查询type
     *
     * @param id
     * @return
     */
    @Override
    public Type getType(Long id) {
        Type type = new Type();
        type.setId(id);
        Example<Type> example = Example.of(type);
        Optional<Type> optional = this.typeRepository.findOne(example);
        if (!optional.isPresent()) {
            throw new NotFoundException("不存在该分类");
        }
        Type resultType = optional.get();
        return resultType;
    }

    /**
     * 分页查询
     *
     * @param
     * @return
     */
    @Override
    public Page<Type> listType(Pageable pageable) {
        Page<Type> typeList = this.typeRepository.findAll(pageable);
        return typeList;
    }

    /**
     * 修改分类
     *
     * @param id
     * @param type
     * @return
     */
    @Override
    public Type updateType(Long id, Type type) {
        Type returnType = this.getType(id);
        if (returnType == null) {
            throw new NotFoundException("不存在该分类");
        }
        BeanUtils.copyProperties(type, returnType);
        return this.typeRepository.save(returnType);
    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteType(Long id) {
        this.typeRepository.deleteById(id);
    }

    /**
     * 根据名称查询分类
     *
     * @param name
     * @return
     */
    @Override
    public Type getTypeByName(String name) {
        return this.typeRepository.getTypeByName(name);
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<Type> listType() {
        List<Type> types = this.typeRepository.findAll();
        return types;
    }

    /**
     * 首页分类展示
     *
     * @param size
     * @return
     */
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Type> types = this.typeRepository.listTypeTop(pageable);
        return types;
    }
}
