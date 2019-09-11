package com.movie.database.movie_database.config.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private final AccessTokenProperties accessTokenProperties;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final ApplicationUserGetService applicationUserGetService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, AccessTokenProperties accessTokenProperties,
                                  TokenBlacklistRepository tokenBlacklistRepository,
                                  ApplicationUserGetService applicationUserGetService) {
        super(authManager);
        this.accessTokenProperties = accessTokenProperties;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.applicationUserGetService = applicationUserGetService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(accessTokenProperties.getHeaderName());
        if (tokenBlacklistRepository.existsByToken(token)) {
            return null;
        }
        try {
            var userId = JWT.require(Algorithm.HMAC256(accessTokenProperties.getSecret())).build()
                    .verify(token).getSubject();
            var applicationUser = applicationUserGetService.findById(UUID.fromString(userId));
            return new UsernamePasswordAuthenticationToken(applicationUser, null, applicationUser.getRoles());
        } catch (Exception e) {
            log.warn("Unsuccessful authentication from address: " + request.getRemoteAddr());
            return null;
        }
    }
}
