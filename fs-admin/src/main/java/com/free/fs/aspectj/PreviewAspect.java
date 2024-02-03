package com.free.fs.aspectj;

import cn.dev33.satoken.stp.StpUtil;
import com.free.fs.common.annotation.Preview;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.service.FileService;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.free.fs.domain.table.FileInfoTableDef.FILE_INFO;

/**
 * 演示环境拦截器
 *
 * @author dinghao
 * @date 2021/3/16
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PreviewAspect {

    private final FileService fileService;

    @Before("@annotation(preview)")
    public void doBefore(JoinPoint point, Preview preview) {
        Long id = StpUtil.getLoginIdAsLong();
        long fileCount = fileService.count(new QueryWrapper()
                .where(FILE_INFO.USER_ID.eq(id))
                .and(FILE_INFO.IS_DIR.eq(0)));
        if (fileCount >= preview.count()) {
            throw new BusinessException("演示环境限制每个用户最多上传" + preview.count() + "个文件");
        }
    }
}
