package com.example.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.auth.service.AuthService;
import com.example.auth.util.AuthUserRequest;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseBody
    public Object authenticateUser(@RequestBody AuthUserRequest authUserRequest) {
        logger.debug("Authenticating user : " + authUserRequest.getUserName());
        return authService.getAuthToken(authUserRequest.getUserName(), authUserRequest.getPassword());
    }

}
