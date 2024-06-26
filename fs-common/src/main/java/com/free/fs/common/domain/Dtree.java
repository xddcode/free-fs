package com.free.fs.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Dtree extends FileBo {

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
    private List<Dtree> children;

}
