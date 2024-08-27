package com.example.auth.filter;

import com.example.auth.util.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import com.example.auth.service.AuthService;

import java.nio.charset.StandardCharsets;

@Component
public class AuthorizationFilter implements WebFilter {

    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        String requestPath = exchange.getRequest().getPath().value();

        if (isRequestAllowed(requestPath, authHeader)) {
            return chain.filter(exchange);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, AuthConstants.authHeaderMismatch);
        }
    }

    private boolean isRequestAllowed(String requestPath, String authHeader) {
        if (requestPath.contains("/auth/")) {
            return true;
        }
        if (authHeader != null && requestPath.contains("/profiles/")) {
            return authService.isTokenValid(authHeader);
        }
        return false;
    }

}