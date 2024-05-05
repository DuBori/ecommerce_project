package com.duboribu.ecommerce.admin.member.repository;

import com.duboribu.ecommerce.admin.common.BoSearchCommand;
import com.duboribu.ecommerce.admin.member.dto.response.AdminMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminMemberCustomJpaRepository {
    Page<AdminMemberResponse> list(BoSearchCommand search, Pageable pageable);

    AdminMemberResponse getMemberById(String userId);
}
