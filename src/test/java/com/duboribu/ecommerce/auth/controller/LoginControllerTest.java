package com.duboribu.ecommerce.auth.controller;

import com.duboribu.ecommerce.entity.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest

class LoginControllerTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 회원가입진행() {

        Member member = new Member("wjdgus5", "1234", "정현");
        entityManager.persist(member);
        Member findMember = entityManager.find(Member.class, member.getId());

        assertEquals(member.getId(), findMember.getId());
    }

}