package com.example.userpostapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry // Enable retry capabilities for fault tolerance
@SpringBootApplication
public class UserPostApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserPostApplication.class, args);
	}

}
