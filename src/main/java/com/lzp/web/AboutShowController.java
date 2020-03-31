package com.lzp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {

    private static final String ABOUT = "/about";

    /**
     * 跳转关于我页面
     *
     * @return
     */
    @GetMapping("/about")
    public String about() {
        return ABOUT;
    }

}
