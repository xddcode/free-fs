package com.free.fs.controller;

import com.free.fs.common.domain.Result;
import com.free.fs.domain.vo.UserVO;
import com.free.fs.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yann
 */
@Tag(name = "用户管理")
@Validated
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Result<UserVO> getUserInfo() {
        return Result.ok(userService.getUserInfo());
    }
}
