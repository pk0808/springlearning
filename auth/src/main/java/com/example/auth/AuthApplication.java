package com.example.auth;

import com.example.auth.filter.AuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	public AuthorizationFilter authorizationFilter() {
		return new AuthorizationFilter();
	}

}
