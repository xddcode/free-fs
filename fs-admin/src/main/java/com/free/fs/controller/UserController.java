package com.free.fs.controller;

import com.free.fs.common.domain.Result;
import com.free.fs.core.domain.dto.UpdatePasswordDTO;
import com.free.fs.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/18 8:51
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    @PutMapping("/password")
    @ResponseBody
    public Result<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        if (userService.updatePassword(dto)) {
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }
}
