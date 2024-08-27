package com.finalproject.sulbao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SulbaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SulbaoApplication.class, args);
    }

}
