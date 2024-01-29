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
}