package com.free.fs.core.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息实体类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:03
 */
@Data
@Table("file_info")
public class FileInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 文件路径
     */
    private String url;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件uuid后的新名称
     */
    private String fileName;

    /**
     * 文件后缀名
     */
    private String suffix;

    /**
     * 是否图片 0否 1是
     */
    private Boolean isImg;

    /**
     * 是否文件夹 0否 1是
     */
    private Boolean isDir;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件展示类型
     */
    private String type;

    /**
     * 上传时间
     */
    private Date putTime;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 来源
     */
    @Column(tenantId = true)
    private String source;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 重命名的名称值
     */
    @Column(ignore = true)
    private String rename;

    /**
     * 目录id拼接
     */
    @Column(ignore = true)
    private String dirIds;
}
