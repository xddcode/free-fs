package com.free.fs.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * oss配置资源类
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Getter
@Setter
public class OssProperties {

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String bucket;

    private String path;
}
