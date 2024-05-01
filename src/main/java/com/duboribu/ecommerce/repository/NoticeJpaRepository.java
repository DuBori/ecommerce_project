package com.duboribu.ecommerce.repository;

import com.duboribu.ecommerce.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJpaRepository extends JpaRepository<Notice, Long> {
}
