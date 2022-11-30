package com.free.fs.component.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * 文件处理工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:45
 */
@Slf4j
public class FileUtil {

    /**
     * 通过文件名获取后缀
     *
     * @param fileName
     * @return
     */
    public static String getExt(String fileName) {
        if (null == fileName) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        return index > 0 ? fileName.substring(index + 1) : null;
    }

    /**
     * 关闭流
     *
     * @param s
     */
    public static boolean close(Closeable s) {
        if (null == s) {
            return true;
        }
        try {
            s.close();
            return true;
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("close stream error", e);
            }
        }
        return false;
    }
}
