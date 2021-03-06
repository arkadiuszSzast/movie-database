package com.movie.database.movie_database.user.token.blacklist.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, UUID> {
    boolean existsByToken(String token);
    void deleteAllByCreatedDateBefore(LocalDateTime dateTime);
}
