package com.free.fs.core.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.domain.FileBo;
import com.free.fs.common.domain.Result;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.storage.IFileStorage;
import com.free.fs.common.storage.IFileStorageProvider;
import com.free.fs.core.domain.FileInfo;
import com.free.fs.core.domain.dto.UpdateFileNameDTO;
import com.free.fs.core.mapper.FileMapper;
import com.free.fs.core.service.FileService;
import com.free.fs.common.domain.Dtree;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.free.fs.core.domain.table.FileInfoTableDef.FILE_INFO;
import static com.mybatisflex.core.query.QueryMethods.noCondition;

/**
 * 文件管理接口实现
 *
 * @author dinghao
 * @date 2024/06/07 16:56
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements FileService {

    private final IFileStorageProvider uploaderProvider;

    @Override
    public List<FileInfo> getList(FileInfo info) {
        String dirIds = info.getDirIds();
        if (StringUtils.isNotEmpty(dirIds)) {
            dirIds = dirIds.substring(dirIds.lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
        }
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .from(FILE_INFO)
                .where(FILE_INFO.USER_ID.eq(StpUtil.getLoginIdAsLong()))
                .and(FILE_INFO.PARENT_ID.eq(StringUtils.isEmpty(dirIds) ? CommonConstant.ROOT_PARENT_ID : Long.parseLong(dirIds)))
                .orderBy(FILE_INFO.IS_DIR.desc(), FILE_INFO.PUT_TIME.desc());
        return this.list(queryWrapper);
    }

    @Override
    public List<Dtree> getTreeList(FileInfo info) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .from(FILE_INFO)
                .where(info.getIsDir() != null && info.getIsDir() ? FILE_INFO.IS_DIR.eq(true) : noCondition())
                .and(FILE_INFO.USER_ID.eq(StpUtil.getLoginIdAsLong()))
                .orderBy(FILE_INFO.IS_DIR.desc(), FILE_INFO.PUT_TIME.desc());
        List<FileInfo> fileInfos = this.list(queryWrapper);
        List<Dtree> dtrees = fileInfos.stream().map(item -> {
            Dtree dtree = new Dtree();
            BeanUtils.copyProperties(item, dtree);
            if (dtree.getIsDir()) {
                dtree.setIconClass(CommonConstant.DTREE_ICON_1);
                dtree.setTitle(item.getName());
            } else {
                dtree.setIconClass(CommonConstant.DTREE_ICON_2);
                dtree.setTitle(item.getName() + CommonConstant.SUFFIX_SPLIT + item.getSuffix());
            }
            return dtree;
        }).collect(Collectors.toList());
        return dtrees.stream()
                .filter(m -> m.getParentId() == -1)
                .peek((m) -> m.setChildren(getChildrens(m, dtrees)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dtree> getDirTreeList(FileInfo info) {
        info.setIsDir(Boolean.TRUE);
        return this.getTreeList(info);
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
    public Result<?> upload(MultipartFile[] files, String dirIds) {
        if (files == null || files.length == 0) {
            throw new BusinessException("文件不能为空");
        }
        for (MultipartFile file : files) {
            FileInfo fileInfo = uploadFile(file);
            // 文件目录id
            String dirId = dirIds.substring(dirIds.lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
            if (CommonConstant.DIR_SPLIT.equals(dirId)
                    || StringUtils.isEmpty(dirId)) {
                fileInfo.setParentId(CommonConstant.ROOT_PARENT_ID);
            } else {
                FileInfo f = this.getById(Long.parseLong(dirId));
                fileInfo.setParentId(f.getId());
            }
            int flag = 0;
            fileInfo.setName(recursionFindName(fileInfo.getName(), fileInfo.getName(), fileInfo.getParentId(), flag));
            fileInfo.setUserId(StpUtil.getLoginIdAsLong());
            if (!this.save(fileInfo)) {
                return Result.error("文件：" + file.getOriginalFilename() + "上传失败");
            }
        }
        return Result.ok("上传成功");
    }

    private FileInfo uploadFile(MultipartFile file) {
        FileBo bo = uploaderProvider.getStorage().upload(file);
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(bo, fileInfo);
        return fileInfo;
    }

    /**
     * 递归查询查询name是否存在，如果存在，则给name+(flag)
     *
     * @param sname 原name
     * @param rname 修改后name
     * @param flag  标记值
     * @return
     */
    private String recursionFindName(String sname, String rname, Long parentId, int flag) {
        boolean exists = true;
        while (exists) {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .select()
                    .from(FILE_INFO)
                    .where(FILE_INFO.NAME.eq(rname))
                    .and(FILE_INFO.IS_DIR.eq(Boolean.FALSE))
                    .and(FILE_INFO.PARENT_ID.eq(parentId));
            long count = this.count(queryWrapper);
            if (count > 0) {
                flag++;
                rname = sname + "(" + flag + ")";
            } else {
                exists = false;
            }
        }
        return flag > 0 ? sname + "(" + flag + ")" : sname;
    }

    @Override
    public Result uploadSharding(MultipartFile[] files, String dirIds, HttpSession session) {
        //TODO 分片上传待实现
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(String url) {
        //判断是否个人资源
        FileInfo fileInfo = this.getOne(new QueryWrapper().where(FILE_INFO.URL.eq(url)));
        if (fileInfo == null) {
            throw new BusinessException("资源不存在！");
        }
        if (!fileInfo.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            throw new BusinessException("你无权删除此资源！");
        }
        if (!this.remove(new QueryWrapper().where(FILE_INFO.URL.eq(url)))) {
            throw new BusinessException("资源删除失败！");
        }
        String objectName = fileInfo.getFileName();
        deleteFile(objectName);
        return true;
    }

    private void deleteFile(String objectName) {
        IFileStorage uploader = uploaderProvider.getStorage();
        uploader.delete(objectName);
    }

    @Override
    public void download(String url, HttpServletResponse response) {
        IFileStorage uploader = uploaderProvider.getStorage();
        FileInfo fileInfo = this.getOne(new QueryWrapper().where(FILE_INFO.URL.eq(url)));
        if (fileInfo == null) {
            throw new BusinessException("资源不存在！");
        }
        String objectName = fileInfo.getFileName();
        uploader.download(objectName, response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addFolder(FileInfo info) {
        String dirId = info.getDirIds().substring(info.getDirIds().lastIndexOf(CommonConstant.DIR_SPLIT) + 1);
        if (CommonConstant.DIR_SPLIT.equals(dirId) || StringUtils.isEmpty(dirId)) {
            info.setParentId(CommonConstant.ROOT_PARENT_ID);
        } else {
            FileInfo fileInfo = this.getById(Long.parseLong(dirId));
            info.setParentId(fileInfo.getId());
        }
        info.setType(CommonConstant.DEFAULT_DIR_TYPE);
        info.setIsDir(Boolean.TRUE);
        info.setIsImg(Boolean.FALSE);
        info.setUserId(StpUtil.getLoginIdAsLong());
        //判断文件夹名称在当前目录中是否存在
        long count = this.count(new QueryWrapper()
                .where(FILE_INFO.NAME.eq(info.getName()))
                .and(FILE_INFO.IS_DIR.eq(Boolean.TRUE))
                .and(FILE_INFO.PARENT_ID.eq(info.getParentId()))
                .and(FILE_INFO.USER_ID.eq(info.getUserId()))
        );
        if (count > 0) {
            throw new BusinessException("当前目录名称已存在，请修改名称！");
        }
        return this.save(info);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByName(UpdateFileNameDTO dto) {
        if (dto.getName().equals(dto.getRename())) {
            throw new BusinessException("当前名称与原始名称相同，请修改后重试！");
        }
        // 获取文件信息
        FileInfo fileInfo = this.getById(dto.getId());
        if (fileInfo == null) {
            throw new BusinessException("文件不存在！");
        }
        long count = this.count(new QueryWrapper()
                .where(FILE_INFO.NAME.eq(dto.getRename()))
                .and(FILE_INFO.IS_DIR.eq(fileInfo.getIsDir()))
                .and(FILE_INFO.PARENT_ID.eq(fileInfo.getParentId()))
                .and(FILE_INFO.USER_ID.eq(fileInfo.getUserId()))
        );
        if (count > 0) {
            throw new BusinessException("当前目录已存在该名称,请修改名称！");
        }
        FileInfo updInfo = new FileInfo();
        updInfo.setId(dto.getId());
        updInfo.setName(dto.getRename());
        return this.updateById(updInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(Long id) {
        //id集合
        List<Long> idList = new ArrayList<>();
        //资源key集合
        List<String> keys = new ArrayList<>();
        this.selectPermissionChildById(id, idList, keys);
        idList.add(id);
        // 删除云资源
        FileInfo info = this.getById(id);
        keys.add(info.getUrl());
        for (String key : keys) {
            FileInfo fileInfo = this.getOne(new QueryWrapper().where(FILE_INFO.URL.eq(key)));
            if (fileInfo != null && StringUtils.isNotEmpty(fileInfo.getUrl())) {
                String objectName = fileInfo.getFileName();
                deleteFile(objectName);
            }
        }
        return this.removeByIds(idList);
    }

    private void selectPermissionChildById(Long id, List<Long> idList, List<String> keys) {
        //查询菜单里面子菜单id
        List<FileInfo> childIdList = this.list(new QueryWrapper().where(FILE_INFO.PARENT_ID.eq(id)));
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        if (!CollectionUtils.isEmpty(childIdList)) {
            childIdList.forEach(item -> {
                //封装idList里面
                idList.add(item.getId());
                //如果资源不是文件夹，则把资源路径放入keys里面，做删除操作
                if (!item.getIsDir()) {
                    keys.add(item.getUrl());
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
        FileInfo updateInfo;
        for (String id : idsArry) {
            updateInfo = new FileInfo();
            updateInfo.setId(Long.parseLong(id));
            updateInfo.setParentId(parentId);
            if (!this.updateById(updateInfo)) {
                throw new BusinessException("移动失败");
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getDirs(Long id) {
        Map<String, Object> map = new HashMap<>();
        // 通过pid获取目录结构
        List<FileInfo> filePojos = this.getParentList(new ArrayList<>(), id);
        StringBuilder dir = new StringBuilder(CommonConstant.DIR_SPLIT);
        StringBuilder dirIds = new StringBuilder(CommonConstant.DIR_SPLIT);
        if (!CollectionUtils.isEmpty(filePojos)) {
            for (FileInfo fileInfo : filePojos) {
                dir.append(fileInfo.getName()).append(CommonConstant.DIR_SPLIT);
                dirIds.append(fileInfo.getId()).append(CommonConstant.DIR_SPLIT);
            }
        }
        map.put("dirs", !dir.isEmpty() ? dir.deleteCharAt(dir.length() - 1).toString() : "");
        map.put("dirIds", !dirIds.isEmpty() ? dirIds.deleteCharAt(dirIds.length() - 1).toString() : "");
        return map;
    }

    //根据id查询所有上级目录
    private List<FileInfo> getParentList(List<FileInfo> list, Long id) {
        // 获取当前的目录
        FileInfo fileInfo = this.getById(id);
        if (fileInfo != null) {
            if (fileInfo.getParentId() != null && fileInfo.getParentId() != -1) {
                getParentList(list, fileInfo.getParentId());
            }
            list.add(fileInfo);
        }
        // 不排了, no.48行拿的最后一个目录, 通过id排序可能会导致,目录展示的层级不对
//        if (CollUtil.isNotEmpty(list)) {
//            list.sort(Comparator.comparing(FileInfo::getId));
//        }
        return list;
    }
}

