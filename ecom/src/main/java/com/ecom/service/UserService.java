package com.ecom.service;

import com.ecom.dto.LoginRequest;
import com.ecom.dto.LoginResponse;
import com.ecom.dto.UserRequest;
import com.ecom.dto.UserResponse;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<UserResponse> fetchAllUsers();
    Optional<UserResponse> fetchUser(Long id);
    void addUser(UserRequest userRequest);
    boolean updateUser(Long id, UserRequest updateUserRequest);
    LoginResponse login(LoginRequest request);
}
