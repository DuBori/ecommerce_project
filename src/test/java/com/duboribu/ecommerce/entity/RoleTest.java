package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Transient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.InitBinder;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RoleTest {

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    public void 역할을추가한다() {

    }

}