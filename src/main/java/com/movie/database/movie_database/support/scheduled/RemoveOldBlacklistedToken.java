package com.movie.database.movie_database.support.scheduled;

import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class RemoveOldBlacklistedToken {

    private static Logger log = LoggerFactory.getLogger(RemoveOldBlacklistedToken.class);

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public RemoveOldBlacklistedToken(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Scheduled(cron = "${cron.remove-blacklisted-token}")
    @Transactional
    public void removeOldBlacklistedToken() {
        log.info("Removing old blacklisted tokens");
        var date = LocalDateTime.now().minusHours(2);
        tokenBlacklistRepository.deleteAllByCreatedDateBefore(date);
    }
}
