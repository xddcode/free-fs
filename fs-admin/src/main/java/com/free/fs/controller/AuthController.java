package com.free.fs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.domain.Result;
import com.free.fs.domain.dto.LoginBody;
import com.free.fs.domain.dto.UserDto;
import com.free.fs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "认证管理")
@Validated
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginBody body) {
        Map<String, Object> map = userService.loginByAccount(body);
        return Result.ok(map);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody UserDto user) {
        if (userService.register(user)) {
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
    @Operation(summary = "退出")
    @GetMapping("/logout")
    public Result<String> logout() {
        StpUtil.logout();
        return Result.ok();
    }
}
