package com.finalproject.sulbao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SulbaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SulbaoApplication.class, args);
    }

}
