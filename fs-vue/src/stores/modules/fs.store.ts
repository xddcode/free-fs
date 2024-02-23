import { defineStore } from 'pinia';
import to from "await-to-js";
import store from "/@/stores";
import { useStorageApi } from "/@/api/storage";
import { StorageDictVo, StoragePlatformVO } from "/@/api/storage/types";

// 支持文件存储的类型
export const useFsConfig = defineStore( 'fsConfig', () => {
    // 当前使用的存储平台
    const fileStorage = ref<String>( '' );
    // 支持的存储平台
    const fileStorageList = ref<Array<StorageDictVo>>( [] );

    /** 加载文件存储类型 */
    const loadFileStorageList = async () => {
        if (fileStorageList.value.length > 0) {
            return Promise.resolve();
        }
        const [err, res] = await to( useStorageApi().getStorageTypes() );
        if ( res ) {
            const list = res.data;
            fileStorageList.value = list;
            if ( fileStorage.value === '' ) {
                fileStorage.value = list[0].key
            }
            return Promise.resolve();
        }
        return Promise.reject( err );
    }

    return {
        fileStorage,
        fileStorageList,
        loadFileStorageList
    }
}, {
    persist: true,
}, )

export default useFsConfig;

export function useFsConfigHook() {
    return useFsConfig( store );
}