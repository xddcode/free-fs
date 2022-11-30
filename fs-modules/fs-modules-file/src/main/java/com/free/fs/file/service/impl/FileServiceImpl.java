package com.free.fs.file.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.file.mapper.FileMapper;
import com.free.fs.file.model.IFile;
import com.free.fs.file.service.FileService;
import com.free.fs.file.vo.DTreeFileVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 10:53
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, IFile> implements FileService {


    @Override
    public List<IFile> getList(IFile iFile) {
        LambdaQueryWrapper<IFile> wrapper = new LambdaQueryWrapper<>();
        String dirIds = iFile.getDirIds();
        if (StrUtil.isNotEmpty(dirIds)) {
            dirIds = dirIds.substring(dirIds.lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
            if (StrUtil.isNotEmpty(dirIds)) {
                wrapper.eq(IFile::getParentId, Long.parseLong(dirIds));
            } else {
                wrapper.eq(IFile::getParentId, CommonConstant.ROOT_PARENT_ID);
            }
        }
        wrapper.orderByDesc(IFile::getIsDir, IFile::getPutTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<DTreeFileVo> getTreeList(IFile iFile) {
        List<IFile> iFiles = baseMapper.selectList(
                new LambdaQueryWrapper<IFile>()
                        .eq(iFile.getIsDir() != null && iFile.getIsDir(), IFile::getIsDir, iFile.getIsDir())
                        .orderByDesc(IFile::getIsDir, IFile::getPutTime)
        );
        List<DTreeFileVo> dTreeFileVos = new ArrayList<>();
        iFiles.forEach(item -> {
            DTreeFileVo dTreeFileVo = new DTreeFileVo();
            BeanUtils.copyProperties(item, dTreeFileVo);
            dTreeFileVo.setTitle(item.getName());
            //设置图标
            if (dTreeFileVo.getIsDir()) {
                dTreeFileVo.setIconClass(CommonConstant.DTREE_ICON_1);
            } else {
                dTreeFileVo.setIconClass(CommonConstant.DTREE_ICON_2);
            }
            dTreeFileVos.add(dTreeFileVo);
        });
        return dTreeFileVos.stream()
                .filter(m -> m.getParentId() == -1)
                .peek((m) -> m.setChildren(getChildrens(m, dTreeFileVos)))
                .collect(Collectors.toList());
    }

    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     * @return 根节点信息
     */
    private List<DTreeFileVo> getChildrens(DTreeFileVo root, List<DTreeFileVo> all) {
        return all.stream()
                .filter(m -> Objects.equals(m.getParentId(), root.getId()))
                .peek((m) -> m.setChildren(getChildrens(m, all)))
                .collect(Collectors.toList());
    }

    @Override
    public List<DTreeFileVo> getDirTreeList(IFile iFile) {
        iFile.setIsDir(Boolean.TRUE);
        return this.getTreeList(iFile);
    }

    @Override
    public boolean addFolder(IFile iFile) {
        String dirId = iFile.getDirIds().substring(iFile.getDirIds().lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
        if (CommonConstant.DIR_SPLIT.equals(dirId) || StrUtil.isEmpty(dirId)) {
            iFile.setParentId(CommonConstant.ROOT_PARENT_ID);
        } else {
            IFile p = baseMapper.selectById(Long.parseLong(dirId));
            iFile.setParentId(p.getId());
        }
        iFile.setType(CommonConstant.DEFAULT_DIR_TYPE);
        iFile.setIsDir(Boolean.TRUE);
        iFile.setIsImg(Boolean.FALSE);
        //判断文件夹名称在当前目录中是否存在
        Long count = baseMapper.selectCount(
                new LambdaQueryWrapper<IFile>()
                        .eq(IFile::getName, iFile.getName())
                        .eq(IFile::getIsDir, Boolean.TRUE)
                        .eq(IFile::getParentId, iFile.getParentId())
        );
        if (count > 0) {
            throw new BusinessException("当前目录名称已存在，请修改后重试！");
        }
        return baseMapper.insert(iFile) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByName(IFile iFile) {
        if (iFile.getName().equals(iFile.getRename())) {
            throw new BusinessException("当前名称与原始名称相同，请修改后重试！");
        }
        IFile p = baseMapper.selectById(iFile.getId());
        Long count = baseMapper.selectCount(
                new LambdaQueryWrapper<IFile>()
                        .eq(IFile::getName, iFile.getRename())
                        .eq(IFile::getIsDir, p.getIsDir())
                        .eq(IFile::getParentId, p.getParentId())
        );
        if (count > 0) {
            throw new BusinessException("当前目录已存在该名称,请修改后重试！");
        }
        IFile updPojo = new IFile();
        updPojo.setId(iFile.getId());
        updPojo.setName(iFile.getRename());
        return baseMapper.updateById(updPojo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(Long id) {
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean move(String ids, Long parentId) {
        if (StrUtil.isEmpty(ids)) {
            throw new BusinessException("请选择要移动的文件或目录");
        }
        String[] idsArrays = ids.split(CommonConstant.STRING_SPLIT);
        IFile updatePojo;
        for (String id : idsArrays) {
            updatePojo = new IFile();
            updatePojo.setId(Long.parseLong(id));
            updatePojo.setParentId(parentId);
            if (baseMapper.updateById(updatePojo) <= 0) {
                throw new BusinessException("移动失败");
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getDirs(Long id) {
        Map<String, Object> map = new HashMap<>();
        List<IFile> filePojos = this.getParentList(new ArrayList<>(), id);
        StringBuilder dir = new StringBuilder(CommonConstant.DIR_SPLIT);
        StringBuilder dirIds = new StringBuilder(CommonConstant.DIR_SPLIT);
        if (!CollectionUtils.isEmpty(filePojos)) {
            for (IFile filePojo : filePojos) {
                dir.append(filePojo.getName()).append(CommonConstant.DIR_SPLIT);
                dirIds.append(filePojo.getId()).append(CommonConstant.DIR_SPLIT);
            }
        }
        map.put("dirs", dir.length() > 0 ? dir.deleteCharAt(dir.length() - 1).toString() : "");
        map.put("dirIds", dirIds.length() > 0 ? dirIds.deleteCharAt(dirIds.length() - 1).toString() : "");
        return map;
    }

    //根据id查询所有上级目录
    private List<IFile> getParentList(List<IFile> list, Long id) {
        IFile filePojo = baseMapper.selectById(id);
        list.add(filePojo);
        if (filePojo != null && filePojo.getParentId() != null && filePojo.getParentId() != -1) {
            getParentList(list, filePojo.getParentId());
        }
        if (CollUtil.isNotEmpty(list)) {
            list.sort(Comparator.comparing(IFile::getId));
        }
        return list;
    }
}
