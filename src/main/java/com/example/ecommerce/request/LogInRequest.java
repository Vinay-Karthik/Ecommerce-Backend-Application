package com.example.ecommerce.request;

import lombok.Data;

@Data
public class LogInRequest {
    private String email;
    private String otp;
}
