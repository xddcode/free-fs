package com.free.fs.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.free.fs.domain.StoragePlatform;
import com.free.fs.domain.StorageSettings;
import com.free.fs.domain.vo.PlatformStructureVO;
import com.free.fs.domain.vo.StoragePlatformVO;
import com.free.fs.mapper.StoragePlatformMapper;
import com.free.fs.mapper.StorageSettingsMapper;
import com.free.fs.service.StoragePlatformService;
import com.free.fs.service.StorageSettingsService;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.free.fs.domain.table.StoragePlatformTableDef.STORAGE_PLATFORM;
import static com.free.fs.domain.table.StorageSettingsTableDef.STORAGE_SETTINGS;

/**
 * @author yann
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoragePlatformServiceImpl extends ServiceImpl<StoragePlatformMapper, StoragePlatform> implements StoragePlatformService {

    private final StorageSettingsMapper storageSettingsMapper;

    @Override
    public List<StoragePlatform> getList() {
        return mapper.selectAll();
    }

    @Override
    public List<StoragePlatformVO> getConfigList() {
        List<StoragePlatform> list = getList();
        return list.stream().map(item -> {
            StoragePlatformVO vo = new StoragePlatformVO();
            vo.setName(item.getName());
            vo.setIdentifier(item.getIdentifier());
            // 查询配置
            QueryWrapper settingWrapper = QueryWrapper.create()
                    .where(STORAGE_SETTINGS.USER_ID.eq(StpUtil.getLoginIdAsLong()))
                    .and(STORAGE_SETTINGS.PLATFORM_IDENTIFIER.eq(item.getIdentifier()));
            StorageSettings storageSettings = storageSettingsMapper.selectOneByQuery(settingWrapper);
            if (null != storageSettings) {
                vo.setStatus(true);
                vo.setEnabled(storageSettings.getEnabled() == 1);
            } else {
                vo.setStatus(false);
                vo.setEnabled(false);
            }
            // #TODO 后面加表里?
            vo.setIcon("mostly-cloudy");
            vo.setConfigScheme(JSONArray.parseArray(item.getConfigScheme(), PlatformStructureVO.class));
            return vo;
        }).collect(Collectors.toList());
    }

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
