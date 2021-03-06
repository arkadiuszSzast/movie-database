package com.movie.database.movie_database.user.avatar;

import com.movie.database.movie_database.support.user.CurrentUserId;
import com.movie.database.movie_database.user.avatar.validator.AvatarValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Validated
public class UserAvatarController {

    private final UserAvatarService userAvatarService;

    public UserAvatarController(UserAvatarService userAvatarService) {
        this.userAvatarService = userAvatarService;
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping("/api/user/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadAvatar(@AvatarValidator @RequestParam MultipartFile file, @CurrentUserId UUID userId) throws IOException {
        userAvatarService.save(file, userId);
    }
}
