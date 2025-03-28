package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
    boolean existsCartByOrderIsNullAndMemberId(String userId);

    Optional<Cart> findByMember_IdAndOrderIsNull(String userId);

    void deleteCartByMemberId(String userId);
}
