package com.ala.ala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AlaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlaApplication.class, args);
    }

}
