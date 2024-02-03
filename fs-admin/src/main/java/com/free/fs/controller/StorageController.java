package com.free.fs.controller;

import com.free.fs.common.domain.Result;
import com.free.fs.common.domain.vo.DictVo;
import com.free.fs.common.enums.StorageType;
import com.free.fs.domain.StoragePlatform;
import com.free.fs.service.StoragePlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "获取平台支持的存储平台列表")
    @GetMapping("/platforms")
    public Result<List<StoragePlatform>> getList() {
        List<StoragePlatform> list = storagePlatformService.getList();
        return Result.ok(list);
    }

    @Operation(summary = "支持的存储平台")
    @GetMapping("/types")
    public Result<List<DictVo>> getStorageTypes() {
        return Result.ok(StorageType.storageTypes());
    }

    @Operation(summary = "启用禁用配置")
    @PutMapping("/setting")
    public Result<?> updateEnabled() {
        return Result.ok();
    }
}
