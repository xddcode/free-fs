package com.free.fs.common.utils;

import com.free.fs.common.constant.CommonConstant;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 文件工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/6/7 11:12
 */
public class FileUtil {
    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 合法的后缀名，后续扩展
     */
    public static String[] ALLOWED_FILE_SUFFIX = new String[]{
            "png", "bmp", "jpg", "jpeg", "pdf",
            "xlsx", "xls", "gif", "svg", "txt",
            "zip", "ppt", "doc", "docx", "html",
            "htm", "ico", "mp3", "mp4", "java",
            "sql", "xml", "js", "py", "php", "vue",
            "sh", "cmd", "py3", "css", "md", "csv",
            "rar", "zip", "json"
    };

    public static String[] FILE_SUFFIX_IMAGE = new String[]{"png", "bmp", "jpg", "jpeg", "svg", "gif"};

    public static String[] FILE_SUFFIX_CODE = new String[]{"java", "sql", "js", "py", "py3", "php", "vue", "sh", "cmd", "css"};

    public static boolean isFileAllowed(String fileName) {
        for (String ext : ALLOWED_FILE_SUFFIX) {
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

    /**
     * 是否为图片文件
     *
     * @param suffix
     * @return
     */
    public static boolean isImg(String suffix) {
        for (String ext : FILE_SUFFIX_IMAGE) {
            if (ext.equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是代码文件
     *
     * @param suffix
     * @return
     */
    public static boolean isCode(String suffix) {
        for (String ext : FILE_SUFFIX_CODE) {
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
        if (filenameExtension.equalsIgnoreCase(".jpeg") ||
            filenameExtension.equalsIgnoreCase(".jpg") ||
            filenameExtension.equalsIgnoreCase(".png")) {
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
        if (filenameExtension.equalsIgnoreCase(".pptx") ||
            filenameExtension.equalsIgnoreCase(".ppt")) {
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
     * 获取文件名
     *
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(CommonConstant.SUFFIX_SPLIT));
    }

    /**
     * 获取文件后缀名
     *
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(CommonConstant.SUFFIX_SPLIT) + 1).toLowerCase();
    }

    public static void downLoad(String url, String path, HttpServletResponse response) {
        InputStream in = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(200000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            in = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len = 0;
            response.reset();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/octet-stream");
            String fileName = url.replaceAll(path + "/", "");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(url + "下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
