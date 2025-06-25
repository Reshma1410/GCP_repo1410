package org.example.config;

import com.google.cloud.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GcpStorageConfig {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.credentials.path}")
    private String credentialsPath;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }

}
