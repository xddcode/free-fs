package com.free.fs.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson2.JSON;
import com.free.fs.common.annotation.Preview;
import com.free.fs.common.domain.R;
import com.free.fs.common.domain.Dtree;
import com.free.fs.model.FilePojo;
import com.free.fs.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 获取文件列表
     *
     * @param pojo
     * @return
     */
    @GetMapping({"", "/list"})
    public R getList(FilePojo pojo) {
        List<FilePojo> list = fileService.getList(pojo);
        return R.succeed(list, "查询成功");
    }

    /**
     * 获取树结构列表
     *
     * @param pojo
     * @return
     */
    @GetMapping("/getTree")
    public String getTree(FilePojo pojo) {
        List<Dtree> list = fileService.getTreeList(pojo);
        return JSON.toJSONString(R.succeed(list, "查询成功"));
    }

    /**
     * 获取树结构目录列表
     *
     * @param pojo
     * @return
     */
    @GetMapping("/getDirTree")
    public String getDirTree(FilePojo pojo) {
        List<Dtree> list = fileService.getDirTreeList(pojo);
        return JSON.toJSONString(R.succeed(list, "查询成功"));
    }

    /**
     * 获取目录列表
     *
     * @param id
     * @return
     */
    @GetMapping("/getDirs")
    public R getDirs(Long id) {
        Map<String, Object> map = fileService.getDirs(id);
        return R.succeed(map, "查询成功");
    }

    /**
     * 文件上传
     *
     * @param files
     * @param dirIds
     * @return
     */
    @Preview()
    @PostMapping({"", "/upload"})
    public R upload(@RequestParam(value = "file") MultipartFile[] files, String dirIds) {

        return fileService.upload(files, dirIds);
    }

    /**
     * 文件分片上传
     *
     * @param files
     * @return
     */
    @PostMapping("/uploadSharding")
    public R uploadSharding(@RequestParam(value = "file") MultipartFile[] files, String dirIds, HttpServletRequest request) {
        return fileService.uploadSharding(files, dirIds, request.getSession());
    }

    /**
     * 获取进度数据
     *
     * @param request
     * @return
     */
    @GetMapping("/percent")
    public Integer percent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (session.getAttribute("uploadPercent") == null ? 0 : (Integer) session.getAttribute("uploadPercent"));
    }

    /**
     * 重置上传进度
     *
     * @param request
     */
    @GetMapping("/percent/reset")
    public void resetPercent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("uploadPercent", 0);
        // ossUploadUtils.initPart();
    }

    /**
     * 文件下载
     *
     * @param url
     * @param response
     */
    @SaCheckPermission("file:download")
    @GetMapping("/downLoad")
    public void downLoad(String url, HttpServletResponse response) {
        fileService.download(url, response);
    }

    /**
     * 修改名称
     *
     * @param pojo
     */
    @PostMapping("/updateByName")
    public R upload(FilePojo pojo) {
        if (fileService.updateByName(pojo)) {
            return R.succeed("修改成功");
        }
        return R.failed("修改失败");

    }

    /**
     * 移动文件
     *
     * @param ids
     * @param parentId
     */
    @SaCheckPermission("file:move")
    @PostMapping("/move")
    public R move(String ids, Long parentId) {
        if (fileService.move(ids, parentId)) {
            return R.succeed("移动成功");
        }
        return R.failed("移动失败");
    }

    /**
     * 根据url删除文件
     *
     * @param url
     */
    @SaCheckPermission("file:delete")
    @PostMapping("/deleteFile")
    public R deleteFile(String url) {
        if (fileService.delete(url)) {
            return R.succeed("删除成功");
        }
        return R.failed("删除失败");

    }

    /**
     * 根据id删除文件
     *
     * @param id
     */
    @SaCheckPermission("file:delete")
    @PostMapping("/deleteByIds")
    public R deleteByIds(Long id) {
        if (fileService.deleteByIds(id)) {
            return R.succeed("删除成功");
        }
        return R.failed("删除失败");

    }

    /**
     * 新增文件夹
     *
     * @param pojo
     */
    @SaCheckPermission("dir:add")
    @PostMapping("/addFolder")
    public R addFolder(FilePojo pojo) {
        if (fileService.addFolder(pojo)) {
            return R.succeed("添加成功");
        }
        return R.failed("添加失败");
    }
}
