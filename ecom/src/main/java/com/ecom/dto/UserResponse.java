package com.ecom.dto;

import com.ecom.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String keyCloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private Address address;
}
