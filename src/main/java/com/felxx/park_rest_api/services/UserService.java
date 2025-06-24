package com.felxx.park_rest_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felxx.park_rest_api.entities.User;
import com.felxx.park_rest_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service 
public class UserService {
    
    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public User changePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)){
            throw new RuntimeException("New password and confirm password do not match");
        }
        User user = findById(id);
        if(user.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(newPassword);
        return user;
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }
}
