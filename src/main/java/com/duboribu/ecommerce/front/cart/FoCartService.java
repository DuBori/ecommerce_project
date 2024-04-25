package com.duboribu.ecommerce.front.cart;

import com.duboribu.ecommerce.entity.Cart;
import com.duboribu.ecommerce.entity.CartItem;
import com.duboribu.ecommerce.entity.Item;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.front.item.repository.FoItemCustomRepository;
import com.duboribu.ecommerce.front.order.dto.CreateOrderRequest;
import com.duboribu.ecommerce.front.order.dto.FoOrderResponse;
import com.duboribu.ecommerce.repository.CartJpaRepository;
import com.duboribu.ecommerce.repository.ItemJpaRepository;
import com.duboribu.ecommerce.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoCartService {
    private final CartJpaRepository cartJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ItemJpaRepository itemJpaRepository;

    private final FoItemCustomRepository foItemCustomRepository;


    @Transactional
    public void addCategory(String userId, CartsRequest cartRequest) {
        Optional<Member> findMember = memberJpaRepository.findById(userId);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            List<CartItem> cartItemList = getCartItems(cartRequest);
            Cart cart = new Cart(member, cartItemList);
            cartJpaRepository.save(cart);
        }

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

    /*추후 바꿔야됨 QueryDsl로 확인용도*/
    public FoOrderResponse getCartList(String userId) {
        Optional<Cart> findCart = cartJpaRepository.findFirstByMember_IdOrderByCreatedAtDesc(userId);
        if (findCart.isPresent()) {
            Cart cart = findCart.get();
            List<CartItem> cartItemList = cart.getCartItemList();
            List<CreateOrderRequest.OrderItemRequest> list = new ArrayList<>();
            for (CartItem cartItem : cartItemList) {
                list.add(new CreateOrderRequest.OrderItemRequest(cartItem.getItem().getId(), cartItem.getQuantity()));
            }
            return foItemCustomRepository.itemViewResponses(new CreateOrderRequest(0, 0, list));
        }
        return null;
    }
}
