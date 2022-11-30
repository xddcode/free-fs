package com.free.fs.component.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 文件流
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:49
 */
@Data
@Accessors(chain = true)
public class FileStream {

    private FileMeta meta;

    private byte[] payload;

    private int size = 0;

    private FileStream() {
    }

    public FileStream(FileMeta meta, File file) throws IOException {
        this(meta, Files.readAllBytes(file.toPath()));
    }

    public FileStream(FileMeta meta, byte[] payload) {
        this.meta = meta;
        this.payload = payload;
        if (null != payload) {
            this.size = payload.length;
        }
    }

    public static FileStream empty() {
        return new FileStream();
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

}
