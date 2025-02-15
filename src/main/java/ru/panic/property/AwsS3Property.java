package ru.panic.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record AwsS3Property(String region, String accessKey, String secretKey, String endpoint) {
}
