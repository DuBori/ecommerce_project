package com.duboribu.ecommerce.entity.member;

import com.duboribu.ecommerce.entity.BaseEntity;
import com.duboribu.ecommerce.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleType roleType;

}
