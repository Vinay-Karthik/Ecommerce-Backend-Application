package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice((int) (item.getQuantity() * item.getProduct().getMrpPrice()));
            item.setSellingPrice((int) (item.getQuantity() * item.getProduct().getSellingPrice()));
            return cartItemRepository.save(item);
        }
        throw new Exception("you cannot update this item");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item = findCartItemById(cartItemId);

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            cartItemRepository.delete(item);
        } else throw new Exception("you cannot delete this item");
    }

    @Override
    public CartItem findCartItemById(Long userId) throws Exception {
        return cartItemRepository.findById(userId).orElseThrow(() -> new Exception("cart item not found"));
    }
}
