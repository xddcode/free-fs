package com.free.fs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login() {
        if (getLoginUser() != null) {
            return "redirect:index";
        }
        return "login";
    }

    /**
     * 注册页
     */
    @GetMapping("/reg")
    public String reg() {

        return "reg";
    }

    /**
     * 主页
     */
    @GetMapping({"", "/index"})
    public String index(Model model) {
        if (getLoginUser() == null) {
            return "redirect:login";
        }
        // 登录用户信息
        model.addAttribute("loginUser", getLoginUser());
        return "index";
    }

    @GetMapping("/fileChoose")
    public String fileChoose() {

        return "fileChoose";
    }

    /**
     * 错误页
     */
    @RequestMapping("/error/{code}")
    public String error(@PathVariable("code") String code) {
        if ("403".equals(code)) {
            return "error/403";
        } else if ("500".equals(code)) {
            return "error/500";
        }
        return "error/404";
    }
}
