export interface FileVO {
    /**
     * 主键
     */
    id: string | number;
    /**
     * 文件名
     */
    name: string;
    /**
     * 文件类型
     */
    type: string;
    /**
     * 文件地址
     */
    url: string;
    /**
     * 文件uuid后的新名
     */
    fileName: string;
    /**
     * 是否图片
     */
    isImg: boolean;
    /**
     * 是否文件夹
     */
    isDir: boolean;
    /**
     * 上传时间
     */
    putTime: string;
    /**
     * 父id
     */
    parentId: string | number;
}

/**
 * 目录
 */
export interface DirVo {

}