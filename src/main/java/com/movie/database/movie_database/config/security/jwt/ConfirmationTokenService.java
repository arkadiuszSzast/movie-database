package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenProperties confirmationTokenProperties;

    public ConfirmationTokenService(ConfirmationTokenProperties confirmationTokenProperties) {
        this.confirmationTokenProperties = confirmationTokenProperties;
    }

    public void validate(String token) {
        JWT.require(Algorithm.HMAC256(confirmationTokenProperties.getSecret())).build()
                .verify(token).getSubject();
    }
}
