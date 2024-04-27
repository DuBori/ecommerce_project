package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
/*@NoArgsConstructor(access = AccessLevel.PROTECTED)*/
@NoArgsConstructor
public class Cart extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItemList = new ArrayList<>();

    public Cart(Member member, List<CartItem> cartItemList) {
        this.member = member;
        if (!cartItemList.isEmpty()) {
            cartItemList.forEach(it -> it.mactchedCart(this));
        }
        this.cartItemList = cartItemList;
    }


}
