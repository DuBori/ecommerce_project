package com.duboribu.ecommerce.auth.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDao {

    private final EntityManager entityManager;

}
