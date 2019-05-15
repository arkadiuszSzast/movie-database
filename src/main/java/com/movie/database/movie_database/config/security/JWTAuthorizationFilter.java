package com.movie.database.movie_database.config.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTProperties jwtProperties;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JWTProperties jwtProperties) {
        super(authManager);
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var header = request.getHeader(jwtProperties.getHeaderName());

        if (isNotBearerHeader(header)) {
            chain.doFilter(request, response);
            return;
        }

        var authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(jwtProperties.getHeaderName());
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token.replace(jwtProperties.getHeaderPrefix(), ""))
                    .getBody()
                    .getSubject();
            return user != null ? new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()) : null;
        }
        return null;
    }

    private boolean isNotBearerHeader(String header) {
        return header == null || !header.startsWith(jwtProperties.getHeaderPrefix());
    }
}
