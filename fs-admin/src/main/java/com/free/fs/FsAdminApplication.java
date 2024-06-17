package com.free.fs;

import com.free.fs.common.properties.FsServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ FsServerProperties.class })
@SpringBootApplication
public class FsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FsAdminApplication.class, args);
        System.out.println("   _____ _    _  _____ _____ ______  _____ _____ \n" +
                           "  / ____| |  | |/ ____/ ____|  ____|/ ____/ ____|\n" +
                           " | (___ | |  | | |   | |    | |__  | (___| (___  \n" +
                           "  \\___ \\| |  | | |   | |    |  __|  \\___ \\\\___ \\ \n" +
                           "  ____) | |__| | |___| |____| |____ ____) |___) |\n" +
                           " |_____/ \\____/ \\_____\\_____|______|_____/_____/ \n" +
                           "                                                 \n");
    }
}
