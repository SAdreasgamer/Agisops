package com.aegisops.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        excludeName = {
                "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
                "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
                "org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration"
        }
)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
