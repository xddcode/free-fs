import excelSvg from '/@/assets/svg/fti/excel.svg';
import folderSvg from '/@/assets/svg/fti/folder.svg';
import imgSvg from '/@/assets/svg/fti/img.svg';
import linkSvg from '/@/assets/svg/fti/link.svg';
import mp3Svg from '/@/assets/svg/fti/mp3.svg';
import mp4Svg from '/@/assets/svg/fti/mp4.svg';
import noteSvg from '/@/assets/svg/fti/note.svg';
import pdfSvg from '/@/assets/svg/fti/pdf.svg';
import pptSvg from '/@/assets/svg/fti/ppt.svg';
import unknownSvg from '/@/assets/svg/fti/unknown.svg';
import wordSvg from '/@/assets/svg/fti/word.svg';
import zipSvg from '/@/assets/svg/fti/zip.svg';

/**
 * 获取类型文件图片
 * @param type
 */
export const getAssetsFile = (type: string) => {
    return new URL(`../assets/imgs/fti/${type}.png`, import.meta.url).href;
}

/**
 * 获取文件类型
 * @param type
 */
export const getFileSvg = (type: string) => {
    switch (type) {
        case 'jpg':
        case 'jpeg':
        case 'png':
        case 'gif':
            return imgSvg;
        case 'doc':
        case 'docx':
            return wordSvg;
        case 'xls':
        case 'xlsx':
            return excelSvg;
        case 'pdf':
            return pdfSvg;
        case 'dir':
            return folderSvg;
        case 'mp3':
            return mp3Svg;
        case 'mp4':
            return mp4Svg;
        case 'txt':
            return noteSvg;
        case 'ppt':
            return pptSvg;
        case 'zip':
            return zipSvg;
        default:
            return unknownSvg;
    }
}