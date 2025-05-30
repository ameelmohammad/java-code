package com.firstproject.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.firstproject.connection")
public class ConnectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectionApplication.class, args);
	}

}
