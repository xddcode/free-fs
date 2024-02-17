package com.free.fs.domain.dto;

import lombok.Data;

/**
 * 文件夹数据传输对象
 *
 * @author Yann
 */
@Data
public class FolderDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 父文件夹ID
     */
    private Long pid;
    /**
     * 目录名
     */
    private String name;
}
