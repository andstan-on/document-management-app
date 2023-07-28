package com.springframework.documentmanagementapp;

import com.springframework.documentmanagementapp.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class DocumentManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagementAppApplication.class, args);
	}

}
