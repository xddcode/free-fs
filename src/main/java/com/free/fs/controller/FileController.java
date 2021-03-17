package com.free.fs.controller;

import com.alibaba.fastjson.JSON;
import com.free.fs.common.properties.QiniuProperties;
import com.free.fs.common.utils.R;
import com.free.fs.model.Dtree;
import com.free.fs.model.FilePojo;
import com.free.fs.service.FileService;
import com.qiniu.common.QiniuException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 文件上传管理
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Slf4j
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final FileService fileService;

    private final QiniuProperties qiniuProperties;

    @GetMapping({"", "/list"})
    public R getList(FilePojo pojo) {
        List<FilePojo> list = fileService.getList(pojo);
        return R.succeed(list, "查询成功");
    }

    @GetMapping("/getTree")
    public String getTree(FilePojo pojo) {
        List<Dtree> list = fileService.getTreeList(pojo);
        return JSON.toJSONString(R.succeed(list, "查询成功"));
    }

    @GetMapping("/getDirTree")
    public String getDirTree(FilePojo pojo) {
        List<Dtree> list = fileService.getDirTreeList(pojo);
        return JSON.toJSONString(R.succeed(list, "查询成功"));
    }

    @GetMapping("/getDirs")
    public R getDirs(Long id) {
        Map<String, Object> map = fileService.getDirs(id);
        return R.succeed(map, "查询成功");
    }

    @PostMapping({"", "/upload"})
    public R upload(@RequestParam(value = "file") MultipartFile file, String dirIds) {
        try {
            return fileService.upload(file, dirIds);
        } catch (IOException e) {
            log.warn(e.getMessage());
            return R.failed("上传失败，服务异常");
        }
    }

    @RequiresPermissions("file:download")
    @GetMapping("/downLoad")
    public void downLoad(String url, HttpServletResponse response) {
        InputStream in = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(200000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            in = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len = 0;
            response.reset();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/octet-stream");
            String fileName = url.replaceAll(qiniuProperties.getPath() + "/", "");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(url + "下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/updateByName")
    public R upload(FilePojo pojo) {
        if (fileService.updateByName(pojo)) {
            return R.succeed("修改成功");
        }
        return R.failed("修改失败");

    }

    @PostMapping("/move")
    public R move(String ids, Long parentId) {
        if (fileService.move(ids, parentId)) {
            return R.succeed("移动成功");
        }
        return R.failed("移动失败");
    }

    @RequiresPermissions("file:delete")
    @PostMapping("/deleteFile")
    public R deleteFile(String url) {
        try {
            if (fileService.deleteFile(url)) {
                return R.succeed("删除成功");
            }
            return R.failed("删除失败");
        } catch (QiniuException e) {
            log.warn("删除资源失败，服务异常");
            return R.failed("删除资源失败，服务异常");
        }
    }

    @RequiresPermissions("file:delete")
    @PostMapping("/deleteByIds")
    public R deleteByIds(Long id) {
        try {
            if (fileService.deleteByIds(id)) {
                return R.succeed("删除成功");
            }
            return R.failed("删除失败");
        } catch (QiniuException e) {
            log.warn("删除资源失败，服务异常");
            return R.failed("删除资源失败，服务异常");
        }
    }

    @RequiresPermissions("dir:add")
    @PostMapping("/addFolder")
    public R addFolder(FilePojo pojo) {
        if (fileService.addFolder(pojo)) {
            return R.succeed("添加成功");
        }
        return R.failed("添加失败");
    }

}
