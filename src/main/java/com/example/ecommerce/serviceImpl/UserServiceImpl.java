package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        System.out.println("Extracted email from JWT token: " + email);

        if (email == null || email.isEmpty()) {
            throw new Exception("Invalid email address");
        }
        User user = this.findUserByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }
        return user;
    }

    //    @Override
//    public User findUserByEmail(String email) throws Exception {
//        if (email == null || email.isEmpty()) {
//            throw new Exception("Email cannot be null or empty");
//        }
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new Exception("user not found with email:" + email);
//        }
//        return user;
//    }
    @Override
    public User findUserByEmail(String email) throws Exception {
        String signingPrefix = "signing_";
        if (email.startsWith(signingPrefix)) {
            email = email.substring(signingPrefix.length());
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("user not found with email:" + email);
        }
        return user;
    }
}
