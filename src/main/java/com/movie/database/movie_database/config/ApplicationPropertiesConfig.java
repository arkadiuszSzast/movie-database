package com.movie.database.movie_database.config;

import com.movie.database.movie_database.config.security.jwt.ConfirmationTokenProperties;
import com.movie.database.movie_database.support.properties.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CorsProperties.class, AccessTokenProperties.class, RefreshTokenProperties.class,
        SendGridTemplatesProperties.class, ConfirmationTokenProperties.class, MovieDbProperties.class})
public class ApplicationPropertiesConfig {
}
