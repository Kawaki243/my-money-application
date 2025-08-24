package com.project.moneymanagerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneymanagerbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneymanagerbackendApplication.class, args);
    }

}
