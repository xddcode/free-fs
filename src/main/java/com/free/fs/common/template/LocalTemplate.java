package com.free.fs.common.template;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.properties.FsServerProperties;
import com.free.fs.common.utils.FileUtil;
import com.free.fs.model.FilePojo;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "local")
public class LocalTemplate {

    @Resource
    private FsServerProperties fileProperties;

    @SneakyThrows
    public FilePojo upload(MultipartFile file) {
        FilePojo pojo = FileUtil.buildFilePojo(file);
        File folder = new File(fileProperties.getLocal().getUploadPath());
        //目录不存在则创建
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        //保存文件
        file.transferTo(new File(folder + CommonConstant.DIR_SPLIT + pojo.getFileName()));
        // 返回上传文件的访问路径
        String url = fileProperties.getLocal().getNgxinxUrl() + CommonConstant.DIR_SPLIT + pojo.getFileName();
        pojo.setUrl(url);
        return pojo;
    }

    public void delete(String url) {
        String key = url.replaceAll(fileProperties.getLocal().getNgxinxUrl() + CommonConstant.DIR_SPLIT, "");
        File file = new File(fileProperties.getLocal().getUploadPath() + CommonConstant.DIR_SPLIT + key);
        if (file.exists()) {
            file.delete();
        }
    }

    public void download(String url, HttpServletResponse response) {
        FileUtil.downLoad(url, fileProperties.getLocal().getNgxinxUrl(), response);
    }
}
