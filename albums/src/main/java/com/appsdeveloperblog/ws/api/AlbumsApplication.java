package com.appsdeveloperblog.ws.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AlbumsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumsApplication.class, args);
    }
}
