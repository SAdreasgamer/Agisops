package com.aegisops.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.aegisops.ingestion",
                "com.aegisops.common.kafka"
        },
        excludeName = {
                "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
                "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
                "org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration"
        }
)
public class IngestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(IngestionApplication.class, args);
    }
}
