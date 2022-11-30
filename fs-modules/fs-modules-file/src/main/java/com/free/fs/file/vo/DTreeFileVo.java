package com.free.fs.file.vo;

import com.free.fs.file.model.IFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * dtree组件对象实体
 *
 * @author dinghao
 * @date 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DTreeFileVo extends IFile {

    /**
     * dtree自定义图标
     */
    private String iconClass;

    /**
     * dtree开启复选框
     */
    private String checkArr = "0";

    /**
     * dtree节点名称
     */
    private String title;

    /**
     * 是否展开节点
     */
    private Boolean spread = true;

    /**
     * 子集合
     */
    private List<DTreeFileVo> children;

}

