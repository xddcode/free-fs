import request from '/@/utils/request';
import { AxiosPromise } from "axios";
import { FileVO } from "/@/api/files/types";

/**
 * 文件服务api
 */
export function useFilesApi() {
    return {
        // 文件列表
        fileList: ( data ): AxiosPromise<FileVO[]> => {
            return request( {
                url: '/file/list',
                method: 'get',
                data
            } )
        },
    }
}