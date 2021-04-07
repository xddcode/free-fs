package com.free.fs.common.utils;

import com.free.fs.common.constant.CommonConstant;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.model.FilePojo;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

/**
 * 上传类型判断工具类
 *
 * @author dinghao
 * @date 2021/3/10
 */
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

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

    public static String getcontentType(String filenameExtension) {
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
        if (filenameExtension.equalsIgnoreCase(".docx"))
        {
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
    public String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static FilePojo buildFilePojo(MultipartFile file) {
        //判断文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        //判断文件后缀名是否合法
        int dotPos = file.getOriginalFilename().lastIndexOf(CommonConstant.SUFFIX_SPLIT);
        if (dotPos < 0) {
            throw new BusinessException("文件名称不合法");
        }
        //获取文件大小
        Long size = file.getSize();
        //文件名
        String orgName = file.getOriginalFilename();
        String name = orgName.substring(0, orgName.lastIndexOf(CommonConstant.SUFFIX_SPLIT));
        //文件后缀名
        String fileExt = orgName.substring(dotPos + 1).toLowerCase();
        // 判断是否是合法的文件后缀
        if (!FileUtil.isFileAllowed(fileExt)) {
            throw new BusinessException("文类类型不符合要求");
        }
        String type = "";
        if (FileUtil.isCode(fileExt)) {
            type = "code";
        } else {
            type = fileExt;
        }
        //生成新的文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        FilePojo pojo = new FilePojo();
        pojo.setSuffix(fileExt);
        pojo.setSize(size);
        pojo.setFileName(fileName);
        pojo.setName(name);
        pojo.setIsImg(FileUtil.isImg(fileExt));
        pojo.setIsDir(Boolean.FALSE);
        pojo.setPutTime(new Date());
        pojo.setType(type);
        return pojo;
    }

    public static void downLoad(String url,String path, HttpServletResponse response) {
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
