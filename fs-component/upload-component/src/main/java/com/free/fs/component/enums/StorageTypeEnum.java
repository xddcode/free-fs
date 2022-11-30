package com.free.fs.component.enums;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:43
 */
public enum StorageTypeEnum {

    //阿里oss
    ALIYUN(AliyunStorageImpl.class.getName(), "com.aliyun.oss.OSS"),

    //七牛oss
    QINIU(QiniuStorageImpl.class.getName(), "com.qiniu.storage.UploadManager"),

    //本地文件存储
    LOCAL(LocalStorageImpl.class.getName(), LocalStorageImpl.class.getName());

    private String loaderClass;

    private String conditionClass;

    StorageTypeEnum(String loaderClass, String conditionClass) {
        this.loaderClass = loaderClass;
        this.conditionClass = conditionClass;
    }

    public String getLoaderClass() {
        return loaderClass;
    }

    public String getConditionClass() {
        return conditionClass;
    }

    public static StorageTypeEnum of(String name) {
        if (null != name) {
            for (StorageTypeEnum type : values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }
        return null;
    }
}
