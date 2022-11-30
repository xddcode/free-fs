package com.free.fs.common.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 文件工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2022-06-23 15:02:04
 */
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] IMAGE_FILE_EXTD = new String[] {"png", "bmp", "jpg", "jpeg", "pdf", "xlsx", "xls", "gif",
        "svg", "txt", "zip", "ppt", "doc", "docx", "html", "htm", "ico", "mp3", "mp4", "java", "sql", "xml", "js", "py",
        "php", "vue", "sh", "cmd", "py3", "css"};
    public static String[] IMAGE_FILE_EXTD1 = new String[] {"png", "bmp", "jpg", "jpeg", "svg", "gif"};

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

    public static String getContentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        if (filenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase(".jpeg") || filenameExtension.equalsIgnoreCase(".jpg")
            || filenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (filenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase(".pptx") || filenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase(".docx")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }

    /**
     * 获取文件后缀名
     *
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
