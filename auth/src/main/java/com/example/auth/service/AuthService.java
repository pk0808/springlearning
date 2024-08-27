package com.example.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.auth.entity.User;
import com.example.auth.entity.UserRepository;
import com.example.auth.util.AuthUserResponse;
import com.example.auth.util.AuthConstants;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.expiration}")
    private int jwtExpiry = 420000;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
        this.verifier = JWT.require(algorithm)
                .withIssuer(jwtIssuer)
                .build();
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Transactional
    public void createUser(User user) {
        user.setPassword(encryptPassword(user.getPassword()));
        userRepository.save(user);
        logger.trace("User inserted: {}", user);
    }

    public AuthUserResponse getAuthToken(String userName, String password) {
        if (authenticateUser(userName, password)) {
            logger.info("User Authenticated");
            String token = JWT.create()
                    .withIssuer(jwtIssuer)
                    .withClaim("username", userName)
                    .withIssuedAt(new java.util.Date())
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + jwtExpiry))
                    .sign(algorithm);
            return new AuthUserResponse(userName, token);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthConstants.invalidUserErrMsg);
        }
    }

    private boolean authenticateUser(String userName, String password) {
        User res = userRepository.findByUserName(userName);
        if (res != null && BCrypt.checkpw(password, res.getPassword())) {
            return true;
        } else {
            logger.info("User {} is not authenticated", userName);
            return false;
        }
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean isTokenValid(String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
        if (token == null) {
            return false;
        }
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}