package com.lzp.web;

import com.lzp.po.Blog;
import com.lzp.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchivesShowController {

    private static final String ARCHIVES = "/archives";

    @Autowired
    private BlogService blogService;

    /**
     * 跳转归档页面
     *
     * @param model
     * @return
     */
    @GetMapping("/archives")
    public String archives(Model model) {
        Map<String, List<Blog>> archiveMap = this.blogService.archiveBlog();
        Long blogCount = this.blogService.countBlog();
        model.addAttribute("archiveMap", archiveMap);
        model.addAttribute("blogCount", blogCount);
        return ARCHIVES;
    }

}
