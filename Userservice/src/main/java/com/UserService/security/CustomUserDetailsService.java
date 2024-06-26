package com.UserService.security;

import com.UserService.Repository.UserRepository;
import com.UserService.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component  // Marks this class as a Spring component to be automatically detected and registered
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;  // Injects UserRepository to interact with user data in the database

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user from the database based on email or mobile number (assuming username can be either)
        User user = userRepository.findByEmailOrMobile(username, username);

        // If user is not found, throw UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Create UserDetails object with user details and authorities (roles)
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // Username for authentication (typically email in this case)
                user.getPassword(),  // Password for authentication
                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))  // Authorities (roles) granted to the user
        );
    }
}
