package com.example.glibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories("com.example.glibrary.repository")
@EntityScan("com.example.glibrary.model")
@EnableAsync
public class GlibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlibraryApplication.class, args);
    }

}
