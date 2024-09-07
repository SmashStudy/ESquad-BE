package com.esquad.esquadbe.studypage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "naver.client")
@Getter
@Setter
public class BookApi {
    private String id;
    private String secret;
}
