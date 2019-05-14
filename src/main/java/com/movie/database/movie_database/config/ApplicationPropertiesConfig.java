package com.movie.database.movie_database.config;

import com.movie.database.movie_database.config.security.JWTProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JWTProperties.class)
public class ApplicationPropertiesConfig {
}
