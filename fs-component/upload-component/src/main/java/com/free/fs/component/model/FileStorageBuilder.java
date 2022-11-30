package com.free.fs.component.model;

import com.free.fs.component.enums.StorageTypeEnum;
import com.free.fs.component.exception.StorageException;
import com.free.fs.component.properties.FileStorageProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:49
 */
@Slf4j
@Data
@Accessors(chain = true)
public class FileStorageBuilder {


    private FileStorageProperties props;

    public Map<String, AbstractStorage> build() {
        if (null == StorageTypeEnum.of(props.getPrimary())) {
            throw new StorageException("filestorage.primary's value only support: {aliyun, qiniu, qcloud, minio, local, custom}");
        }
        Map<String, AbstractStorage> storageMap = new HashMap<>();
        Class cls = null;
        String clsName = null;
        ClassLoader loader = FileStorageBuilder.class.getClassLoader();
        try {
            for (StorageTypeEnum type : StorageTypeEnum.values()) {
                if (StorageTypeEnum.CUSTOM == type) {
                    clsName = props.getExt().get(Consts.CUSTOM_STORAGE_CLASS);
                } else {
                    clsName = type.getLoaderClass();
                }
                //如果当前是默认处理器，且处理器的实现类为空
                if (type.name().equalsIgnoreCase(props.getPrimary())
                        && (StringUtils.isEmpty(clsName) || !ClassUtils.isPresent(type.getConditionClass(), loader))) {
                    throw new StorageException("filestorage primary service is null");
                }
                //如果处理器实现类存在，则加载
                if (!StringUtils.isEmpty(clsName) && ClassUtils.isPresent(type.getConditionClass(), loader)) {
                    cls = ClassUtils.forName(clsName, loader);
                    wrapStorage(type.name().toLowerCase(), cls, storageMap);
                }
            }
        } catch (StorageException e) {
            throw e;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("load storage error", e);
            }
            throw new StorageException("load storage error");
        }
        return storageMap;
    }

    private void wrapStorage(String type, Class cls, Map<String, AbstractStorage> storageMap)
            throws IllegalAccessException, InstantiationException {
        if (null != cls) {
            AbstractStorage storage = (AbstractStorage) cls.newInstance();
            storage.afterInitialization(props);
            storageMap.put(type, storage);
        }
    }
}
