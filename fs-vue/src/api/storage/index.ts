import request from '/@/utils/request';

/**
 * 存储平台api
 */
export function useStorageApi() {
    return {
        getStorageTypes() {
            return request({
                url: '/storage/types',
                method: 'get',
            });
        }
    }
}