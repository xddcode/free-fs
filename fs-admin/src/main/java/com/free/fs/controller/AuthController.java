package com.free.fs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.domain.Result;
import com.free.fs.core.domain.dto.LoginBody;
import com.free.fs.core.domain.dto.UserDTO;
import com.free.fs.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

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

    @PostMapping("/login")
    @ResponseBody
    public Result<?> login(@Valid @RequestBody LoginBody body) {
        if (userService.doLogin(body)) {
            return Result.ok("登录成功");
        }
        return Result.error("登录失败");
    }

    @PostMapping("/register")
    @ResponseBody
    public Result<?> register(@Valid UserDTO dto) {
        if (userService.addUser(dto)) {
            return Result.ok("注册成功");
        }
        return Result.error("注册失败");
    }

    @GetMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:login";
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

}
