package com.ecom.service.impl;

import com.ecom.dto.*;
import com.ecom.enums.UserRole;
import com.ecom.model.User;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import com.ecom.util.JwtUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponse> fetchAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToResponse(user))
                .toList();
    }

    @Override
    public Optional<UserResponse> fetchUser(Long id) {
       return userRepository.findById(id).map(this :: mapToResponse);
    }

    @Override
    public void addUser(UserRequest userRequest) {
        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email Already Exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .userName(userRequest.getUserName())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(encoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .userRole(UserRole.CUSTOMER)
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean updateUser(Long id, UserRequest updateUserRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userRepository.findById(id)
                .map(user -> {
                    user.setUserName(updateUserRequest.getUserName());
                    user.setFirstName(updateUserRequest.getFirstName());
                    user.setLastName(updateUserRequest.getLastName());
                    user.setEmail(updateUserRequest.getEmail());
                    user.setPhone(updateUserRequest.getPhone());
                    user.setAddress(updateUserRequest.getAddress());
                    if(null != updateUserRequest.getPassword() &&  !updateUserRequest.getPassword().isBlank()){
                        user.setPassword(encoder.encode(updateUserRequest.getPassword()));
                    }
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String input = request.getUsernameOrEmail();
        Optional<User> optionalUser;
        if(input.contains("@")){
            optionalUser = userRepository.findByEmail(input);
        }else{
            optionalUser = userRepository.findByUserName(input);
        }
        User user = optionalUser
                .orElseThrow(() -> new RuntimeException("Invalid user name / email"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }
        String token = JwtUtil.generateToken(user.getUserName());

        return LoginResponse.builder()
                .token(token)
                .message("Login Successfully")
                .build();
    }

    @Override
    @Cacheable(value = "tokenCache", key = "#token")
    public TokenValidationResponse validateToken(String token) {
        try {
            String username = JwtUtil.validateToken(token);
            return TokenValidationResponse.builder()
                    .valid(true)
                    .username(username)
                    .message("Token is valid")
                    .build();

        } catch (Exception e) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Invalid or expired token")
                    .build();
        }
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id((user.getId()))
                .keyCloakId(user.getKeyCloakId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getUserRole())
                .address(user.getAddress())
                .build();
    }
}
