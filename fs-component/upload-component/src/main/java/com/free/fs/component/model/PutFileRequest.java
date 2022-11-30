package com.free.fs.component.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上传文件，请求参数
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:48
 */
@Data
@Accessors(chain = true)
public class PutFileRequest {

    /**
     * 桶
     */
    private String bucket;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件content-type
     */
    private String contentType;

    /**
     * 文件流
     */
    private byte[] payload;

    /**
     * 文件元数据
     */
    private FileMeta meta;
}
