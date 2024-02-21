import request from '/@/utils/request';
import { AxiosPromise } from "axios";
import { StoragePlatformVO } from "/@/api/storage/types";

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
    }
}