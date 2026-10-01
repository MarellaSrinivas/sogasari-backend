package com.sogasari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.sogasari.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        FileStorageProperties.class
)
public class SogasariBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SogasariBackendApplication.class, args);
	}

}
