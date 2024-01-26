package com.free.fs.common.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.free.fs.common.domain.vo.CaptchaVo;
import io.github.eternalstone.captcha.gp.base.Captcha;
import io.github.eternalstone.captcha.gp.base.CaptchaEnum;
import io.github.eternalstone.captcha.gp.base.TextEntry;
import io.github.eternalstone.captcha.gp.factory.CaptchaFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/27 15:18
 */
@Slf4j
@Data
public class CaptchaUtil {

    /**
     * 生成验证码
     */
    public static CaptchaVo captchaV2() {
        Captcha captcha = CaptchaFactory.getCaptcha(CaptchaEnum.GIF);
        //使用实例创建一个随机码对象TextEntry
        TextEntry text = captcha.createText();
        String uuid = UUID.fastUUID().toString();
        // 验证码凭证放入缓存
        CaptchaCacheUtils.put(uuid, text.getKey());
        // 返回
        return CaptchaVo.builder()
                .img(captcha.toBase64(text))
                .uuid(uuid)
                .build();
    }

    public static boolean verify(String uuid, String text) {
        if (CaptchaCacheUtils.containsKey(uuid)) {
            String storeText = CaptchaCacheUtils.get(uuid);
            return StrUtil.equalsIgnoreCase(storeText, text);
        }
        return false;
    }

}
