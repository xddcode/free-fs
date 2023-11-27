package com.free.fs.common.utils;

import io.github.eternalstone.captcha.gp.base.Captcha;
import io.github.eternalstone.captcha.gp.base.CaptchaEnum;
import io.github.eternalstone.captcha.gp.base.TextEntry;
import io.github.eternalstone.captcha.gp.factory.CaptchaFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 验证码工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/27 15:18
 */
public class CaptchaUtil {
    private static final String CAPTCHA_KEY = "captcha";

    public static void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //通过验证码类型枚举获取一个验证码实例
        Captcha captcha = CaptchaFactory.getCaptcha(CaptchaEnum.SPEC);
        //使用实例创建一个随机码对象TextEntry
        TextEntry text = captcha.createText();
        //自定义验证码存储逻辑，单机一般存储session, 分布式下存储redis
        request.getSession().setAttribute(CAPTCHA_KEY, text.getKey());
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //将随机码通过实体写出成图片
        captcha.out(response.getOutputStream(), text);
    }

    public static boolean verify(HttpServletRequest request, String key) {
        String captcha = (String) request.getSession().getAttribute(CAPTCHA_KEY);
        return captcha != null && captcha.equalsIgnoreCase(key);
    }
}
