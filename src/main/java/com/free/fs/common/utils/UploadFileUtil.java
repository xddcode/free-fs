package com.free.fs.common.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.model.FilePojo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 文件操作工具类
 *
 * @author dinghao
 * @date 2021/3/16
 */
@Component
public class UploadFileUtil {

    @Autowired(required = false)
    private  FsServerProperties fileProperties;

    @Autowired(required = false)
    private  UploadManager uploadManager;

    @Autowired(required = false)
    private  BucketManager bucketManager;

    @Autowired(required = false)
    private  Auth auth;

    @Autowired(required = false)
    private OSSClient ossClient;

    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;

    /**
     * 查询资源列表
     *
     * @return
     */
    public List<?> list() {

        return fileProperties.getType().equals(CommonConstant.FILE_TYPE_QINIU) ? this.listQiniu() : this.listOss();
    }

    /**
     * 查询oss的资源列表
     *
     * @return
     */
    public List<OSSObjectSummary> listOss(){
        // 设置最大个数。
        final int maxKeys = 1000;
        // 列举文件。
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(fileProperties.getOss().getBucket()).withMaxKeys(maxKeys));
        return objectListing.getObjectSummaries();
    }

    /**
     * 查询七牛的资源列表
     *
     * @return
     */
    public List<FileInfo> listQiniu() {
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        final int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(fileProperties.getQiniu().getBucket(), prefix, limit, delimiter);
        FileInfo[] items = new FileInfo[0];
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            items = fileListIterator.next();
        }
        return Arrays.asList(items);
    }

    /**
     * 上传文件
     * 返回字符串
     *
     * @param file
     * @return
     */
    public FilePojo upload(MultipartFile file) throws IOException {
        return fileProperties.getType().equals(CommonConstant.FILE_TYPE_QINIU) ? this.uploadQiniu(file) : this.ossUpload(file);
    }

    /**
     * 上传文件-oss
     * 返回字符串
     *
     * @param file
     * @return
     */
    private FilePojo ossUpload(MultipartFile file) throws IOException {
        InputStream inputStream = null;
        FilePojo pojo = this.buildFilePojo(file);
        inputStream = file.getInputStream();
        ossClient.putObject(fileProperties.getOss().getBucket(), pojo.getFileName(), inputStream);
        String url = fileProperties.getOss().getPath() + "/" + pojo.getFileName();
        pojo.setUrl(url);
        return pojo;
    }

    /**
     * 上传文件-七牛
     * 返回字符串
     *
     * @param file
     * @return
     */
    private FilePojo uploadQiniu(MultipartFile file) throws IOException {
        FilePojo pojo = this.buildFilePojo(file);
        Response response = uploadManager.put(file.getBytes(), pojo.getFileName(), getUploadToken());
        //解析上传成功的结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String url = fileProperties.getQiniu().getPath() + "/" + putRet.key;
        pojo.setUrl(url);
        return pojo;
    }

    /**
     * 获取七牛上传凭证
     *
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(fileProperties.getQiniu().getBucket(), null, 3600, putPolicy);
    }

    private FilePojo buildFilePojo(MultipartFile file) {
        //判断文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        //判断文件后缀名是否合法
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            throw new BusinessException("文件名称不合法");
        }
        //获取文件大小
        Long size = file.getSize();
        //文件名
        String orgName = file.getOriginalFilename();
        String name = orgName.substring(0, orgName.lastIndexOf("."));
        //文件后缀名
        String fileExt = orgName.substring(dotPos + 1).toLowerCase();
        // 判断是否是合法的文件后缀
        if (!FileUtil.isFileAllowed(fileExt)) {
            throw new BusinessException("文类类型不符合要求");
        }
        String type = "";
        if (FileUtil.isCode(fileExt)) {
            type = "code";
        } else {
            type = fileExt;
        }
        //生成新的文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        FilePojo pojo = new FilePojo();
        pojo.setSuffix(fileExt);
        pojo.setSize(size);
        pojo.setFileName(fileName);
        pojo.setName(name);
        pojo.setIsImg(FileUtil.isImg(fileExt));
        pojo.setIsDir(Boolean.FALSE);
        pojo.setPutTime(new Date());
        pojo.setType(type);
        return pojo;
    }

    /**
     * 根据key删除文件
     * 返回字符串
     *
     * @param url
     * @return
     */
    public void delete(String url) throws QiniuException {
        if (fileProperties.getType().equals(CommonConstant.FILE_TYPE_QINIU)) {
            this.deleteQiniu(url);
        } else {
            this.deleteOss(url);
        }
    }

    /**
     * 根据key删除文件-qiniu
     * 返回字符串
     *
     * @param url
     * @return
     */
    public void deleteQiniu(String url) throws QiniuException {
        String key = url.replaceAll(fileProperties.getQiniu().getPath() + CommonConstant.DIR_SPLIT, "");
        bucketManager.delete(fileProperties.getQiniu().getBucket(), key);
    }

    /**
     * 根据key删除文件-oss
     * 返回字符串
     *
     * @param url
     * @return
     */
    public void deleteOss(String url) {
        String key = url.replaceAll(fileProperties.getOss().getPath() + CommonConstant.DIR_SPLIT, "");
        ossClient.deleteObject(fileProperties.getOss().getBucket(), key);
    }
}
