package com.deepakraj.cart.service;

import com.deepakraj.cart.dto.AddToCartRequest;
import com.deepakraj.cart.dto.CartResponse;

import java.util.List;

public interface CartService {

    CartResponse addToCart(AddToCartRequest request);

    List<CartResponse> getCartByUser(Long userId);

    void removeFromCart(Long id);

    void clearCart(Long userId);
}
