package com.deepakraj.cart.controller;

import com.deepakraj.cart.dto.AddToCartRequest;
import com.deepakraj.cart.dto.CartResponse;
import com.deepakraj.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/{userId}")
    public List<CartResponse> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }

    @DeleteMapping("/clear/{userId}")
    public void clear(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}

