import request from '/@/utils/request';
import { AxiosPromise } from "axios";
import { FileQuery, FileVO } from "/@/api/files/types";

/**
 * 文件服务api
 */
export function useFilesApi() {
    return {
        // 文件列表
        fileList: ( query?: FileQuery ): AxiosPromise<FileVO[]> => {
            return request( {
                url: '/file/list',
                method: 'get',
                params: query
            } )
        },
        // 新增文件夹
        addFolder: ( data ) => {
            return request({
                url: '/folder',
                method: 'post',
                data
            })
        },
        // 修改文件名称
        updateFileName: () => {
            return request({

            })
        },
        getLevelFolders: (id) => {
            return request({
                url: '/folder/level/' + id,
                method: 'get'
            })
        }
    }
}