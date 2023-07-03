package com.free.fs;

import com.free.fs.common.properties.FsServerProperties;
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
@EnableConfigurationProperties({FsServerProperties.class})
public class FreeFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeFsApplication.class, args);
        System.out.println("   _____ _    _  _____ _____ ______  _____ _____ \n" +
                "  / ____| |  | |/ ____/ ____|  ____|/ ____/ ____|\n" +
                " | (___ | |  | | |   | |    | |__  | (___| (___  \n" +
                "  \\___ \\| |  | | |   | |    |  __|  \\___ \\\\___ \\ \n" +
                "  ____) | |__| | |___| |____| |____ ____) |___) |\n" +
                " |_____/ \\____/ \\_____\\_____|______|_____/_____/ \n" +
                "                                                 \n");
    }

}
