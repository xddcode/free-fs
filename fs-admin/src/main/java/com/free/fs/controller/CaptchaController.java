package com.free.fs.controller;

import com.free.fs.common.utils.CaptchaUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * 验证码
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/24 11:03
 */
@Controller
public class CaptchaController {

    /**
     * 生成验证码 算术类型
     */
    @RequestMapping("/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaUtil.captcha(request, response);
    }
}
