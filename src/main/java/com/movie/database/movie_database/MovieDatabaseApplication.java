package com.movie.database.movie_database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class MovieDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieDatabaseApplication.class, args);
    }

}
