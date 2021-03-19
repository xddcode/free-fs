package com.free.fs.common.utils;

/**
 * 上传类型判断工具类
 *
 * @author dinghao
 * @date 2021/3/10
 */
public class FileUtil {

    public static String[] IMAGE_FILE_EXTD = new String[]{
            "png", "bmp", "jpg", "jpeg", "pdf",
            "xlsx", "xls", "gif", "svg", "txt",
            "zip", "ppt", "doc", "docx", "html",
            "htm", "ico", "mp3", "mp4", "java",
            "sql", "xml","js","py","php","vue",
            "sh","cmd","py3","css"
    };
    public static String[] IMAGE_FILE_EXTD1 = new String[]{"png", "bmp", "jpg", "jpeg", "svg", "gif"};

    public static String[] IMAGE_FILE_CODE = new String[]{"java", "sql", "js", "py", "py3", "php", "vue", "sh", "cmd","css"};

    public static boolean isFileAllowed(String fileName) {
        for (String ext : IMAGE_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFileAllowed(String fileName, String[] file) {
        for (String ext : file) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isImg(String suffix) {
        for (String ext : IMAGE_FILE_EXTD1) {
            if (ext.equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCode(String suffix) {
        for (String ext : IMAGE_FILE_CODE) {
            if (ext.equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件后缀名
     *
     * @return
     */
    public String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
