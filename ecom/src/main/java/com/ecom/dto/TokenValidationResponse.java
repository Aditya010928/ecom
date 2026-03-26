package com.ecom.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private String message;
}