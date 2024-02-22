package com.free.fs.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件分片信息
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/5 10:58
 */
@Data
@NoArgsConstructor
@Table(value = "f_file_chunk_info")
public class FileChunkInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 文件分块编号
     */
    private Integer chunkNumber;

    /**
     * 文件分块大小
     */
    private Long chunkSize;

    /**
     * 文件分块路径
     */
    private String url;

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
