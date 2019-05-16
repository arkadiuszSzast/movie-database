package com.movie.database.movie_database.config;

import com.movie.database.movie_database.config.security.jwt.AccessTokenProperties;
import com.movie.database.movie_database.config.security.jwt.RefreshTokenProperties;
import com.movie.database.movie_database.config.security.cors.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CorsProperties.class, AccessTokenProperties.class, RefreshTokenProperties.class})
public class ApplicationPropertiesConfig {
}
