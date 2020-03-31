package com.lzp.web;

import com.lzp.po.Blog;
import com.lzp.po.Tag;
import com.lzp.po.Type;
import com.lzp.service.BlogService;
import com.lzp.service.TagService;
import com.lzp.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    private static final String INDEX = "/index";
    private static final String SEARCH = "/search";
    private static final String BLOG = "/blog";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 前台页面跳转
     *
     * @return
     */
    @GetMapping("/")
    public String toPage(@PageableDefault(size = 6, page = 0, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        // 首页数据初始化
        Page<Blog> blogs = this.blogService.listBlog(pageable);
        List<Type> types = this.typeService.listTypeTop(6);
        List<Tag> tags = this.tagService.listTagTop(10);
        List<Blog> recommendBlogs = this.blogService.listRecommendBlogTop(5);
        model.addAttribute("page", blogs);
        model.addAttribute("types", types);
        model.addAttribute("tags", tags);
        model.addAttribute("recommendBlogs", recommendBlogs);
        return INDEX;
    }

    /**
     * 搜索
     *
     * @param pageable
     * @param model
     * @param query
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 6, page = 0, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model, @RequestParam String query) {
        Page<Blog> blogs = this.blogService.listBlog("%" + query + "%", pageable);
        model.addAttribute("page", blogs);
        model.addAttribute("query", query);
        System.out.println(blogs.getTotalElements());
        return SEARCH;
    }

    /**
     * 博客详情
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable("id") Long id, Model model) {
        Blog blog = this.blogService.getBlogAndConvert(id);
        model.addAttribute("blog", blog);
        return BLOG;
    }

}
