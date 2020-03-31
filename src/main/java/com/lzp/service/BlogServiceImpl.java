package com.lzp.service;

import com.lzp.NotFoundException;
import com.lzp.dao.BlogRepository;
import com.lzp.po.Blog;
import com.lzp.po.Type;
import com.lzp.utils.MarkdownUtils;
import com.lzp.utils.MyBeanUtils;
import com.lzp.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 根据id查询博客
     *
     * @param id
     * @return
     */
    @Override
    public Blog getBlogById(Long id) {
        Optional<Blog> optional = this.blogRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException("不存在该博客");
        }
        Blog blog = optional.get();
        return blog;
    }

    /**
     * 组合条件分页查询博客列表
     *
     * @param pageable
     * @param blog
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        Page<Blog> blogs = this.blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // 设置查询条件查询
                // 根据title模糊查询
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                // 根据分类查询
                if (blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                // 根据是否为推荐
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
        return blogs;
    }

    /**
     * 新增博客
     *
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        // 初始化创建时间，更新时间。浏览次数
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        Blog resultBlog = this.blogRepository.save(blog);
        return resultBlog;
    }

    /**
     * 修改博客
     *
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog returnBlog = this.getBlogById(id);
        if (returnBlog == null) {
            throw new NotFoundException("不存在该博客");
        }
        BeanUtils.copyProperties(blog, returnBlog, MyBeanUtils.getNullPropertyNames(blog));
        returnBlog.setUpdateTime(new Date());
        return this.blogRepository.save(returnBlog);
    }

    /**
     * 删除博客
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        this.blogRepository.deleteById(id);
    }

    /**
     * 分页查询博客列表
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        Page<Blog> blogs = this.blogRepository.findAll(pageable);
        return blogs;
    }

    /**
     * 首页最新推荐博客列表
     *
     * @param size
     * @return
     */
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Blog> recommendBlogs = this.blogRepository.listRecommendBlogTop(pageable);
        return recommendBlogs;
    }

    /**
     * 搜索博客
     *
     * @param query
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        Page<Blog> blogs = this.blogRepository.listQueryBlog(query, pageable);
        return blogs;
    }

    /**
     * 获取博客并转换成html格式
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getBlogAndConvert(Long id) {
        Blog blog = this.getBlogById(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog returnBlog = new Blog();
        BeanUtils.copyProperties(blog, returnBlog);
        String content = returnBlog.getContent();
        returnBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        // 更新博客浏览次数
        this.blogRepository.updateViews(id);
        return returnBlog;
    }

    /**
     * 根据标签id获取所有博客列表
     *
     * @param tagId
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return this.blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    /**
     * 根据年份归档博客
     *
     * @return
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        // 根据年份分组
        List<String> years = this.blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, this.blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 统计博客条数
     *
     * @return
     */
    @Override
    public Long countBlog() {
        return this.blogRepository.count();
    }
}
