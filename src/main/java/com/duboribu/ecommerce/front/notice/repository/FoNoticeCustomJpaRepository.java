package com.duboribu.ecommerce.front.notice.repository;

import com.duboribu.ecommerce.front.notice.dto.reponse.FoNoticeResponse;
import com.duboribu.ecommerce.front.qna.dto.request.NoticeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoNoticeCustomJpaRepository {
    Page<FoNoticeResponse> getFoNoticeList(Pageable request, NoticeRequest noticeType);

    FoNoticeResponse noticeView(Long id);
}
