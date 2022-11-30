package com.free.fs.component.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件metadata
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022/11/30 13:50
 */
public class FileMeta extends HashMap<String, String> {

    public FileMeta add(String key, String value) {
        this.put(key, value);
        return this;
    }

    public FileMeta addAll(Map<String, String> map) {
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }
}
