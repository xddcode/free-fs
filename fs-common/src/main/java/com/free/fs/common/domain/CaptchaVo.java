package com.free.fs.common.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:27
 */
@Data
@Builder
public class CaptchaVo {
    /**
     * 验证码base64
     */
    private String img;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 是否开启
     */
    private boolean enableCaptcha = true;
}
