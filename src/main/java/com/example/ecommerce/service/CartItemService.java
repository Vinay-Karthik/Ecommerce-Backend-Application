package com.example.ecommerce.service;

import com.example.ecommerce.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception;

    void deleteCartItem(Long userId, Long cartItemId) throws Exception;

    CartItem findCartItemById(Long userId) throws Exception;


}
