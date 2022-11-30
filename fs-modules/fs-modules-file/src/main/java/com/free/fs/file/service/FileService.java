package com.free.fs.file.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.free.fs.file.model.IFile;
import com.free.fs.file.vo.DTreeFileVo;

import java.util.List;
import java.util.Map;

/**
 * 文件管理接口
 *
 * @author dinghao
 * @date 2021/3/15
 */
public interface FileService extends IService<IFile> {

    /**
     * 获取文件列表
     *
     * @param iFile
     * @return
     */
     List<IFile> getList(IFile iFile);

    /**
     * 获取文件树结构列表
     *
     * @param iFile
     * @return
     */
    List<DTreeFileVo> getTreeList(IFile iFile);

    /**
     * 获取目录树结构列表
     *
     * @param iFile
     * @return
     */
    List<DTreeFileVo> getDirTreeList(IFile iFile);

    /**
     * 新增文件夹
     *
     * @param iFile
     * @return
     */
    boolean addFolder(IFile iFile);

    /**
     * 重命名
     *
     * @param iFile
     * @return
     */
    boolean updateByName(IFile iFile);

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
