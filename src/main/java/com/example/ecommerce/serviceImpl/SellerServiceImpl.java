package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.domain.USER_ROLE;
import com.example.ecommerce.exception.SellerException;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.SellerRepository;

import com.example.ecommerce.repository.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist != null){
            throw new Exception("seller already exist");
        }
        Address savedAddress = addressRepository.save(seller.getPickUpAddress());
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickUpAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());;
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(()-> new SellerException("seller not found with id:"+id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null){
            throw new Exception("seller not found");
        }
        return sellerRepository.save(seller);
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);

    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(()-> new Exception("Seller not found by id:" + id));

        if (seller.getSellerName() != null){
            existingSeller.setSellerName(seller.getSellerName());
        }
        if (seller.getEmail() != null){
            existingSeller.setEmail(seller.getEmail());
        }
        if (seller.getPickUpAddress() != null){
            existingSeller.setPickUpAddress(seller.getPickUpAddress());
        }
        if (seller.getGSTIN() != null){
            existingSeller.setGSTIN(seller.getGSTIN());
        }
        if (seller.getMobile() != null){
            existingSeller.setMobile(seller.getMobile());
        }
        if (seller.getBankDetails() != null && seller.getBankDetails().getAccountHolderName() != null && seller.getBankDetails().getIfscCode()!=null && seller.getBankDetails().getAccountNumber() != null){
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());

        }
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null){
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);

    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {

        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
