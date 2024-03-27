package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList = new ArrayList<>();
    @OneToMany(mappedBy = "order")
    private List<Delivery> delivery = new ArrayList<>();

}
