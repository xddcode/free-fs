package com.free.fs.controller;

import com.free.fs.common.domain.Result;
import com.free.fs.common.utils.CaptchaCacheUtils;
import com.free.fs.common.utils.CaptchaUtil;
import com.free.fs.common.utils.UUIDUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/24 11:03
 */
@Tag(name = "验证码管理")
@RestController
public class CaptchaController {

    @Operation(summary = "获取验证码")
    @GetMapping("/assets/captcha")
    public Result<?> getCaptcha() {
        //画图工具类
        CaptchaUtil imageCode = new CaptchaUtil();
        // 获取验证码对应的 base64  编码
        String base64 = CaptchaUtil.getBase64(imageCode.getImage());
        // 获取对应的 验证码 code
        String code = imageCode.getCode();
        // 生成 UUID
        String imgUUID = UUIDUtil.get32UUID();
        // 封装 获取的 验证码相关的数据 到 验证码对象中，并响应
        Map<String, Object> imgResult = new HashMap<>();
        imgResult.put("imgUUID", imgUUID);
        imgResult.put("img", base64);
        imgResult.put("code", code);
        CaptchaCacheUtils.put(imgUUID, code);
        System.out.println(code);
        System.out.println(imgUUID);
        return Result.ok(imgResult);
    }
}
