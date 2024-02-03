import {defineStore} from 'pinia';
import to from "await-to-js";
import store from "/@/stores";
import {useStorageApi} from "/@/api/storage";
import {StorageDictVo} from "/@/api/storage/types";

// 支持文件存储的类型
export declare type fileStorageTypes = 'Local' | 'AliyunOSS' | 'Minio' | 'Qiniu';
export const useFsConfig = defineStore('fsConfig', () => {
    // 当前使用的存储平台
    const fileStorage = ref<String>('');
    // 支持的存储平台
    const fileStorageList = ref<Array<StorageDictVo>>([]);

    /** 加载文件存储类型 */
    const loadFileStorageList = async () => {
        const [ err, res ] = await to(useStorageApi().getStorageTypes());
        if (res) {
            fileStorageList.value = res.data;
            return Promise.resolve();
        }
        return Promise.reject(err);
    }

    /** 切换平台时, 校验是否已配置 */


    return {
        fileStorage,
        fileStorageList,
        loadFileStorageList
    }
}, {
    persist: true,
},)

export default useFsConfig;
export function useFsConfigHook() {
    return useFsConfig(store);
}