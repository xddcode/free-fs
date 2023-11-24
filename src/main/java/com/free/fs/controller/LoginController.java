package com.free.fs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.free.fs.common.domain.R;
import com.free.fs.common.utils.StringUtil;
import com.free.fs.model.User;
import com.free.fs.service.UserService;
import com.ramostear.captcha.HappyCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginController
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login() {
        if (StpUtil.isLogin()) {
            return "redirect:index";
        }
        return "login2";
    }

    /**
     * 注册页
     */
    @GetMapping("/reg")
    public String reg() {

        return "reg2";
    }

    /**
     * 生成验证码 算术类型
     */
    @RequestMapping("/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        HappyCaptcha.require(request, response).build().finish();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(HttpServletRequest request, String username, String password, String code, boolean rememberMe) {
        if (StringUtil.isBlank(username, password)) {
            return R.failed("账号或密码不能为空");
        }
        boolean flag = HappyCaptcha.verification(request, code, true);
        if (!flag) {
            return R.failed("验证码不正确");
        }
        if (userService.doLogin(username, password, rememberMe)) {
            return R.succeed("登录成功");
        }
        return R.failed("登录失败");
    }

    /**
     * 注册
     */
    @PostMapping("/reg")
    @ResponseBody
    public R register(User user, HttpServletRequest request, String code) {
        boolean flag = HappyCaptcha.verification(request, code, true);
        if (!flag) {
            return R.failed("验证码不正确");
        }
        if (userService.addUser(user)) {
            return R.succeed("注册成功");
        }
        return R.failed("注册失败");
    }

    /**
     * 完善密码
     */
    @PostMapping("/informationPass")
    @ResponseBody
    public R informationPass(User user) {
        //注册
        if (userService.addUser(user)) {
            //登录
            User u = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
            StpUtil.login(u.getId(), true);
            return R.succeed("注册成功");
        }
        return R.succeed("注册失败");
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:login";
    }
}
