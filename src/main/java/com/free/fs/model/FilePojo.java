package com.free.fs.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 文件资源表实体
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Data
@TableName("file_info")
@EqualsAndHashCode(callSuper = true)
public class FilePojo extends Model<FilePojo> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
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
    private String source;

    /**
     * 重命名的名称值
     */
    @TableField(exist = false)
    private String rename;

    /**
     * 目录id拼接
     */
    @TableField(exist = false)
    private String dirIds;
}
