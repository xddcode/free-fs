package com.free.fs.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.fs.file.model.IFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源文件表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface FileMapper extends BaseMapper<IFile> {

}
