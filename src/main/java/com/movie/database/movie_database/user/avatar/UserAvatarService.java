package com.movie.database.movie_database.user.avatar;

import com.movie.database.movie_database.support.files.StorageService;
import com.movie.database.movie_database.support.properties.FileStorageProperties;
import com.movie.database.movie_database.user.ApplicationUserUpdateService;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.exception.ApplicationUserNotFoundException;
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

    private final FileStorageProperties fileStorageProperties;
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserUpdateService applicationUserUpdateService;

    public UserAvatarService(FileStorageProperties fileStorageProperties, ApplicationUserRepository applicationUserRepository, ApplicationUserUpdateService applicationUserUpdateService) {
        this.fileStorageProperties = fileStorageProperties;
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserUpdateService = applicationUserUpdateService;
    }

    @Override
    public void save(MultipartFile file, UUID userId) throws IOException {
        var user = applicationUserRepository.findById(userId).orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        var location = fileStorageProperties.getLocation();
        var filename = UUID.randomUUID().toString();
        var path = Path.of(location + filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        user.getAvatarUrl().ifPresent(removeOldAvatar());
        user.setAvatarUrl(path.toString());
        applicationUserUpdateService.update(user);
    }

    public Consumer<String> removeOldAvatar() {
        return url -> {
            try {
                Files.deleteIfExists(Path.of(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
