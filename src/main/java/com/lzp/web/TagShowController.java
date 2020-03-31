package com.lzp.web;

import com.lzp.po.Blog;
import com.lzp.po.Tag;
import com.lzp.service.BlogService;
import com.lzp.service.TagService;
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

import javax.jws.WebParam;
import java.util.List;

@Controller
public class TagShowController {

    private static final String TAGS = "/tags";

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(page = 0, size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model, @PathVariable("id") Long id) {
        // 查询所有标签
        List<Tag> tags = this.tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        // 查询该标签的所有博客
        Page<Blog> blogs = this.blogService.listBlog(id, pageable);
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogs);
        model.addAttribute("activeTagId", id);
        return TAGS;
    }

}
