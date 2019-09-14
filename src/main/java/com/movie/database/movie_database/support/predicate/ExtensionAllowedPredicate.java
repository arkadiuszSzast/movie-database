package com.movie.database.movie_database.support.predicate;

import com.movie.database.movie_database.support.properties.FileStorageProperties;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class ExtensionAllowedPredicate<String> implements Predicate<String> {

    private final FileStorageProperties fileStorageProperties;

    public ExtensionAllowedPredicate(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public boolean test(String value) {
        return fileStorageProperties.getAllowedExtensions().stream().anyMatch(value::equals);
    }
}
