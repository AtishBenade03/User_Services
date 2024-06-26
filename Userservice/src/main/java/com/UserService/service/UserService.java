package com.UserService.service;

import com.UserService.entity.User;

import java.util.List;

public interface UserService {
     List<User> getAllUsers();

     User getUserById(Long userId);

     User createUser(User user);

     User updateUser(Long userId, User userDetails);

     void deleteUser(Long userId);
}
