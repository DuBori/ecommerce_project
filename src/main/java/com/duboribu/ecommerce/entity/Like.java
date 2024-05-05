package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
