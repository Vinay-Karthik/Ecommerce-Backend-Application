package com.example.ecommerce.service;

import com.example.ecommerce.model.User;

public interface UserService {


    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}
