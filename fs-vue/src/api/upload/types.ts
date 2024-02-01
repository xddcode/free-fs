import type { UploadFile } from "element-plus";

export interface UploadFileVo extends UploadFile{
    /** 文件类型 */
    type: string;
    /** 存储方式 */
    channel: string;
}