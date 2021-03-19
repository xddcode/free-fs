package com.free.fs;

import com.free.fs.common.properties.FsServerProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * free-fs
 *
 * @author dinghao
 * @date 2021/3/16
 */
@SpringBootApplication
@EnableConfigurationProperties(FsServerProperties.class)
public class FreeFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeFsApplication.class, args);
    }

}
