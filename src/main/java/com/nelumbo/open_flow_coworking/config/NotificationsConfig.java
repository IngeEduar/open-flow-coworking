package com.nelumbo.open_flow_coworking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.application.notifications")
public class NotificationsConfig {

    private String url;
    private List<HeaderConfig> extraHeaders;

    @Getter
    @Setter
    public static class HeaderConfig {
        private String name;
        private String value;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
