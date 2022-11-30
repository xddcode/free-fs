package com.free.fs.common.utils;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 自定义线程池，支持父子线程传值
 *
 * @author hao.ding@insentek.com
 * @date 2022-05-12 16:33
 */
public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private static final long serialVersionUID = -5887035957049288777L;

    @Override
    public void execute(Runnable runnable) {
        Runnable ttlRunnable = TtlRunnable.get(runnable);
        super.execute(ttlRunnable);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Callable ttlCallable = TtlCallable.get(task);
        return super.submit(ttlCallable);
    }

    @Override
    public Future<?> submit(Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submit(ttlRunnable);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submitListenable(ttlRunnable);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        Callable ttlCallable = TtlCallable.get(task);
        return super.submitListenable(ttlCallable);
    }
}
