package com.free.fs.component.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 删除文件，请求参数
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:48
 */
@Data
@Accessors(chain = true)
public class RemoveFileRequest {

    /**
     * 桶
     */
    private String bucket;

    /**
     * 存储路径
     */
    private String path;
}
