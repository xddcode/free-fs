package com.free.fs.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.free.fs.common.annotation.Preview;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.domain.Dtree;
import com.free.fs.common.domain.Result;
import com.free.fs.domain.FileInfo;
import com.free.fs.domain.dto.FileDTO;
import com.free.fs.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件管理
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Slf4j
@Tag(name = "文件管理")
@RestController
@RequestMapping("${fs.api.prefix}/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "获取文件列表")
    @GetMapping("/list")
    public Result<List<FileInfo>> getList(FileDTO fileDTO) {
        // #TODO Yann 修改查询参数
        List<FileInfo> list = fileService.getListByPage(fileDTO);
        return Result.ok(list);
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "获取树结构列表")
    @GetMapping("/tree/list")
    public String getTree(FileInfo info) {
        List<Dtree> list = fileService.getTreeList(info);
        return JSON.toJSONString(Result.ok(list));
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "获取树结构目录列表")
    @GetMapping("/dir/tree/list")
    public String getDirTree(FileInfo info) {
        // update Yann 增加一个根目录提供移动
        Dtree dtree = new Dtree();
        dtree.setId(-1L);
        dtree.setTitle("根目录");
        dtree.setIconClass(CommonConstant.DTREE_ICON_1);
        dtree.setIsDir(true);
        List<Dtree> list = fileService.getDirTreeList(info);
        dtree.setChildren(list);
        return JSON.toJSONString(Result.ok(ListUtil.of(dtree)));
    }

    /**
     * #TODO 待对接
     */
    @Deprecated
    @Operation(summary = "获取目录列表")
    @GetMapping("/dirs/{id}")
    public Result<Map<String, Object>> getDirs(@PathVariable(value = "id") Long id) {
        Map<String, Object> map = fileService.getDirs(id);
        return Result.ok(map);
    }

    @Operation(summary = "文件上传")
    @Preview()
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> upload(@RequestParam(value = "file") MultipartFile[] files,
                            @RequestParam(value = "dirIds") String dirIds) {

        return fileService.upload(files, dirIds);
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "文件分片上传")
    @PostMapping("/uploadSharding")
    public Result<?> uploadSharding(@RequestParam(value = "file") MultipartFile[] files, String dirIds, HttpServletRequest request) {
        return fileService.uploadSharding(files, dirIds, request.getSession());
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "获取进度数据")
    @GetMapping("/percent")
    public Integer percent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (session.getAttribute("uploadPercent") == null ? 0 : (Integer) session.getAttribute("uploadPercent"));
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "重置上传进度")
    @GetMapping("/percent/reset")
    public void resetPercent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("uploadPercent", 0);
        // ossUploadUtils.initPart();
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "文件下载")
    @SaCheckPermission("file:download")
    @GetMapping("/downLoad")
    public void downLoad(@RequestParam(value = "url") String url, HttpServletResponse response) {
        fileService.download(url, response);
    }

    @Operation(summary = "修改名称")
    @PutMapping("/rename")
    public Result<?> update(@RequestBody FileInfo info) {
        if (fileService.updateByName(info)) {
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");

    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "移动文件")
    @SaCheckPermission("file:move")
    @PutMapping("/move")
    public Result<?> move(@RequestParam(value = "ids") String ids, @RequestParam(value = "parentId") Long parentId) {
        if (fileService.move(ids, parentId)) {
            return Result.ok("移动成功");
        }
        return Result.error("移动失败");
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "根据url删除文件")
    @SaCheckPermission("file:delete")
    @DeleteMapping("/deleteFile")
    public Result<?> deleteFile(@RequestParam(value = "url") String url) {
        if (fileService.delete(url)) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * #TODO 待对接
     */
    @Operation(summary = "根据id删除文件")
    @SaCheckPermission("file:delete")
    @DeleteMapping("/deleteByIds")
    public Result<?> deleteByIds(@RequestParam(value = "id") Long id) {
        if (fileService.deleteByIds(id)) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    @DeleteMapping("/{ids}")
    public Result<?> delete(@PathVariable("ids") Long[] ids) {

        return Result.ok(fileService.deleteByIds(ids));
    }

    /**
     * #TODO 待对接
     */
    @Deprecated
    @Operation(summary = "新增文件夹")
    @SaCheckPermission("dir:add")
    @PostMapping("/folder")
    public Result<?> addFolder(FileInfo info) {
        if (fileService.addFolder(info)) {
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }
}
