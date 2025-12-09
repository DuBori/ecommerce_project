package com.duboribu.ecommerce.admin.notice.repository;

import com.duboribu.ecommerce.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminNoticeJpaRepository extends JpaRepository<Notice, Long> {

}
