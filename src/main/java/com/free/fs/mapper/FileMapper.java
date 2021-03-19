package com.free.fs.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.fs.model.FilePojo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资源文件表mapper接口
 *
 * @author dinghao
 * @date 2021/3/12
 */
@Mapper
public interface FileMapper extends BaseMapper<FilePojo> {

    /**
     * 根据id查询它所有上级（包含自身）
     *
     * @param id 根节点
     * @return
     */
    @SqlParser(filter = true)
    List<FilePojo> selectParentList(Long id);
}
