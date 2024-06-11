package com.free.fs.aspectj;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.free.fs.common.annotation.Preview;
import com.free.fs.common.exception.BusinessException;
import com.free.fs.core.service.FileService;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.free.fs.core.domain.table.FileInfoTableDef.FILE_INFO;


/**
 * 演示环境拦截器
 *
 * @author dinghao
 * @date 2024/3/16
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PreviewAspect {

    private final static String demoProfile = "demo";

    private final FileService fileService;

    @Before("@annotation(preview)")
    public void doBefore(JoinPoint point, Preview preview) {
        if (demoProfile.equals(SpringUtil.getActiveProfile())) {
            Long id = StpUtil.getLoginIdAsLong();
            long fileCount = fileService.count(new QueryWrapper()
                    .where(FILE_INFO.USER_ID.eq(id))
                    .and(FILE_INFO.IS_DIR.eq(0)));
            if (fileCount >= preview.count()) {
                throw new BusinessException("演示环境限制每个用户最多上传" + preview.count() + "个文件");
            }
        }
    }
}
