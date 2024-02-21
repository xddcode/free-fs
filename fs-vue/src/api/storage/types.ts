export interface StorageDictVo {
    /** 存储平台键 */
    key: string;
    /** 名称 */
    name: string;
}

/**
 * 存储平台配置
 */
export interface StoragePlatformVO {
    /** 主键 */
    id: string;
    /** 存储平台名称 */
    name: string;
    /** 存储平台唯一标识 */
    identifier: string;
    /** 存储平台图标 */
    icon: string;
    /** 存储平台配置参数 */
    configScheme: string | object;
}