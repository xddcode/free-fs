package com.free.fs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.free.fs.common.template.OssTemplate;
import com.free.fs.model.FilePojo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * oss业务实现
 *
 * @author dinghao
 * @date 2021/4/6
 */
@Service
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "oss")
public class OssService extends AbstractIFileService {

    @Resource
    private OssTemplate ossTemplate;

    @Override
    protected FilePojo uploadFile(MultipartFile file) {
        return ossTemplate.upload(file);
    }

    @Override
    protected FilePojo uploadFileSharding(MultipartFile file, HttpSession session) {

        return ossTemplate.uploadSharding(file, session);
    }

    @Override
    protected void deleteFile(String objectPath) {
        if(StrUtil.isNotEmpty(objectPath)){
            ossTemplate.delete(objectPath);
        }
    }

    @Override
    public void download(String url, HttpServletResponse response) {
        ossTemplate.download(url, response);
    }
}
