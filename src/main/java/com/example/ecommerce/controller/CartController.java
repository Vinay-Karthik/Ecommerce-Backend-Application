package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.AddItemRequest;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.service.CartItemService;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCartHandler(@RequestHeader("Authorization") String jwt, @RequestBody AddItemRequest request) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(request.getProductId());
        CartItem cartItem = cartService.addCartItem(user, product, request.getSize(), request.getQuantity());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item added to cart successfully");
        return new ResponseEntity<>(cartItem, HttpStatus.ACCEPTED);


    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long cartItemId) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.deleteCartItem(user.getId(), cartItemId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Item removed from cart successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);

    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long cartItemId, @RequestBody CartItem cartItem) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;
        if (cartItem.getQuantity() > 0) {
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }


}

