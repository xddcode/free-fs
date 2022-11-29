package com.free.fs.component.cache.heartbeat;

import com.free.fs.component.cache.template.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Redis心跳检查，解决长时间不操作，redis客户端主动被断开的问题
 *
 * @author hao.ding@insentek.com
 * @date 2022-11-29 10:57
 */
@RequiredArgsConstructor
public class RedisTimer {

    private final RedisRepository redisRepository;

    @Scheduled(cron = "0/10 * * * * *")
    public void timer() {
        redisRepository.get("heartbeat");
    }
}
