package com.ecom.service;

import com.ecom.dto.*;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<UserResponse> fetchAllUsers();
    Optional<UserResponse> fetchUser(Long id);
    void addUser(UserRequest userRequest);
    boolean updateUser(Long id, UserRequest updateUserRequest);
    LoginResponse login(LoginRequest request);

    TokenValidationResponse validateToken(String token);
}
