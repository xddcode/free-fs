package com.free.fs.common.utils;

import cn.hutool.core.io.IoUtil;
import com.free.fs.common.constant.CommonConstant;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/22 13:51
 */
public class ResponseUtil {

    /**
     * 导出文件流
     *
     * @param is
     * @param objectName
     * @param response
     * @throws IOException
     */
    public static void write(InputStream is, String objectName, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("application/x-msdownload");
        response.setCharacterEncoding(CommonConstant.UTF_8);
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(objectName, StandardCharsets.UTF_8));
        IoUtil.copy(is, outputStream);
        response.flushBuffer();
        IoUtil.close(outputStream);
        IoUtil.close(is);
    }
}
