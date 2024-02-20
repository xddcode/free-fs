import request from '/@/utils/request';
import { AxiosPromise } from "axios";
import { FileForm, FileQuery, FileVO } from "/@/api/files/types";

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
        addFolder: ( data?: FileForm ) => {
            return request( {
                url: '/folder',
                method: 'post',
                data
            } )
        },
        // 修改文件名称
        updateFileName: ( data?: FileForm ) => {
            return request( {
                url: '/file/rename',
                method: 'put',
                data
            } )
        },
        // 获取目录层级
        getLevelFolders: ( id ) => {
            return request( {
                url: '/folder/level/' + id,
                method: 'get'
            } )
        },
        // 删除文件
        deleteFile: ( ids?: string | number | Array<string | number> ) => {
            return request({
                url: '/file/' + ids,
                method: 'delete'
            })
        }
    }
}