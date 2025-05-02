package com.example.ecommerce.controller;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.exception.SellerException;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.repository.SellerService;
import com.example.ecommerce.repository.VerificationCodeRepository;
import com.example.ecommerce.request.LogInRequest;
import com.example.ecommerce.response.AuthResponse;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.serviceImpl.EmailService;
import com.example.ecommerce.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final SellerRepository sellerRepository;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LogInRequest logInRequest) throws Exception {
        String otp = logInRequest.getOtp();
        String email =logInRequest.getEmail();

        logInRequest.setEmail("seller_" + email);
        AuthResponse authResponse = authService.signIn(logInRequest);

        return ResponseEntity.ok(authResponse);

    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("wrong otp..");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail() , otp);

        return new ResponseEntity<>(seller , HttpStatus.OK);
    }

    @PostMapping("/createSeller")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller ) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);
        String subject = " verification code";
        String text = "welcome to ecommerce";
        String frontend_url = "http://localhost:1234/seller/verify-otp";
        emailService.sendVerificationOtpEmail(seller.getEmail() , verificationCode.getOtp(),subject , text + frontend_url);
        return new ResponseEntity<>(savedSeller , HttpStatus.CREATED);



    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller , HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller , HttpStatus.OK);
    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt){
//        String email = jwtProvider.getEmailFromJwtToken((jwt));
//        Seller seller = sellerService.getSellerByEmail(email);
//        SellerReport report = sellerReportService.getSellerReport(Seller);
//        return new ResponseEntity<>(report , HttpStatus.OK);
//    }

    @GetMapping("/getAllSellers")
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)AccountStatus status){
        List<Seller> sellers = sellerService.getAllSellers((status));
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping("/updateSeller")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt ,@RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updateSeller);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }









}
