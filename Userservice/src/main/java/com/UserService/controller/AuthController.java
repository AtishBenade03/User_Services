package com.UserService.controller;

import com.UserService.payLoad.JwtResponse;
import com.UserService.payLoad.Login;
import com.UserService.security.CustomUserDetailsService;
import com.UserService.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Endpoint to handle user login and return JWT token
    @PostMapping
    public JwtResponse login(@RequestBody Login login) {
        // Authenticate user credentials
        Authentication authentication = this.doAuthenticate(login.getUser(), login.getPassword());
        // Set authenticated user in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Load user details from custom UserDetailsService
        UserDetails user = customUserDetailsService.loadUserByUsername(login.getUser());
        // Generate JWT token for the authenticated user
        String token = this.jwtTokenProvider.generateToken(user);

        // Prepare JwtResponse object with JWT token and username
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwtToken(token);
        jwtResponse.setUsername(login.getUser());

        // Return JwtResponse object containing JWT token
        return jwtResponse;
    }

    // Method to authenticate user credentials using AuthenticationManager
    private Authentication doAuthenticate(String email, String password) {
        // Create authentication token with provided username and password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try {
            // Attempt authentication
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            return authenticate;
        } catch (BadCredentialsException e) {
            // Throw exception if authentication fails due to bad credentials
            throw new RuntimeException("Invalid Username or Password");
        }
    }
}
