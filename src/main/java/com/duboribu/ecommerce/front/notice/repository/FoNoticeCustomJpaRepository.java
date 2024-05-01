package com.duboribu.ecommerce.front.notice.repository;

import com.duboribu.ecommerce.front.notice.dto.reponse.FoNoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoNoticeCustomJpaRepository {
    Page<FoNoticeResponse> getFoNoticeList(Pageable request);

    FoNoticeResponse noticeView(Long id);
}
