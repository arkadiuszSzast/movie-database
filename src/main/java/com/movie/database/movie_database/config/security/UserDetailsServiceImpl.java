package com.movie.database.movie_database.config.security;

import com.movie.database.movie_database.user.UserNotFoundException;
import com.movie.database.movie_database.user.ApplicationUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public ApplicationUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        var applicationUser = applicationUserRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return new ApplicationUserDetail(applicationUser);
    }
}
