package com.free.fs.common.enums;

import com.alibaba.fastjson2.JSONObject;
import com.free.fs.common.domain.vo.DictVo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/1/25 10:03
 */
public enum StorageType {

    /**
     * 本地
     */
    Local("本地存储"),
    /**
     * 阿里云OSS
     */
    AliyunOSS("阿里云"),
    /**
     * 七牛云
     */
    Qiniu("七牛云"),
    /**
     * MinIO
     */
    Minio("MinIO");

    private final String name;

    StorageType(String name) {
        this.name = name;
    }

    public static StorageType getStorageType(String value) {
        try {
            return StorageType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static List<DictVo> storageTypes() {
        return Arrays.stream(StorageType.values()).sequential().map(item -> {

            DictVo vo = new DictVo();
            vo.setKey(item.toString());
            vo.setValue(item.name);
            return vo;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        System.out.println(JSONObject.toJSONString(StorageType.storageTypes()));
    }
}
