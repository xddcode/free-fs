package com.free.fs.component.service.impl;

import com.free.fs.component.exception.StorageException;
import com.free.fs.component.model.*;
import com.free.fs.component.properties.FileStorageProperties;
import com.free.fs.component.service.StorageService;
import com.free.fs.component.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.function.Consumer;

@Slf4j
@Service
public class LocalFileStorage implements StorageService {

    @Autowired
    private FileStorageProperties storageProperties;

    @Override
    public String type() {
        return null;
    }

    @Override
    public PutResponse put(PutFileRequest request) {
        PutResponse uploader = new PutResponse();
        FileOutputStream write = null;
        String path = getPath(config.getPrefix(), request.getFileName());

        return false;
    }

    @Override
    public boolean remove(RemoveFileRequest request) {
        String path = storageProperties.getLocal().getDir() + "/" + request.getPath();
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        return true;
    }

    @Override
    public void download(GetFileRequest request, Consumer<InputStream> consumer) {
        String fullPath = storageProperties.getLocal().getDir() + "/" + request.getPath();
        File file = new File(fullPath);
        try (InputStream in = Files.newInputStream(file.toPath())) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new StorageException("文件下载失败, file path:" + request.getPath());
        }
    }

    @Override
    public boolean isExists(GetFileRequest request) {
        String path = storageProperties.getLocal().getDir() + "/" + request.getPath();
        File file = new File(path);
        return file.exists();
    }

    @Override
    public void close() {

    }
}
