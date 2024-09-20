package com.esquad.esquadbe.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
public class ChatAmazonS3config {

    @Value("${cloud.aws.credentials.chat_accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.chat_secretKey}")
    private String secretKey;
    @Value("${cloud.aws.chat_region.static}")
    private String region;

    @Bean (name = "chatAwsCredentialsProvider")
    @Primary
    public AwsCredentialsProvider awsCredentialsProvider() {
        return () -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessKey;
            }

            @Override
            public String secretAccessKey() {
                return secretKey;
            }
        };
    }

    @Bean
    @Primary
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider())
                .region(awsRegionProvider().getRegion())
                .build();
    }

    @Bean
    public AwsRegionProvider awsRegionProvider() {
        return () -> Region.of(region);
    }
}
