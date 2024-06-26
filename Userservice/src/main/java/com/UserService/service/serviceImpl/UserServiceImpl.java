package com.UserService.service.serviceImpl;

import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.Repository.UserRepository;
import com.UserService.entity.User;
import com.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Marks this class as a Spring service component to be automatically detected and registered
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;  // Injects UserRepository for data access operations

    @Autowired
    private PasswordEncoder passwordEncoder;  // Injects PasswordEncoder for encrypting passwords

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Retrieves all users from the database
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));  // Retrieves a user by ID or throws ResourceNotFoundException if not found
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encrypts the user's password before saving
        return userRepository.save(user);  // Saves the new user entity in the database
    }

    @Override
    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));  // Retrieves the existing user by ID or throws ResourceNotFoundException

        // Updates user details with the provided userDetails
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));  // Encrypts the updated password
        user.setAddress(userDetails.getAddress());

        return userRepository.save(user);  // Saves the updated user entity in the database
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));  // Checks if user exists or throws ResourceNotFoundException
        userRepository.deleteById(userId);  // Deletes the user by ID
    }
}
