package com.free.fs.component.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.Consts;

/**
 * 上传文件，返回结果
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:47
 */
@Data
@Accessors(chain = true)
public class PutResponse {

    private String path;

    private String url;
}
