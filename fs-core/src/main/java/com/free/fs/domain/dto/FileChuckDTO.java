package com.free.fs.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件分片DTO
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/5 10:47
 */
@Data
@Schema(description = "文件分片DTO")
public class FileChuckDTO {

    /**
     * 文件分块编号
     */
    private Integer chunkNumber;

    /**
     * 文件分块大小
     */
    private Long chunkSize;

    /**
     * 当前分块大小
     */
    private Long currentChunkSize;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件md5
     */
    private String md5;

    /**
     * 文件总分块数
     */
    private String totalChunks;

    /**
     * 文件总大小
     */
    private Long totalSize;
}
