package com.lzp.web;

import com.lzp.po.Blog;
import com.lzp.po.Type;
import com.lzp.service.BlogService;
import com.lzp.service.TypeService;
import com.lzp.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TypeShowController {

    private static final String TYPES = "/types";

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    /**
     * 跳转分类页面
     *
     * @param pageable
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/types/{id}")
    public String types(@PageableDefault(page = 0, size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model, @PathVariable("id") Long id) {
        // 查询所有分类
        List<Type> types = this.typeService.listTypeTop(10000);
        if (id == -1) {
            // 首页进入分类页，默认选第一个分类
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        Page<Blog> blogs = this.blogService.listBlog(pageable, blogQuery);
        model.addAttribute("types", types);
        model.addAttribute("page", blogs);
        model.addAttribute("activeTypeId", id);
        return TYPES;
    }

}
