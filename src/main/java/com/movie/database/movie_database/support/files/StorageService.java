package com.movie.database.movie_database.support.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface StorageService {

    void save(MultipartFile file, UUID userId) throws IOException;
}
