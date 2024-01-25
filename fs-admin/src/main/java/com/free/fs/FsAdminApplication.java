package com.free.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
