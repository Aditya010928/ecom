package com.ecom.controller;

import com.ecom.dto.LoginRequest;
import com.ecom.dto.LoginResponse;
import com.ecom.dto.UserRequest;
import com.ecom.dto.UserResponse;
import com.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(),
                HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("User added successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody UserRequest updateUserRequest){
        boolean updated = userService.updateUser(id, updateUserRequest);
        if (updated)
            return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }
}
