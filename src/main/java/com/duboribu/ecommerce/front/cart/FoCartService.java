package com.duboribu.ecommerce.front.cart;

import com.duboribu.ecommerce.entity.Cart;
import com.duboribu.ecommerce.entity.CartItem;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.front.cart.dto.CartRequest;
import com.duboribu.ecommerce.front.cart.dto.CartsRequest;
import com.duboribu.ecommerce.front.cart.repository.CartCustomJpaRepository;
import com.duboribu.ecommerce.front.item.repository.FoItemCustomRepository;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import com.duboribu.ecommerce.repository.CartJpaRepository;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
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
    private final MemberJpaRepository memberJpaRepository;
    private final ItemJpaRepository itemJpaRepository;

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

    public FoOrderResponse getCartList(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("계정 정보가 없습니다.");
        }
        return cartCustomJpaRepository.findCartByUserId(userId);
    }
}
