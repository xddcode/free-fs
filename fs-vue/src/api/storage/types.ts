export interface StorageDictVo {
    /** 存储平台键 */
    key: string;
    /** 名称 */
    name: string;

    value: string;
}

/**
 * 存储平台配置
 */
export interface StoragePlatformVO {
    /** 存储平台名称 */
    name: string;
    /** 存储平台唯一标识 */
    identifier: string;
    /** 存储平台图标 */
    icon: string;
    /** 存储平台配置参数 */
    configScheme: Array<PlatformStructure>;
    /** 是否已配置 */
    status: boolean;
    /** 是否已启用 */
    enabled: boolean;
}

export interface PlatformStructure {
    identifier: string;
    label: string;
    dataType: string;
    description: string;
    validation: StructureValid;
}

export interface StructureValid {
    required: boolean;
}

/**
 * 存储平台配置表单
 */
export interface StorageSettingForm extends BaseEntity {
    /**
     * 主键
     */
    id?: string | number;
    /**
     * 配置明细
     */
    configData: string;
    /**
     * 存储平台唯一标识
     */
    platformIdentifier: string;
}

export interface StorageSettingVO {

    id: string | number;

    platformIdentifier: string;

    configData: string | object;

    enabled: number;

    createTime: string;

    updateTime: string;
}