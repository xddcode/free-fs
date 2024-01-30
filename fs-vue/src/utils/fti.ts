/**
 * 获取类型文件图片
 * @param type
 */
export const getAssetsFile = (type: string) => {
    return new URL(`../assets/imgs/fti/${type}.png`, import.meta.url).href;
}