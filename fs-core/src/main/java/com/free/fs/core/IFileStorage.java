package com.free.fs.core;

import com.free.fs.common.domain.FileBo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 9:43
 */
public interface IFileStorage {


    /**
     *
     * oss文件上传流程:
     * 1. 获取oss配置
     * 2. 创建oss client
     * 3. 处理文件
     * 4. 执行上传
     * 5. 返回存储后的文件对象
     * 6. 关闭上传流 [有的话]
     *
     */


    /**
     * 从文件路径中获取存储桶名称
     *
     * @param url 文件路径
     * @return 存储桶名称
     */
    String getBucketByUrl(String url);

    /**
     * 从文件路径中获取对象名称
     *
     * @param url 文件路径
     * @return 对象名称
     */
    String getObjectNameByUrl(String url);

    /**
     * 判断存储桶是否在存在
     *
     * @param bucket 桶名称
     * @return true 存在 false 不存在
     */
    boolean bucketExists(String bucket);

    /**
     * 创建存储桶
     *
     * @param bucket 桶名称
     */
    void makeBucket(String bucket);

    /**
     * 上传文件
     *
     * @param file
     * @return 文件基本信息
     */
    FileBo upload(MultipartFile file);

    /**
     * 删除文件
     *
     * @param url 文件路径
     */
    void delete(String url);

    /**
     * 下载文件
     *
     * @param url      文件路径
     * @param response
     */
    void download(String url, HttpServletResponse response);


    /**
     * 获取文件访问地址
     *
     * @param objectName 对象名称
     * @return
     */
    String getUrl(String objectName);

    /**
     * 获取私有bucket文件访问地址
     *
     * @param url 文件路径
     * @return
     */
    default String getPolicyUrl(String url) {
        return url;
    }
}
