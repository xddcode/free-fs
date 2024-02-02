package com.free.fs.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.free.fs.domain.StoragePlatform;
import com.free.fs.mapper.StoragePlatformMapper;
import com.free.fs.service.StoragePlatformService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.free.fs.domain.table.StoragePlatformTableDef.STORAGE_PLATFORM;

/**
 * @author yann
 */
@Slf4j
@Service
public class StoragePlatformServiceImpl extends ServiceImpl<StoragePlatformMapper, StoragePlatform> implements StoragePlatformService {

    @Override
    public StoragePlatform getStoragePlatformByIdentifier(String identifier) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(STORAGE_PLATFORM.IDENTIFIER.eq(identifier));
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean checkConfigScheme(String identifier, String config) {
        JSONObject configObj = JSONObject.parseObject(config);
        StoragePlatform storagePlatform = this.getStoragePlatformByIdentifier(identifier);
        String configScheme = storagePlatform.getConfigScheme();
        JSONArray schemes = JSONArray.parse(configScheme);
        for (Object scheme : schemes) {
            JSONObject schemeObj = (JSONObject) scheme;
            for (String key : schemeObj.keySet()) {
                if (key.equals("identifier")) {
                    String value = schemeObj.getString(key);
                    if (!configObj.containsKey(value)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
