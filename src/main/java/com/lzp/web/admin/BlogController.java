package com.lzp.web.admin;

import com.lzp.po.Blog;
import com.lzp.po.Tag;
import com.lzp.po.Type;
import com.lzp.po.User;
import com.lzp.service.BlogService;
import com.lzp.service.TagService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "/admin/blogs-input";
    private static final String BLOGS = "/admin/blogs";
    private static final String REDIRECT_BLOGS = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 跳转博客分类列表
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String toBlogs(@PageableDefault(size = 5, page = 0, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        Page<Blog> blogs = this.blogService.listBlog(pageable, blog);
        model.addAttribute("page", blogs);
        // 初始化分类
        List<Type> types = this.typeService.listType();
        model.addAttribute("types", types);
        return BLOGS;
    }

    /**
     * 搜索
     *
     * @param pageable
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, page = 0, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        Page<Blog> blogs = this.blogService.listBlog(pageable, blog);
        model.addAttribute("page", blogs);
        return "admin/blogs :: blogList";
    }

    /**
     * 跳转新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        // 初始化分类和标签
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    /**
     * 保存博客和修改博客
     *
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String saveBlog(Blog blog, RedirectAttributes attributes, HttpSession session) {
        // 初始化用户，分类，标签
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(this.typeService.getType(blog.getType().getId()));
        blog.setTags(this.tagService.listTag(blog.getTagIds()));
        // 新增博客或修改博客
        Blog resultBlog;
        if (blog.getId() == null) {
            resultBlog = this.blogService.saveBlog(blog);
        } else {
            resultBlog = this.blogService.updateBlog(blog.getId(), blog);
        }
        if (resultBlog == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_BLOGS;
    }

    /**
     * 删除博客
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable("id") Long id, RedirectAttributes attributes) {
        this.blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_BLOGS;
    }

    /**
     * 初始化分类和标签
     *
     * @param model
     */
    public void setTypeAndTag(Model model) {
        model.addAttribute("types", this.typeService.listType());
        model.addAttribute("tags", this.tagService.listTag());
    }

    /**
     * 跳转修改博客页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable("id") Long id, Model model) {
        // 初始化分类，标签
        setTypeAndTag(model);
        Blog blog = this.blogService.getBlogById(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }
}
