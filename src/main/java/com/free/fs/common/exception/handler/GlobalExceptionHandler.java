package com.free.fs.common.exception.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.NotSafeException;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.common.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/6/29 17:27
 */
@Slf4j
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理token异常
     */
    @ExceptionHandler(NotLoginException.class)
    public R<?> handleSaTokenException(NotLoginException nle) {
        // 判断场景值，定制化异常信息
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        } else {
            message = "当前会话未登录";
        }
        return R.failed(401, message);
    }

    @ExceptionHandler
    public R<?> handlerNotRoleException(NotRoleException e) {
        return R.failed(403, "无此角色：" + e.getRole());
    }

    @ExceptionHandler
    public R<?> handlerNotPermissionException(NotPermissionException e) {
        return R.failed(403, "您无此权限做此操作！");
    }

    @ExceptionHandler
    public R<?> handlerNotSafeException(NotSafeException e) {
        return R.failed(401, "二级认证异常：" + e.getMessage());
    }

    /**
     * IllegalArgumentException 异常处理返回json
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public R<?> badRequestException(IllegalArgumentException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        return defHandler("unknown.error", e);
    }

    /**
     * 统一返回
     */
    private R<?> defHandler(String msg, Exception e) {
        log.error(msg, e);
        return R.failed(msg);
    }
}
