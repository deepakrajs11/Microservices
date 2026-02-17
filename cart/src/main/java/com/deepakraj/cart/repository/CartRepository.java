package com.deepakraj.cart.repository;

import com.deepakraj.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUserId(String userId);

    Optional<Cart> findByUserIdAndProductId(String userId, String productId);
}
