package com.knayam.instagramapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class InstagramApiApplication {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(InstagramApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(InstagramApiApplication.class, args);
	}

}
