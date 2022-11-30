package com.free.fs.component.model;

import lombok.Data;
import lombok.experimental.Accessors;

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

    private Integer code = Consts.CODE_SUCCESS;

    private Exception error;

    public boolean isSuccess() {
        return null != getCode() && getCode() == Consts.CODE_SUCCESS;
    }

}
