package com.example.ivf;

import com.example.ivf.config.ExternalIntegrationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExternalIntegrationProperties.class)
public class IVFApplication {

    public static void main(String[] args) {
        SpringApplication.run(IVFApplication.class, args);
    }
}
