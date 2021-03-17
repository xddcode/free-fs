package com.free.fs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * free-fs
 *
 * @author dinghao
 * @date 2021/3/16
 */
@SpringBootApplication
@MapperScan("com.free.fs.mapper")
public class FreeFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeFsApplication.class, args);
    }

}
