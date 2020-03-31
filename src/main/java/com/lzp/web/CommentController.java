package com.lzp.web;

import com.lzp.po.Blog;
import com.lzp.po.Comment;
import com.lzp.po.User;
import com.lzp.service.BlogService;
import com.lzp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;
    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 根据博客id获取评论列表
     *
     * @param blogId
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable("blogId") Long blogId, Model model) {
        model.addAttribute("comments", this.commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @PostMapping("/comments")
    public String saveComment(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(this.blogService.getBlogById(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
        this.commentService.saveComment(comment);
        // 发布评论成功，重定向至评论列表
        return "redirect:/comments/" + blogId;
    }

}
