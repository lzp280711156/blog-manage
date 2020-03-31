package com.lzp.web.admin;

import com.lzp.po.User;
import com.lzp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    private static final String INDEX = "/admin/index";
    private static final String LOGIN = "/admin/login";
    private static final String REDIRECT_LOGIN = "redirect:/admin";

    @Autowired
    private UserService userService;

    /**
     * 跳转后台登陆页面
     *
     * @return
     */
    @GetMapping
    public String loginPage() {
        return LOGIN;
    }

    /**
     * 登陆功能
     *
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, RedirectAttributes attributes) {
        User user = this.userService.checkUser(username, password);
        if (user != null) {
            // 登陆成功，跳转首页，并将user对象返回，密码置空
            user.setPassword(null);
            session.setAttribute("user", user);
            return INDEX;
        } else {
            // 登陆失败，重定向至登陆页面，返回错误信息
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return REDIRECT_LOGIN;
        }
    }

    /**
     * 注销功能
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 注销时删除session中的用户信息，重定向至登陆页面
        session.removeAttribute("user");
        return REDIRECT_LOGIN;
    }

}
