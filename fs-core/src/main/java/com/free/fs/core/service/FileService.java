package com.free.fs.core.service;

import com.free.fs.common.domain.Dtree;
import com.free.fs.common.domain.Result;
import com.free.fs.core.domain.FileInfo;
import com.free.fs.core.domain.dto.UpdateFileNameDTO;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件服务接口
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:09
 */
public interface FileService extends IService<FileInfo> {

    /**
     * 获取文件列表
     *
     * @param info
     * @return
     */
    List<FileInfo> getList(FileInfo info);

    /**
     * 获取文件树结构列表
     *
     * @param info
     * @return
     */
    List<Dtree> getTreeList(FileInfo info);

    /**
     * 获取目录树结构列表
     *
     * @param info
     * @return
     */
    List<Dtree> getDirTreeList(FileInfo info);

    /**
     * 上传文件
     *
     * @param files
     * @param dirIds
     * @return
     */
    Result upload(MultipartFile[] files, String dirIds);

    /**
     * 分片上传大文件
     *
     * @param files
     * @return
     */
    Result uploadSharding(MultipartFile[] files, String dirIds, HttpSession session);

    /**
     * 删除文件
     *
     * @param url
     * @return
     */
    boolean delete(String url);

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    void download(String url, HttpServletResponse response);

    /**
     * 新增文件夹
     *
     * @param fileInfo
     * @return
     */
    boolean addFolder(FileInfo fileInfo);

    /**
     * 重命名
     *
     * @param dto
     * @return
     */
    boolean updateByName(UpdateFileNameDTO dto);

    /**
     * 根据id集合批量删除
     *
     * @param id
     * @return
     */
    boolean deleteByIds(Long id);

    /**
     * 移动文件
     *
     * @param ids
     * @param parentId
     * @return
     */
    boolean move(String ids, Long parentId);

    /**
     * 根据id拼接父目录
     *
     * @param id
     * @return
     */
    Map<String, Object> getDirs(Long id);
}
