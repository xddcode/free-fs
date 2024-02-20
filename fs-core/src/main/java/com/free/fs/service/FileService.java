package com.free.fs.service;

import com.free.fs.common.domain.Dtree;
import com.free.fs.common.domain.Result;
import com.free.fs.domain.FileInfo;
import com.free.fs.domain.dto.FileDTO;
import com.free.fs.domain.dto.FolderDTO;
import com.free.fs.domain.vo.FolderVO;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件管理接口
 *
 * @author dinghao
 * @date 2021/3/15
 */
public interface FileService extends IService<FileInfo> {

    /**
     * 获取文件列表
     *
     * @param dirIds 目录id
     * @return
     */
    List<FileInfo> getList(String dirIds);


    List<FileInfo> getListByPage(FileDTO dto);

    /**
     * 获取文件树结构列表
     *
     * @param fileInfo
     * @return
     */
    List<Dtree> getTreeList(FileInfo fileInfo);

    /**
     * 获取目录树结构列表
     *
     * @param fileInfo
     * @return
     */
    List<Dtree> getDirTreeList(FileInfo fileInfo);

    /**
     * 上传文件
     *
     * @param files
     * @param dirIds
     * @return
     */
    Result<?> upload(MultipartFile[] files, String dirIds);

    /**
     * 分片上传大文件
     *
     * @param files
     * @return
     */
    Result<?> uploadSharding(MultipartFile[] files, String dirIds, HttpSession session);

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

    boolean addFolder(FolderDTO folderDTO);

    /**
     * 重命名
     *
     * @param fileInfo
     * @return
     */
    boolean updateByName(FileInfo fileInfo);

    /**
     * 根据id集合批量删除
     *
     * @param id 文件id
     * @return
     */
    boolean deleteByIds(Long id);

    boolean deleteByIds(Long[] ids);

    /**
     * 移动文件
     *
     * @param ids      移动的目录或文件id
     * @param parentId 父目录id
     * @return 是否成功
     */
    boolean move(String ids, Long parentId);

    /**
     * 根据id拼接父目录
     *
     * @param id 文件id
     * @return
     */
    Map<String, Object> getDirs(Long id);

    /**
     * 根据文件夹id 返回自下而上的文件夹
     * @param id 文件夹id
     * @return list
     */
    List<FolderVO> getLevelFolders(Long id);
}
