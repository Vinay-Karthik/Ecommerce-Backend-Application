package com.example.ecommerce.service;


import com.example.ecommerce.domain.USER_ROLE;
import com.example.ecommerce.request.LogInRequest;
import com.example.ecommerce.response.AuthResponse;
import com.example.ecommerce.response.SignUpRequest;

public interface AuthService {
    void sendOtp(String email , USER_ROLE role) throws Exception;
    String createUser(SignUpRequest signUpRequest) throws Exception;
    AuthResponse signIn(LogInRequest logInRequest) throws Exception;

}
