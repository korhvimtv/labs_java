package com.example.glibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.glibrary.repository")
@EntityScan("com.example.glibrary.model")
public class GlibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlibraryApplication.class, args);
    }

}
