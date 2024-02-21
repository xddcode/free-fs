import request from '/@/utils/request';
import { AxiosPromise } from "axios";
import { StoragePlatformVO, StorageSettingForm } from "/@/api/storage/types";

/**
 * 存储平台api
 */
export function useStorageApi() {
    return {
        getStorageTypes: () => {
            return request({
                url: '/storage/types',
                method: 'get',
            });
        },
        checkStorageConfig: (identifier: string) => {
            return request({
                url: '/storage/setting/check/' + identifier,
                method: 'get',
            })
        },
        getStoragePlatformsConfig: ():  AxiosPromise<StoragePlatformVO[]> => {
            return request({
                url: '/storage/platforms',
                method: 'get',
            })
        },
        getStorageSetting: (identifier: string) => {
            return request({
                url: '/storage/setting/' + identifier,
                method: 'get'
            })
        },
        saveStorageSetting: (data?: StorageSettingForm) => {
            return request({
                url: '/storage/setting',
                method: 'post',
                data
            })
        },
        toggleSettingStatus: (identifier: string) => {
            return request({
                url: '/storage/setting/' + identifier,
                method: 'put'
            })
        }
    }
}