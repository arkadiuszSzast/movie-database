package com.movie.database.movie_database.config.security;

import com.movie.database.movie_database.user.UserNotFoundException;
import com.movie.database.movie_database.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ApplicationUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        var applicationUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return new ApplicationUserDetail(applicationUser);
    }
}
