package com.free.fs.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.free.fs.common.domain.Result;
import com.free.fs.domain.FileInfo;
import com.free.fs.domain.dto.FolderDTO;
import com.free.fs.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yann
 */
@Slf4j
@Tag(name = "文件目录管理")
@RestController
@RequestMapping("${fs.api.prefix}/folder")
@RequiredArgsConstructor
public class FolderController {

    private final FileService fileService;

    /**
     * 新增文件夹
     */
    @Operation(summary = "新增文件夹")
    @SaCheckPermission("folder:add")
    @PostMapping
    public Result<?> addFolder(@RequestBody FolderDTO folderDTO) {
        return Result.ok(fileService.addFolder(folderDTO));
    }
}
