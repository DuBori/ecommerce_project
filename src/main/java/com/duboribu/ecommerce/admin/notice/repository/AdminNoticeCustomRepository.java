package com.duboribu.ecommerce.admin.notice.repository;

import com.duboribu.ecommerce.admin.notice.dto.response.BoNoticeRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminNoticeCustomRepository {
    
    Page<BoNoticeRes> findNoticeList(Pageable pageable, String searchType, String keyword);
    
    BoNoticeRes findNoticeById(Long id);
}
