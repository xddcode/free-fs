package com.free.fs.service.impl;

import cn.hutool.core.util.StrUtil;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.template.LocalTemplate;
import com.free.fs.model.FilePojo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 本地上传业务实现
 *
 * @author dinghao
 * @date 2021/4/6
 */
@Service
@ConditionalOnProperty(prefix = "fs.files-server", name = "type", havingValue = "local")
public class LocalService extends AbstractIFileService {

    @Resource
    private LocalTemplate localTemplate;

    @Override
    protected FilePojo uploadFile(MultipartFile file) {
        return localTemplate.upload(file);
    }


    @Override
    protected FilePojo uploadFileSharding(MultipartFile file, HttpSession session) {
        throw new BusinessException("分片上传目前只支持OSS，本地模式还未实现！");
    }

    @Override
    protected void deleteFile(String url) {
        if(StrUtil.isNotEmpty(url)){
            localTemplate.delete(url);
        }
    }

    @Override
    public void download(String url, HttpServletResponse response) {
        localTemplate.download(url, response);
    }
}
