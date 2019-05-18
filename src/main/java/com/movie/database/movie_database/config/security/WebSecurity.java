package com.movie.database.movie_database.config.security;

import com.movie.database.movie_database.config.security.filters.JWTAuthenticationFilter;
import com.movie.database.movie_database.config.security.filters.JWTAuthorizationFilter;
import com.movie.database.movie_database.config.security.jwt.AccessTokenProperties;
import com.movie.database.movie_database.config.security.jwt.JWTGenerateService;
import com.movie.database.movie_database.config.security.jwt.RefreshTokenProperties;
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

    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;
    private final JWTGenerateService jwtGenerateService;

    public WebSecurity(AccessTokenProperties accessTokenProperties,
                       RefreshTokenProperties refreshTokenProperties,
                       JWTGenerateService jwtGenerateService) {
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
        this.jwtGenerateService = jwtGenerateService;
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
        return new JWTAuthorizationFilter(authenticationManager(), accessTokenProperties);
    }

    private JWTAuthenticationFilter getJwtAuthenticationFilter() throws Exception {
        var jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager(), jwtGenerateService, accessTokenProperties, refreshTokenProperties);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        return jwtAuthenticationFilter;
    }
}
