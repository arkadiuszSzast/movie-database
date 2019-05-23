package com.movie.database.movie_database.user.token.domain;

import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.token.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserTokenRepository extends JpaRepository<ApplicationUserToken, UUID> {

    Optional<ApplicationUserToken> findByTokenAndTokenType(String token, TokenType tokenType);

    Optional<ApplicationUserToken> findByApplicationUserAndTokenType(ApplicationUser applicationUser, TokenType tokenType);

    void deleteAllByApplicationUserAndTokenType(ApplicationUser applicationUser, TokenType tokenType);
}
