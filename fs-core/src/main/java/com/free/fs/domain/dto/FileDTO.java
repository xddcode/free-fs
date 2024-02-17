package com.free.fs.domain.dto;

import lombok.Data;

/**
 * @author Yann
 */
@Data
public class FileDTO {

    /**
     * #TODO Yann 暂时命名为此, 后面调试结束, 看统一如何修改
     * 目录id
     */
    private Long dirId;
    /**
     * 文件名
     */
    private String name;
}
