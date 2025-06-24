package com.felxx.park_rest_api.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felxx.park_rest_api.entities.User;
import com.felxx.park_rest_api.services.UserService;
import com.felxx.park_rest_api.web.dto.UserCreateDto;
import com.felxx.park_rest_api.web.dto.UserPasswordDto;
import com.felxx.park_rest_api.web.dto.UserResponseDto;
import com.felxx.park_rest_api.web.dto.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(UserMapper.toListDto(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(UserMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        User newUser = userService.save(UserMapper.toUser(userCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(newUser));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDto userPasswordDto) {
        userService.changePassword(id, userPasswordDto.getCurrentPassword(), userPasswordDto.getNewPassword(), userPasswordDto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }
}
