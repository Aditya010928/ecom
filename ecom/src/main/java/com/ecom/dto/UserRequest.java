package com.ecom.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private Address address;
}
