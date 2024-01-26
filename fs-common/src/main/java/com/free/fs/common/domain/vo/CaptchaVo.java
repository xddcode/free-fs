package com.free.fs.common.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Yann
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
