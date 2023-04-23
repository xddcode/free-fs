package com.free.fs.common.template;

import com.alibaba.fastjson2.JSON;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.properties.QiniuProperties;
import com.free.fs.common.utils.FileUtil;
import com.free.fs.model.FilePojo;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "qiniu")
public class QiniuTemplate {

    /**
     * 每次迭代的长度限制，最大1000，推荐值 1000
     */
    private static final int LIMIT = 1000;

    private final UploadManager uploadManager;
    private final BucketManager bucketManager;
    private final Auth auth;
    private final QiniuProperties qiniuProperties;
    /**
     * 定义七牛云上传的相关策略
     */
    private StringMap putPolicy;

    public QiniuTemplate(UploadManager uploadManager, BucketManager bucketManager, Auth auth, QiniuProperties qiniuProperties) {
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
        this.auth = auth;
        this.qiniuProperties = qiniuProperties;
    }

    /**
     * 查询七牛的资源列表
     *
     * @return
     */
    public List<FileInfo> list() {
        //文件名前缀
        String prefix = "";
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(qiniuProperties.getBucket(), prefix, LIMIT, delimiter);
        FileInfo[] items = new FileInfo[0];
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            items = fileListIterator.next();
        }
        return Arrays.asList(items);
    }

    /**
     * 上传文件-七牛
     * 返回字符串
     *
     * @param file
     * @return
     */
    @SneakyThrows
    public FilePojo upload(MultipartFile file) {
        FilePojo pojo = FileUtil.buildFilePojo(file);
        Response response = uploadManager.put(file.getBytes(), pojo.getFileName(), getUploadToken());
        //解析上传成功的结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        String url = qiniuProperties.getPath() + CommonConstant.DIR_SPLIT + putRet.key;
        pojo.setUrl(url);
        return pojo;
    }


    /**
     * 获取七牛上传凭证
     *
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(qiniuProperties.getBucket(), null, 3600, putPolicy);
    }

    /**
     * 删除对象
     *
     * @param url 对象路径
     */
    @SneakyThrows
    public void delete(String url) {
        String key = url.replaceAll(qiniuProperties.getPath() + CommonConstant.DIR_SPLIT, "");
        bucketManager.delete(qiniuProperties.getBucket(), key);
    }

    /**
     * 下载对象
     *
     * @param url 对象路径
     */
    public void download(String url, HttpServletResponse response) {
        FileUtil.downLoad(url, qiniuProperties.getPath(), response);
    }
}
