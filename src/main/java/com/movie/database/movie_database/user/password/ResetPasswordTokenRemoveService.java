package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import com.movie.database.movie_database.user.token.domain.ApplicationUserTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenRemoveService {

    private final ApplicationUserTokenRepository applicationUserTokenRepository;

    public ResetPasswordTokenRemoveService(ApplicationUserTokenRepository applicationUserTokenRepository) {
        this.applicationUserTokenRepository = applicationUserTokenRepository;
    }

    public void remove(ApplicationUserToken applicationUserToken) {
        applicationUserTokenRepository.delete(applicationUserToken);
    }
}
