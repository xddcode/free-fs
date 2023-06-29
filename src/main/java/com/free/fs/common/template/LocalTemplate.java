package com.free.fs.common.template;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.properties.LocalProperties;
import com.free.fs.common.utils.FileUtil;
import com.free.fs.common.utils.StringUtil;
import com.free.fs.model.FilePojo;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "local")
public class LocalTemplate {

    private final Environment environment;
    private final LocalProperties localProperties;

    public LocalTemplate(Environment environment, LocalProperties localProperties) {
        this.environment = environment;
        this.localProperties = localProperties;
    }

    @SneakyThrows
    public FilePojo upload(MultipartFile file) {
        FilePojo pojo = FileUtil.buildFilePojo(file);
        File folder = new File(localProperties.getUploadDir());
        //目录不存在则创建
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        //保存文件
        file.transferTo(new File(folder + CommonConstant.DIR_SPLIT + pojo.getFileName()));
        // 返回上传文件的访问路径
        String url = getServerUrl() + localProperties.getUploadMapping() + CommonConstant.DIR_SPLIT + pojo.getFileName();
        pojo.setUrl(url);
        return pojo;
    }

    public void delete(String url) {
        String key = url.replaceAll(getServerUrl() + localProperties.getUploadMapping() + CommonConstant.DIR_SPLIT, "");
        File file = new File(localProperties.getUploadDir() + CommonConstant.DIR_SPLIT + key);
        if (file.exists()) {
            file.delete();
        }
    }

    public void download(String url, HttpServletResponse response) {
        FileUtil.downLoad(url, getServerUrl() + localProperties.getUploadMapping(), response);
    }

    public String getServerUrl() {
        if (StringUtil.isNotBlank(localProperties.getDomain())) {
            return localProperties.getDomain();
        }
        String serverUrl;
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = Integer.parseInt(environment.getProperty("server.port"));
            serverUrl = "http://" + ip + ":" + port;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return serverUrl;
    }
}
