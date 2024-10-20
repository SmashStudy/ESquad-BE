package com.esquad.esquadbe.global.config;

import com.esquad.esquadbe.chat.dto.FirebaseProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    private final FirebaseProperties firebaseProperties;

    public FirebaseConfig(FirebaseProperties firebaseProperties) {
        this.firebaseProperties = firebaseProperties;
    }

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        String json = String.format("{\"type\":\"%s\",\"project_id\":\"%s\",\"private_key_id\":\"%s\",\"private_key\":\"%s\",\"client_email\":\"%s\",\"client_id\":\"%s\",\"auth_uri\":\"%s\",\"token_uri\":\"%s\",\"auth_provider_x509_cert_url\":\"%s\",\"client_x509_cert_url\":\"%s\",\"universe_domain\":\"%s\",\"database_url\":\"%s\"}",
                firebaseProperties.getType(),
                firebaseProperties.getProjectId(),
                firebaseProperties.getPrivateKeyId(),
                firebaseProperties.getPrivateKey().replaceAll("\\n", "\\\\n"),  // 새로운 줄 문자를 이스케이프합니다.
                firebaseProperties.getClientEmail(),
                firebaseProperties.getClientId(),
                firebaseProperties.getAuthUri(),
                firebaseProperties.getTokenUri(),
                firebaseProperties.getAuthProviderX509CertUrl(),
                firebaseProperties.getClientX509CertUrl(),
                firebaseProperties.getUniverseDomain(),
                firebaseProperties.getDatabaseUrl());

        InputStream serviceAccount = new ByteArrayInputStream(json.getBytes());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseProperties.getDatabaseUrl())
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseDatabase firebaseDatabase(FirebaseApp firebaseApp) {
        log.info("--------------------> Firebase initialized successfully.");
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}

