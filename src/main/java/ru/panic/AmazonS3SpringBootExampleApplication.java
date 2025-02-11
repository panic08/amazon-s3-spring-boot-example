package ru.panic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AmazonS3SpringBootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmazonS3SpringBootExampleApplication.class, args);
	}

}
