package com.free.fs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.template.QiniuTemplate;
import com.free.fs.model.FilePojo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 七牛业务实现
 *
 * @author dinghao
 * @date 2021/4/6
 */
@Service
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "qiniu")
public class QiniuService extends AbstractIFileService {

    @Resource
    private QiniuTemplate qiniuTemplate;

    @Override
    protected FilePojo uploadFile(MultipartFile file) {
        return qiniuTemplate.upload(file);
    }

    @Override
    protected FilePojo uploadFileSharding(MultipartFile file, HttpSession session) {
        throw new BusinessException("分片上传目前只支持OSS，七牛还未实现！");
    }

    @Override
    protected void deleteFile(String objectPath) {
        if(StrUtil.isNotEmpty(objectPath)){
            qiniuTemplate.delete(objectPath);
        }
    }

    @Override
    public void download(String url, HttpServletResponse response) {
        qiniuTemplate.download(url, response);
    }
}
