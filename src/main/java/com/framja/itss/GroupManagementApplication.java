package com.framja.itss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.framja.itss")
public class GroupManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupManagementApplication.class, args);
	}

}
