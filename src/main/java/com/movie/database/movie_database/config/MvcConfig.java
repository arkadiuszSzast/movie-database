package com.movie.database.movie_database.config;

import com.movie.database.movie_database.config.security.WebSecurity;
import com.movie.database.movie_database.support.properties.FileStorageProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final FileStorageProperties fileStorageProperties;

    public MvcConfig(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(WebSecurity.AVATARS_URL)
                .addResourceLocations("file:" + fileStorageProperties.getLocation());
    }
}
