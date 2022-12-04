package com.free.fs.component.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.free.fs.component.exception.StorageException;
import com.free.fs.component.model.GetFileRequest;
import com.free.fs.component.model.PutFileRequest;
import com.free.fs.component.model.PutResponse;
import com.free.fs.component.model.RemoveFileRequest;
import com.free.fs.component.properties.FileStorageProperties;
import com.free.fs.component.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

@Slf4j
@Service
public class AliyunOssFileStorage implements StorageService {

    @Autowired
    private FileStorageProperties storageProperties;

    private OSS client;

    public OSS getClient() {
        if (client == null) {
            client = new OSSClientBuilder().build(
                    storageProperties.getAliyunOss().getEndPoint(),
                    storageProperties.getAliyunOss().getAccessKey(),
                    storageProperties.getAliyunOss().getAccessSecret()
            );
        }
        return client;
    }

    @Override
    public String type() {
        return null;
    }

    @Override
    public PutResponse put(PutFileRequest request) {
        return false;
    }

    @Override
    public boolean remove(RemoveFileRequest request) {
        OSS client = getClient();
        client.deleteObject(request.getBucket(), request.getPath());
        return true;
    }

    @Override
    public void download(GetFileRequest request, Consumer<InputStream> consumer) {
        OSSObject object = getClient().getObject(storageProperties.getAliyunOss().getBucket(), request.getPath());
        try (InputStream in = object.getObjectContent()) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new StorageException("文件下载失败, file path:" + request.getPath());
        }
    }

    @Override
    public boolean isExists(GetFileRequest request) {
        OSS myClient = getClient();
        try {
            return myClient.doesObjectExist(storageProperties.getAliyunOss().getBucket(), request.getPath());
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("文件查询有误, file path:{}", request.getPath(), e);
            }
            throw new StorageException("文件查询有误, file path:" + request.getPath());
        }
    }

    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }
}
