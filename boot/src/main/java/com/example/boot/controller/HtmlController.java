package com.example.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wzh
 * @since 2021/4/23
 */
@Controller
public class HtmlController {
    @RequestMapping("/")
    public String toIndex() {
        return "/index";
    }
}
