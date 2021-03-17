package com.free.fs.common.utils;

import com.alibaba.fastjson.JSON;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.properties.QiniuProperties;
import com.free.fs.model.FilePojo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UploadFileUtil {

    private final QiniuProperties qiniuProperties;

    private final UploadManager uploadManager;

    private final BucketManager bucketManager;

    private final Auth auth;

    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;

    /**
     * 查询资源列表
     *
     * @return
     */
    public List<FileInfo> list() {
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(qiniuProperties.getBucket(), prefix, limit, delimiter);
        FileInfo[] items = new FileInfo[0];
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            items = fileListIterator.next();
        }
        return Arrays.asList(items);
    }

    /**
     * 在七牛云上保存文件
     * 返回字符串
     *
     * @param file
     * @return
     */
    public FilePojo upload(MultipartFile file) throws IOException {
        //判断文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        //判断文件后缀名是否合法
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            throw new BusinessException("文件名称不合法");
        }
        FilePojo filePojo = new FilePojo();
        filePojo.setSize(file.getSize());
        //文件名
        String orgName = file.getOriginalFilename();
        filePojo.setName(orgName.substring(0, orgName.lastIndexOf(".")));
        //文件后缀名
        String fileExt = orgName.substring(dotPos + 1).toLowerCase();
        filePojo.setSuffix(fileExt);
        if (FileUtil.isCode(fileExt)) {
            filePojo.setType("code");
        } else {
            filePojo.setType(fileExt);
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        filePojo.setFileName(fileName);
        // 判断是否是合法的文件后缀
        if (!FileUtil.isFileAllowed(fileExt)) {
            throw new BusinessException("文类类型不符合要求");
        }
        filePojo.setIsImg(FileUtil.isImg(fileExt));
        Response response = uploadManager.put(file.getBytes(), fileName, getUploadToken());
        //解析上传成功的结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String url = qiniuProperties.getPath() + "/" + putRet.key;
        filePojo.setUrl(url);
        filePojo.setPutTime(new Date());
        filePojo.setIsDir(Boolean.FALSE);
        return filePojo;
    }

    /**
     * 根据key删除文件
     * 返回字符串
     *
     * @param key
     * @return
     */
    public Response delete(String key) throws QiniuException {
        Response response = null;
        if (StringUtils.isNotEmpty(key)) {
            response = bucketManager.delete(qiniuProperties.getBucket(), key);
        }
        return response;
    }

    /**
     * 资源重命名
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public Response rename(String oldKey, String newKey) throws QiniuException {

        return bucketManager.rename(qiniuProperties.getBucket(), oldKey, newKey);
    }

    /**
     * 获取文件后缀名
     *
     * @return
     */
    public String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取上传凭证
     *
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(qiniuProperties.getBucket(), null, 3600, putPolicy);
    }

}
