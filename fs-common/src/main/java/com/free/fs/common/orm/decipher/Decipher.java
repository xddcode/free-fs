package com.free.fs.common.orm.decipher;

import com.mybatisflex.core.datasource.DataSourceDecipher;
import com.mybatisflex.core.datasource.DataSourceProperty;

/**
 * 数据源解密
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/06/07 10:34
 */
public class Decipher implements DataSourceDecipher {
    @Override
    public String decrypt(DataSourceProperty dataSourceProperty, String s) {
//        if (dataSourceProperty == DataSourceProperty.USERNAME) {
//            return value.substring(0, 4);
//        }
        return null;
    }
}
