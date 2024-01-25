package com.free.fs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.domain.Result;
import com.free.fs.common.utils.CaptchaUtil;
import com.free.fs.common.utils.StringUtil;
import com.free.fs.domain.User;
import com.free.fs.domain.dto.LoginBody;
import com.free.fs.domain.dto.UserDto;
import com.free.fs.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * LoginController
 */
@Validated
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
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<?> login(@Valid @RequestBody LoginBody body, HttpServletRequest request) {
        if (StringUtil.isBlank(body.getUsername(), body.getPassword())) {
            return Result.error("账号或密码不能为空");
        }
        boolean flag = CaptchaUtil.verify(request, body.getCode());
        if (!flag) {
            return Result.error("验证码不正确");
        }
        if (userService.doLogin(body)) {
            return Result.ok("登录成功");
        }
        return Result.error("登录失败");
    }

    /**
     * 注册
     */
    @PostMapping("/reg")
    @ResponseBody
    public Result<?> register(@Valid @RequestBody UserDto user, HttpServletRequest request) {
        boolean flag = CaptchaUtil.verify(request, user.getCode());
        if (!flag) {
            return Result.error("验证码不正确");
        }
        if (userService.addUser(user)) {
            return Result.ok("注册成功");
        }
        return Result.error("注册失败");
    }

    /**
     * 完善密码
     */
//    @PostMapping("/informationPass")
//    @ResponseBody
//    public Result<?> informationPass(User user) {
//        //注册
//        if (userService.addUser(user)) {
//            //登录
//            User u = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
//            StpUtil.login(u.getId(), true);
//            return Result.ok("注册成功");
//        }
//        return Result.error("注册失败");
//    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:login";
    }
}
