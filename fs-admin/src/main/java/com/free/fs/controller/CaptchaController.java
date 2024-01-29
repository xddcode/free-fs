package com.free.fs.controller;

import com.free.fs.common.domain.Result;
import com.free.fs.common.domain.vo.CaptchaVo;
import com.free.fs.common.utils.CaptchaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/24 11:03
 */
@Tag(name = "验证码管理")
@RestController
@RequestMapping("${fs.api.prefix}/captcha")
public class CaptchaController {

    @Operation(summary = "获取验证码")
    @GetMapping
    public Result<CaptchaVo> getCaptcha() {
        return Result.ok(CaptchaUtil.captchaV2());
    }
}
