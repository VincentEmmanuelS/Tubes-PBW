package com.tubes.pbw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// @SpringBootApplication
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PbwApplication {

	public static void main(String[] args) {
		SpringApplication.run(PbwApplication.class, args);
	}

}
