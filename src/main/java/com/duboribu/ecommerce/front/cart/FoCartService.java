package com.duboribu.ecommerce.front.cart;

import com.duboribu.ecommerce.entity.Cart;
import com.duboribu.ecommerce.entity.CartItem;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.Stock;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.front.cart.dto.request.CartQuantityReq;
import com.duboribu.ecommerce.front.cart.dto.request.CartRequest;
import com.duboribu.ecommerce.front.cart.dto.request.CartsRequest;
import com.duboribu.ecommerce.front.cart.dto.response.CartItemResponse;
import com.duboribu.ecommerce.front.cart.repository.CartCustomJpaRepository;
import com.duboribu.ecommerce.front.item.repository.FoItemCustomRepository;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import com.duboribu.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoCartService {
    private final CartJpaRepository cartJpaRepository;
    private final CartCustomJpaRepository cartCustomJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final StockJpaRepository stockJpaRepository;


    private final FoItemCustomRepository foItemCustomRepository;


    @Transactional
    public boolean addCategory(String userId, CartsRequest cartRequest) {
        Optional<Member> findMember = memberJpaRepository.findById(userId);

        if (findMember.isEmpty()) {
            throw new IllegalArgumentException("일치하는 회원이 없습니다.");
        }

        Member member = findMember.get();

        //기존 카트가 존재하면 처리해야함
        if (cartJpaRepository.existsCartByOrderIsNullAndMemberId(userId)) {
            return cartCustomJpaRepository.mergeIntoByCartRequest(userId, cartRequest.getList());
        }
        // 신규카트는 생성해주면됨
        List<CartItem> cartItemList = getCartItems(cartRequest);
        
        Cart cart = new Cart(member, cartItemList);
        cartJpaRepository.save(cart);
        return true;
    }

    private List<CartItem> getCartItems(CartsRequest cartRequest) {
        List<CartItem> list = new ArrayList<>();
        for (CartRequest request : cartRequest.getList()) {
            Optional<Item> findItem = itemJpaRepository.findById(request.getProductId());
            if (findItem.isPresent()) {
                list.add(new CartItem(findItem.get(), request.getQuantity()));;
            }
        }
        return list;
    }
    @Transactional
    public FoOrderResponse getCartList(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("계정 정보가 없습니다.");
        }
        return cartCustomJpaRepository.findCartByUserId(userId);
    }
    @Transactional
    public CartItemResponse updateQuantity(String unit, CartQuantityReq cartQuantityReq) {
        int updateCount = 0;

        Optional<CartItem> findCartItem = cartItemJpaRepository.findById(cartQuantityReq.getCartItemId());
        if (findCartItem.isEmpty()) {
            log.error("찾는 장바구니 상품 없음");
            throw new IllegalArgumentException("찾는 장바구니 상품 없음");
        }

        Optional<Stock> findStock = stockJpaRepository.findByItem(itemJpaRepository.findById(cartQuantityReq.getItemId()).get());
        if (findStock.isEmpty()) {
            log.error("찾는 재고 ID가 없음");
            throw new IllegalArgumentException("찾는 재고 ID가 없음");
        }

        CartItem cartItem = findCartItem.get();

        if ("inc".equals(unit)) {
            updateCount = cartItem.getQuantity() + 1;
        } else {
            updateCount = cartItem.getQuantity() - 1;
        }

        Stock stock = findStock.get();
        if (stock.getCount() < updateCount) {
            throw new IllegalArgumentException("재고보다 요청개수가 더 많음 구매불가");
        }
        cartItem.updateQuantity(updateCount);

        return cartItem.toResponse();
    }
    @Transactional
    public boolean deleteCartItem(Long cartItem) {
        Optional<CartItem> findCartItem = cartItemJpaRepository.findById(cartItem);
        if (findCartItem.isEmpty()) {
            return false;
        }
        cartItemJpaRepository.delete(findCartItem.get());
        return true;
    }
}
