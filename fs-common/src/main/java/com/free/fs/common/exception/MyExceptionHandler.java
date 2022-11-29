package com.free.fs.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> errorHandler(Exception ex, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // 根据不同错误获取错误信息
        if (ex instanceof IException) {
            map.put("code", ((IException) ex).getCode());
            map.put("msg", ex.getMessage());
        } else if (ex instanceof UnauthorizedException) {
            //sendRedirect("/error/403", request, response);
            map.put("code", 403);
            map.put("msg", "您没有访问此接口的权限");
        } else {
            String message = ex.getMessage();
            map.put("code", 500);
            map.put("msg", message == null || message.trim().isEmpty() ? "未知错误" : message);
            log.error(message, ex);
            ex.printStackTrace();
        }
        // 支持跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");
        return map;
    }

}
