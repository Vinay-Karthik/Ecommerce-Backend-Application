package com.example.ecommerce.controller;

import com.example.ecommerce.domain.USER_ROLE;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.request.LogInRequest;
import com.example.ecommerce.request.LoginOtpRequest;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.response.AuthResponse;
import com.example.ecommerce.response.SignUpRequest;
import com.example.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createdUserHandler(@RequestBody SignUpRequest signUpRequest) throws Exception {
        String jwt = authService.createUser(signUpRequest);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpRequest loginOtpRequest) throws Exception {
        authService.sendOtp(loginOtpRequest.getEmail(), loginOtpRequest.getRole());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("otp sent successfully");
        return ResponseEntity.ok(apiResponse);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signInOtpHandler(@RequestBody LogInRequest logInRequest) throws Exception {
        AuthResponse authResponse = authService.signIn(logInRequest);
        return ResponseEntity.ok(authResponse);

    }


}
