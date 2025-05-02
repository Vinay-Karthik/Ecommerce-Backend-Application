package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.USER_ROLE;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.repository.VerificationCodeRepository;
import com.example.ecommerce.request.LogInRequest;
import com.example.ecommerce.response.AuthResponse;
import com.example.ecommerce.response.SignUpRequest;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;


    @Override
    public void sendOtp(String email, USER_ROLE role) throws Exception {
        String SIGNING_PREFIX = "signing_";
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }
        //    String SELLER_PREFIX = "seller_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null) {
                    throw new Exception("seller not found");
                }

            } else {
                User users = userRepository.findByEmail(email);
                if (users == null) {
                    throw new Exception("User not found by email");
                }

            }


        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "otp for the verification";
        String text = "your otp is " + otp;
        emailService.sendVerificationOtpEmail(email, otp, subject, text);


    }

    @Override
    public String createUser(SignUpRequest signUpRequest) throws Exception {
        String email = signUpRequest.getEmail();
        String SIGNING_PREFIX = "signing_";
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signUpRequest.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().trim().equals(signUpRequest.getOtp().trim())) {
            throw new Exception("wrong otp...");
        }


        User user = userRepository.findByEmail(signUpRequest.getEmail());

        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(signUpRequest.getEmail());
            createdUser.setFullName(signUpRequest.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("7894651321");
            createdUser.setPassword(passwordEncoder.encode(signUpRequest.getOtp()));
            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtProvider.generateToken(authentication);

    }

    @Override
    public AuthResponse signIn(LogInRequest logInRequest) throws Exception {
        String username = logInRequest.getEmail();
        String otp = logInRequest.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("User logged in successfully");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }


        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("Invalid otp");
        }
        return new
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}
