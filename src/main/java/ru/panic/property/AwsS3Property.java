package ru.panic.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for AWS S3.
 * Maps properties with the prefix "aws.s3" from the application's configuration.
 */
@ConfigurationProperties(prefix = "aws.s3")
public record AwsS3Property(String region, String accessKey, String secretKey, String endpoint, Boolean pathStyleAccessEnabled) {
}
