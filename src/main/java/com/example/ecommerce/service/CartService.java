package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws Exception;

    public Cart findUserCart(User user) throws Exception;
}
