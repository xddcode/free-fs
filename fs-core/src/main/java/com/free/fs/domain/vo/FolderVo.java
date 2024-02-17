package com.free.fs.domain.vo;

import lombok.Data;

/**
 * 文件夹Vo
 *
 * @author Yann
 */
@Data
public class FolderVo {

    /**
     * 主键
     */
    private Long id;
    /**
     * 文件夹名称
     */
    private String name;
    /**
     * 级别
     */
    private Integer level;
}
