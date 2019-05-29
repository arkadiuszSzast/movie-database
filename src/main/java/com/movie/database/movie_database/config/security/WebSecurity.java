package com.movie.database.movie_database.config.security;

import com.movie.database.movie_database.config.security.filters.JWTAuthenticationFilter;
import com.movie.database.movie_database.config.security.filters.JWTAuthorizationFilter;
import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.support.properties.AccessTokenProperties;
import com.movie.database.movie_database.support.properties.RefreshTokenProperties;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklistRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static final String SIGN_UP_URL = "/api/users/sign-up";
    private static final String AUTH_REFRESH_URL = "/api/auth/refresh";
    private static final String LOGIN_URL = "/api/auth/login";
    private static final String CONFIRM_URL = "/api/confirm";
    private static final String RESET_PASSWORD_MAIL_URL = "/api/reset-password/mail";
    private static final String RESET_PASSWORD_URL = "/api/reset-password";

    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;
    private final JWTGenerateService jwtGenerateService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public WebSecurity(AccessTokenProperties accessTokenProperties,
                       RefreshTokenProperties refreshTokenProperties,
                       JWTGenerateService jwtGenerateService,
                       TokenBlacklistRepository tokenBlacklistRepository) {
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
        this.jwtGenerateService = jwtGenerateService;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, AUTH_REFRESH_URL).permitAll()
                .antMatchers(HttpMethod.GET, CONFIRM_URL).permitAll()
                .antMatchers(HttpMethod.POST, RESET_PASSWORD_URL).permitAll()
                .antMatchers(HttpMethod.POST, RESET_PASSWORD_MAIL_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getJwtAuthenticationFilter())
                .addFilter(getJWTAuthorizationFilter());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private JWTAuthorizationFilter getJWTAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManager(), accessTokenProperties, tokenBlacklistRepository);
    }

    private JWTAuthenticationFilter getJwtAuthenticationFilter() throws Exception {
        var jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager(), jwtGenerateService, accessTokenProperties, refreshTokenProperties);
        jwtAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        return jwtAuthenticationFilter;
    }
}
