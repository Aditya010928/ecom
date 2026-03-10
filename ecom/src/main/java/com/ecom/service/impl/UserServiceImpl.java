package com.ecom.service.impl;

import com.ecom.dto.LoginRequest;
import com.ecom.dto.LoginResponse;
import com.ecom.dto.UserRequest;
import com.ecom.dto.UserResponse;
import com.ecom.enums.UserRole;
import com.ecom.model.User;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        User user = User.builder()
                .userName(userRequest.getUserName())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .userRole(UserRole.CUSTOMER)
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean updateUser(Long id, UserRequest updateUserRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUserName(updateUserRequest.getUserName());
                    user.setFirstName(updateUserRequest.getFirstName());
                    user.setLastName(updateUserRequest.getLastName());
                    user.setEmail(updateUserRequest.getEmail());
                    user.setPhone(updateUserRequest.getPhone());
                    user.setAddress(updateUserRequest.getAddress());
                    if(null != updateUserRequest.getPassword() &&  !updateUserRequest.getPassword().isBlank()){
                        user.setPassword(updateUserRequest.getPassword());
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

        if(!request.getPassword().equals(user.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }
        return LoginResponse.builder()
                .token("dummyToken")
                .message("Login Successfully")
                .build();
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
