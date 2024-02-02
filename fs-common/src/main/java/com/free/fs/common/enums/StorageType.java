package com.free.fs.common.enums;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:03
 */
public enum StorageType {

    /**
     * 本地
     */
    Local("Local"),
    /**
     * 阿里云OSS
     */
    AliyunOSS("AliyunOSS"),
    /**
     * 七牛云
     */
    Qiniu("Qiniu"),
    /**
     * MinIO
     */
    Minio("Minio");

    private final String value;

    StorageType(String value) {
        this.value = value;
    }

    public static StorageType getStorageType(String value) {
        for (StorageType storageType : StorageType.values()) {
            if (storageType.value.equals(value)) {
                return storageType;
            }
        }
        return null;
    }
}
