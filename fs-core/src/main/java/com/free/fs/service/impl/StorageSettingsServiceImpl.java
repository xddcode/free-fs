package com.free.fs.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.domain.StorageSettings;
import com.free.fs.domain.dto.StorageConfigDTO;
import com.free.fs.domain.vo.StorageSettingsVO;
import com.free.fs.mapper.StorageSettingsMapper;
import com.free.fs.service.StoragePlatformService;
import com.free.fs.service.StorageSettingsService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.free.fs.domain.table.StorageSettingsTableDef.STORAGE_SETTINGS;

/**
 * @author yann
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageSettingsServiceImpl extends ServiceImpl<StorageSettingsMapper, StorageSettings> implements StorageSettingsService {

    private final StoragePlatformService storagePlatformService;

    @Override
    public StorageSettings getByUserEnabled(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(STORAGE_SETTINGS.USER_ID.eq(userId))
                .and(STORAGE_SETTINGS.ENABLED.eq(CommonConstant.ENABLE));
        return this.getOne(queryWrapper);
    }

//    @Override
//    public StorageSettings getByUser(Long userId, Integer enabled) {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.where(STORAGE_SETTINGS.USER_ID.eq(userId))
//                .and(STORAGE_SETTINGS.ENABLED.eq(enabled).when(enabled != null));
//        return this.getOne(queryWrapper);
//    }

    @Override
    public StorageSettings getByUserPlatformIdentifier(Long userId, String platformIdentifier) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(STORAGE_SETTINGS.USER_ID.eq(userId))
                .and(STORAGE_SETTINGS.PLATFORM_IDENTIFIER.eq(platformIdentifier));
        return this.getOne(queryWrapper);
    }

    @Override
    public StorageSettingsVO getByVoUserPlatformIdentifier(Long userId, String platformIdentifier) {
        StorageSettings settings = getByUserPlatformIdentifier(userId, platformIdentifier);
        StorageSettingsVO vo = BeanUtil.copyProperties(settings, StorageSettingsVO.class, "configData");
        vo.setConfigData(JSONObject.parseObject(settings.getConfigData()));
        return vo;
    }

    @Override
    public List<StorageSettings> getListByUser(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(STORAGE_SETTINGS.USER_ID.eq(userId));
        return this.list(queryWrapper);
    }

    @Override
    public boolean checkConfigByUser(Long userId, String platformIdentifier) {
        StorageSettings storageSettings = this.getByUserPlatformIdentifier(userId, platformIdentifier);
        return storageSettings != null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean toggleStoragePlatform(Long userId, String platformIdentifier) {
        //判断用户是否已经配置此平台
        if (!this.checkConfigByUser(userId, platformIdentifier)) {
            throw new BusinessException("您尚未配置此存储平台，请配置后重试");
        }
        //判断配置参数是否正确
        StorageSettings storageSettings = this.getByUserPlatformIdentifier(userId, platformIdentifier);
        String config = storageSettings.getConfigData();
        if (!storagePlatformService.checkConfigScheme(platformIdentifier, config)) {
            throw new BusinessException("您此存储平台配置有误，请检查后重试");
        }
        //查询当前的启用平台
        StorageSettings storageSettingsEnable = this.getByUserEnabled(userId);
        //判断当前用户此平台是否已经启用
        if (storageSettingsEnable.getEnabled().equals(storageSettings.getEnabled())) {
            throw new BusinessException("您当前平台已启用，无需操作");
        }
        //禁用之前的已经启用的存储平台
        storageSettingsEnable.setEnabled(CommonConstant.DISABLE);
        this.updateById(storageSettingsEnable);
        //启用此存储平台
        storageSettings.setEnabled(CommonConstant.ENABLE);
        return this.updateById(storageSettings);
    }

    @Override
    public boolean saveOrUpdateConfig(StorageConfigDTO storageConfigDTO) {
        long userId = StpUtil.getLoginIdAsLong();
        StorageSettings storageSettings = new StorageSettings();
        storageSettings.setPlatformIdentifier(storageConfigDTO.getPlatformIdentifier());
        storageSettings.setConfigData(storageConfigDTO.getConfigData());
        storageSettings.setUserId(userId);
        if (storageConfigDTO.getId() == null) {
            storageSettings.setEnabled(CommonConstant.DISABLE);
            storageSettings.setCreateTime(new Date());
        } else {
            storageSettings.setId(storageConfigDTO.getId());
            storageSettings.setUpdateTime(new Date());
        }
        return this.saveOrUpdate(storageSettings);
    }
}
