package com.pullm.backendmonolit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@SpringBootApplication
@EnableCaching
public class BackendMonolitApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendMonolitApplication.class, args);
    }

}
