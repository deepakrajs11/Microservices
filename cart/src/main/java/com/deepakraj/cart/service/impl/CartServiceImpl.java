package com.deepakraj.cart.service.impl;

import com.deepakraj.cart.client.ProductClient;
import com.deepakraj.cart.client.UserClient;
import com.deepakraj.cart.dto.AddToCartRequest;
import com.deepakraj.cart.dto.CartResponse;
import com.deepakraj.cart.dto.ProductResponseDTO;
import com.deepakraj.cart.dto.UserResponseDTO;
import com.deepakraj.cart.model.Cart;
import com.deepakraj.cart.repository.CartRepository;
import com.deepakraj.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    @Override
    public CartResponse addToCart(AddToCartRequest request) {

        // 1️⃣ Validate User
        UserResponseDTO user =
                userClient.getUserById(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 2️⃣ Validate Product
        ProductResponseDTO product =
                productClient.getProductById(request.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 3️⃣ Check if already exists in cart
        Optional<Cart> existing =
                cartRepository.findByUserIdAndProductId(
                        request.getUserId(),
                        request.getProductId()
                );

        Cart cart;

        if (existing.isPresent()) {
            cart = existing.get();
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        } else {
            cart = Cart.builder()
                    .userId(request.getUserId())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .priceAtAddTime(product.getPrice())
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        Cart saved = cartRepository.save(cart);

        return mapToResponse(saved);
    }

    @Override
    public List<CartResponse> getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.deleteAll(cartRepository.findByUserId(userId));
    }

    private CartResponse mapToResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .quantity(cart.getQuantity())
                .priceAtAddTime(cart.getPriceAtAddTime())
                .build();
    }
}
