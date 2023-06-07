package com.pullm.backendmonolit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BackendMonolitApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendMonolitApplication.class, args);
  }

}
