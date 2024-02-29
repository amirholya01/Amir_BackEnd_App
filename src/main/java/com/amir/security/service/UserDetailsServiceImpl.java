package com.amir.security.service;

import com.amir.domain.User;
import com.amir.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementation of the Spring Security UserDetailsService interface.
 * This class is responsible for loading user-specific data.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;



    /**
     * Load user by username.
     * @param username The username of the user to be loaded.
     * @return UserDetails object representing the loaded user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username in the repository
        User user = userRepository.findByUsername(username).orElseThrow(
                // Throw UsernameNotFoundException if user is not found
                () -> new UsernameNotFoundException("User was not found " + username)
        );
        // Build and return UserDetailsImpl object from the found user
        return UserDetailsImpl.build(user);
    }
}
