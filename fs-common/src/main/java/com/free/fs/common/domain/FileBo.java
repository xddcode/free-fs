package com.free.fs.common.domain;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.utils.FileUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:16
 */
@Data
public class FileBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
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
     * 用户id
     */
    private Long userId;

    /**
     * 重命名的名称值
     */
    private String rename;

    /**
     * 目录id拼接
     */
    private String dirIds;

    public static FileBo build(MultipartFile file) {

        //判断文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        //判断文件后缀名是否合法
        int dotPos = file.getOriginalFilename().lastIndexOf(CommonConstant.SUFFIX_SPLIT);
        if (dotPos < 0) {
            throw new BusinessException("文件名称不合法");
        }
        //获取文件大小
        Long size = file.getSize();
        //文件名
        String orgName = file.getOriginalFilename();
        String name = FileUtil.getFileName(orgName);
        //文件后缀名
        String fileExt = FileUtil.getFileSuffix(orgName);
        // 判断是否是合法的文件后缀
        if (!FileUtil.isFileAllowed(fileExt)) {
            throw new BusinessException("文件类型不符合要求");
        }
        String type;
        if (FileUtil.isCode(fileExt)) {
            type = "code";
        } else {
            type = fileExt;
        }
        //生成新的文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        FileBo fileBo = new FileBo();
        fileBo.setSuffix(fileExt);
        fileBo.setSize(size);
        fileBo.setFileName(fileName);
        fileBo.setName(name);
        fileBo.setIsImg(FileUtil.isImg(fileExt));
        fileBo.setIsDir(Boolean.FALSE);
        fileBo.setPutTime(new Date());
        fileBo.setType(type);
        return fileBo;
    }
}
