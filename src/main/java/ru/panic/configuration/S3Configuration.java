package ru.panic.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.panic.property.AwsS3Property;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Configuration class for AWS S3 client setup.
 */
@Configuration
@RequiredArgsConstructor
public class S3Configuration {

    private final AwsS3Property awsS3Property;

    /**
     * Creates and configures an S3 client bean.
     *
     * @return the configured S3Client instance
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsS3Property.region()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awsS3Property.accessKey(),
                                awsS3Property.secretKey()
                        )
                ))
                .endpointOverride(URI.create(awsS3Property.endpoint()))
                .serviceConfiguration(software.amazon.awssdk.services.s3.S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

}
