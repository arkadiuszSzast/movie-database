package com.movie.database.movie_database.user.avatar;

import com.movie.database.movie_database.support.files.StorageService;
import com.movie.database.movie_database.support.properties.FileStorageProperties;
import com.movie.database.movie_database.user.ApplicationUserUpdateService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class UserAvatarService implements StorageService {

    private final Logger log = LoggerFactory.getLogger(UserAvatarService.class);
    private final FileStorageProperties fileStorageProperties;
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserUpdateService applicationUserUpdateService;

    public UserAvatarService(FileStorageProperties fileStorageProperties, ApplicationUserRepository applicationUserRepository,
                             ApplicationUserUpdateService applicationUserUpdateService) {
        this.fileStorageProperties = fileStorageProperties;
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserUpdateService = applicationUserUpdateService;
    }

    @Override
    public void save(MultipartFile file, UUID userId) throws IOException {
        var user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        var path = getPathToAvatar();
        removeOldAvatar(user);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        user.setAvatarUrl(path.getFileName().toString());
        applicationUserUpdateService.update(user);
    }

    private Path getPathToAvatar() {
        var location = fileStorageProperties.getLocation();
        var filename = UUID.randomUUID().toString();
        return Path.of(location + filename);
    }

    private void removeOldAvatar(ApplicationUser user) {
        user.getAvatarUrl().ifPresent(removeOldAvatar());
    }

    private Consumer<String> removeOldAvatar() {
        return filename -> {
            try {
                Files.deleteIfExists(Path.of(fileStorageProperties.getLocation() + filename));
            } catch (IOException e) {
                log.error("Failed to remove old avatar, uri: {}", filename);
            }
        };
    }
}
