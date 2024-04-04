package com.example.chatapptrpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan(basePackages = "com.example.chatapptrpo.*")
@EnableJpaRepositories(basePackages = "com.example.chatapptrpo.*")
@EnableAsync
public class ChatAppTrpoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAppTrpoApplication.class, args);
    }

}
