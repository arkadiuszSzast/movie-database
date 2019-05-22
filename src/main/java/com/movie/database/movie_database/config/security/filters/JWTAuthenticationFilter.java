package com.movie.database.movie_database.config.security.filters;

import com.movie.database.movie_database.config.security.ApplicationUserDetail;
import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.support.properties.RefreshTokenProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerateService jwtGenerateService;
    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JWTGenerateService jwtGenerateService,
                                   AccessTokenProperties accessTokenProperties,
                                   RefreshTokenProperties refreshTokenProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerateService = jwtGenerateService;
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getParameter("username"),
                        request.getParameter("password"),
                        new ArrayList<>())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        var applicationUser = ((ApplicationUserDetail) authResult.getPrincipal()).getApplicationUser();
        var accessToken = jwtGenerateService.getAccessToken(applicationUser.getId());
        var refreshToken = jwtGenerateService.getRefreshToken(applicationUser.getId());
        response.addHeader(accessTokenProperties.getHeaderName(), accessToken);
        response.addHeader(refreshTokenProperties.getHeaderName(), refreshToken);
    }
}
