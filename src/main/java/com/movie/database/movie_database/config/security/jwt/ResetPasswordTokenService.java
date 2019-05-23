package com.movie.database.movie_database.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.support.properties.ResetPasswordTokenProperties;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenService {

    private final ResetPasswordTokenProperties resetPasswordTokenProperties;

    public ResetPasswordTokenService(ResetPasswordTokenProperties resetPasswordTokenProperties) {
        this.resetPasswordTokenProperties = resetPasswordTokenProperties;
    }

    public void validate(String token) {
        JWT.require(Algorithm.HMAC256(resetPasswordTokenProperties.getSecret())).build()
                .verify(token).getSubject();
    }
}
