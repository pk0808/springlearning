package com.example.auth.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.auth.entity.User;
import com.example.auth.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class config {

    private static final Logger logger = LoggerFactory.getLogger(config.class);
    
    @Bean
    CommandLineRunner runner(AuthService authService) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<User>> typeReference = new TypeReference<>() {
            };
            InputStream inputStream = TypeReference.class.getResourceAsStream("/static/user.json");
            if (inputStream == null) {
                throw new FileNotFoundException("Cannot find file 'user.json' in resources");
            }
            try {
                List<User> users = mapper.readValue(inputStream,typeReference);
                for (User user : users) {
                    authService.createUser(user);
                }
                logger.info("Data successfully inserted into Database");
            } catch (IOException e){
                logger.info("Unable to save items: " + e.getMessage());
            }
        };
    }



	
	
}
