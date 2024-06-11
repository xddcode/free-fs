package com.free.fs.common.storage;

/**
 * 文件上传对象工厂
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 14:23
 */
public interface IFileStorageProvider {
    IFileStorage getStorage();
}
