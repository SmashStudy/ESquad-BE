package com.esquad.esquadbe.streaming.config;

import com.esquad.esquadbe.streaming.rtc.KurentoHandler;
import com.esquad.esquadbe.streaming.service.KurentoManager;
import com.esquad.esquadbe.streaming.service.KurentoUserRegistry;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;

@Configuration
public class WebRtcConfig {

    @Value("${kms.url}")
    private String kmsUrl;

    private final KurentoUserRegistry registry;
    private final KurentoManager roomManager;

    @Autowired
    public WebRtcConfig(KurentoUserRegistry registry, KurentoManager roomManager) {
        this.registry = registry;
        this.roomManager = roomManager;
    }

    @Bean
    public KurentoHandler kurentoHandler(){
        return new KurentoHandler(registry, roomManager, kurentoClient());
    }

    @Bean
    public KurentoClient kurentoClient() {
        disableSslVerification();
        return KurentoClient.create(kmsUrl);
    }

    private void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

