package com.free.fs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.utils.R;
import com.free.fs.mapper.FileMapper;
import com.free.fs.model.Dtree;
import com.free.fs.model.FilePojo;
import com.free.fs.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件管理抽象接口实现
 *
 * @author dinghao
 * @date 2021/4/6
 */
public abstract class AbstractIFileService extends ServiceImpl<FileMapper, FilePojo> implements FileService {

    @SuppressWarnings("unchecked")
    @Override
    public List<FilePojo> getList(FilePojo pojo) {
        LambdaQueryWrapper<FilePojo> wrapper = new LambdaQueryWrapper<>();
        String dirIds = pojo.getDirIds();
        if (StringUtils.isNotEmpty(dirIds)) {
            dirIds = dirIds.substring(dirIds.lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
            if (StringUtils.isNotEmpty(dirIds)) {
                wrapper.eq(FilePojo::getParentId, Long.parseLong(dirIds));
            } else {
                wrapper.eq(FilePojo::getParentId, CommonConstant.ROOT_PARENT_ID);
            }
        }
        wrapper.orderByDesc(FilePojo::getIsDir, FilePojo::getPutTime);
        return baseMapper.selectList(wrapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Dtree> getTreeList(FilePojo pojo) {
        List<FilePojo> filePojos = baseMapper.selectList(
                new LambdaQueryWrapper<FilePojo>()
                        .eq(pojo.getIsDir()!=null && pojo.getIsDir(), FilePojo::getIsDir, pojo.getIsDir())
                        .orderByDesc(FilePojo::getIsDir, FilePojo::getPutTime)
        );
        List<Dtree> dtrees = new ArrayList<>();
        filePojos.forEach(item -> {
            Dtree dtree = new Dtree();
            BeanUtils.copyProperties(item,dtree);
            dtree.setTitle(item.getName());
            //设置图标
            if (dtree.getIsDir()) {
                dtree.setIconClass(CommonConstant.DTREE_ICON_1);
            } else {
                dtree.setIconClass(CommonConstant.DTREE_ICON_2);
            }
            dtrees.add(dtree);
        });
        return dtrees.stream()
                     .filter(m -> m.getParentId() == -1)
                     .peek((m) -> m.setChildren(getChildrens(m, dtrees)))
                     .collect(Collectors.toList());
    }

    @Override
    public List<Dtree> getDirTreeList(FilePojo pojo) {
        pojo.setIsDir(Boolean.TRUE);
        return this.getTreeList(pojo);
    }

    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     * @return 根节点信息
     */

    private List<Dtree> getChildrens(Dtree root, List<Dtree> all) {
        return all.stream()
                  .filter(m -> Objects.equals(m.getParentId(), root.getId()))
                    .peek((m) -> m.setChildren(getChildrens(m, all)))
                .collect(Collectors.toList());
    }

    @Override
    public R upload(MultipartFile[] files, String dirIds){
        if (files == null || files.length == 0) {
            throw new BusinessException("文件不能为空");
        }
        for (MultipartFile file : files) {
            FilePojo filePojo = uploadFile(file);
            String dirId = dirIds.substring(dirIds.lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
            if (CommonConstant.DIR_SPLIT.equals(dirId) || StringUtils.isEmpty(dirId)) {
                filePojo.setParentId(CommonConstant.ROOT_PARENT_ID);
            } else {
                FilePojo p = baseMapper.selectById(Long.parseLong(dirId));
                filePojo.setParentId(p.getId());
            }
            int flag = 0;
            filePojo.setName(recursionFindName(filePojo.getName(), filePojo.getName(), filePojo.getParentId(), flag));
            if (baseMapper.insert(filePojo) <= 0) {
                return R.failed("文件：" + file.getOriginalFilename() + "上传失败");
            }
        }
        return R.succeed("上传成功");
    }

    /**
     * 递归查询查询name是否存在，如果存在，则给name+(flag)
     *
     * @param sname 原name
     * @param rname 修改后name
     * @param flag 标记值
     * @return
     */
    private String recursionFindName(String sname, String rname, Long parentId, int flag) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<FilePojo>()
                .eq(FilePojo::getName, rname)
                .eq(FilePojo::getIsDir,Boolean.FALSE)
                .eq(FilePojo::getParentId,parentId));
        if (count > 0) {
            flag++;
            rname = sname + "(" + flag + ")";
            return recursionFindName(sname, rname, parentId, flag);
        }
        return flag > 0 ? sname + "(" + flag + ")" : sname;
    }

    /**
     * 上传文件
     *
     * @param file
     */
    protected abstract FilePojo uploadFile(MultipartFile file);


    @Override
    public R uploadSharding(MultipartFile[] files, String dirIds, HttpSession session) {
        if (files == null || files.length == 0) {
            throw new BusinessException("文件不能为空");
        }
        for (MultipartFile file : files) {
            FilePojo filePojo = uploadFileSharding(file, session);
            String dirId = dirIds.substring(dirIds.lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
            if (CommonConstant.DIR_SPLIT.equals(dirId) || StringUtils.isEmpty(dirId)) {
                filePojo.setParentId(CommonConstant.ROOT_PARENT_ID);
            } else {
                FilePojo p = baseMapper.selectById(Long.parseLong(dirId));
                filePojo.setParentId(p.getId());
            }
            int flag = 0;
            filePojo.setName(recursionFindName(filePojo.getName(), filePojo.getName(), filePojo.getParentId(), flag));
            if (baseMapper.insert(filePojo) <= 0) {
                return R.failed("文件：" + file.getOriginalFilename() + "上传失败");
            }
        }
        return R.succeed("上传成功");
    }

    /**
     * 分片上传文件
     *
     * @param file
     * @param session
     */
    protected abstract FilePojo uploadFileSharding(MultipartFile file,HttpSession session);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(String url){
        //先删除数据库
        if (baseMapper.delete(new LambdaQueryWrapper<FilePojo>().eq(FilePojo::getUrl, url)) <= 0) {
            throw new BusinessException("资源删除失败");
        }
        //在删除七牛云
        deleteFile(url);
        return true;
    }

    /**
     * 删除文件资源
     *
     * @param url 文件路径
     */
    protected abstract void deleteFile(String url);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addFolder(FilePojo pojo) {
        String dirId = pojo.getDirIds().substring(pojo.getDirIds().lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
        if (CommonConstant.DIR_SPLIT.equals(dirId) || StringUtils.isEmpty(dirId)) {
            pojo.setParentId(CommonConstant.ROOT_PARENT_ID);
        } else {
            FilePojo p = baseMapper.selectById(Long.parseLong(dirId));
            pojo.setParentId(p.getId());
        }
        pojo.setType(CommonConstant.DEFAULT_DIR_TYPE);
        pojo.setIsDir(Boolean.TRUE);
        pojo.setIsImg(Boolean.FALSE);
        //判断文件夹名称在当前目录中是否存在
        Integer count = baseMapper.selectCount(
                new LambdaQueryWrapper<FilePojo>()
                        .eq(FilePojo::getName,pojo.getName())
                        .eq(FilePojo::getIsDir,Boolean.TRUE)
                        .eq(FilePojo::getParentId,pojo.getParentId())
        );
        if (count > 0) {
            throw new BusinessException("当前目录名称已存在，请修改后重试！");
        }
        return baseMapper.insert(pojo) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByName(FilePojo pojo) {
        if (pojo.getName().equals(pojo.getRename())) {
            throw new BusinessException("当前名称与原始名称相同，请修改后重试！");
        }
        FilePojo p = baseMapper.selectById(pojo.getId());
        Integer count = baseMapper.selectCount(
                new LambdaQueryWrapper<FilePojo>()
                        .eq(FilePojo::getName, pojo.getRename())
                        .eq(FilePojo::getIsDir,p.getIsDir())
                        .eq(FilePojo::getParentId,p.getParentId())
        );
        if (count > 0) {
            throw new BusinessException("当前目录已存在该名称,请修改后重试！");
        }
        FilePojo updPojo = new FilePojo();
        updPojo.setId(pojo.getId());
        updPojo.setName(pojo.getRename());
        return baseMapper.updateById(updPojo) > 0;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(Long id){
        //id集合
        List<Long> idList = new ArrayList<>();
        //资源key集合
        List<String> keys = new ArrayList<>();
        this.selectPermissionChildById(id, idList, keys);
        idList.add(id);
        //删除七牛云里的资源
        FilePojo pojo = baseMapper.selectById(id);
        keys.add(pojo.getFileName());
        for (String key : keys) {
            deleteFile(key);
        }
        return baseMapper.deleteBatchIds(idList) > 0;
    }

    private void selectPermissionChildById(Long id, List<Long> idList, List<String> keys) {
        //查询菜单里面子菜单id
        LambdaQueryWrapper<FilePojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FilePojo::getParentId, id);
        List<FilePojo> childIdList = baseMapper.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        if (!CollectionUtils.isEmpty(childIdList)) {
            childIdList.forEach(item -> {
                //封装idList里面
                idList.add(item.getId());
                //如果资源不是文件夹，则把资源名称封装到keys
                if (!item.getIsDir()) {
                    keys.add(item.getFileName());
                }
                //递归查询
                this.selectPermissionChildById(item.getId(), idList, keys);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean move(String ids, Long parentId) {
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException("请选择要移动的文件或目录");
        }
        String[] idsArry = ids.split(CommonConstant.STRING_SPLIT);
        FilePojo updatePojo;
        for (String id : idsArry) {
            updatePojo = new FilePojo();
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
        List<FilePojo> filePojos = baseMapper.selectParentList(id);
        StringBuilder dir = new StringBuilder(CommonConstant.DIR_SPLIT);
        StringBuilder dirIds = new StringBuilder(CommonConstant.DIR_SPLIT);
        if (!CollectionUtils.isEmpty(filePojos)) {
            for (FilePojo filePojo : filePojos) {
                dir.append(filePojo.getName()).append(CommonConstant.DIR_SPLIT);
                dirIds.append(filePojo.getId()).append(CommonConstant.DIR_SPLIT);
            }
        }
        map.put("dirs", dir.length() > 0 ? dir.deleteCharAt(dir.length() - 1).toString() : "");
        map.put("dirIds", dirIds.length() > 0 ? dirIds.deleteCharAt(dirIds.length() - 1).toString() : "");
        return map;
    }
}
