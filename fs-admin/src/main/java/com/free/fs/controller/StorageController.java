package com.free.fs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.domain.Result;
import com.free.fs.common.domain.vo.DictVo;
import com.free.fs.common.enums.StorageType;
import com.free.fs.domain.StoragePlatform;
import com.free.fs.service.StoragePlatformService;
import com.free.fs.service.StorageSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yann
 */
@Slf4j
@Tag(name = "存储平台管理")
@RestController
@RequestMapping("${fs.api.prefix}/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StoragePlatformService storagePlatformService;

    private final StorageSettingsService storageSettingsService;

    @Operation(summary = "获取平台支持的存储平台列表")
    @GetMapping("/platforms")
    public Result<List<StoragePlatform>> getList() {
        List<StoragePlatform> list = storagePlatformService.getList();
        return Result.ok(list);
    }

    @Operation(summary = "检查用户是否已经配置当前存储平台")
    @GetMapping("/setting/check/{identifier}")
    public Result<?> checkConfigByUser(@PathVariable("identifier") String identifier) {
        long userId = StpUtil.getLoginIdAsLong();
        if (storageSettingsService.checkConfigByUser(userId, identifier)) {
            return Result.ok();
        }
        return Result.error();
    }

    @Operation(summary = "启用禁用配置")
    @PutMapping("/platform/{identifier}")
    public Result<?> toggleStoragePlatform(@PathVariable("identifier") String identifier) {
        long userId = StpUtil.getLoginIdAsLong();
        if (storageSettingsService.toggleStoragePlatform(userId, identifier)) {
            return Result.ok();
        }
        return Result.error();
    }

    @Operation(summary = "支持的存储平台")
    @GetMapping("/types")
    public Result<List<DictVo>> getStorageTypes() {
        return Result.ok(StorageType.storageTypes());
    }
}
