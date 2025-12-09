package com.duboribu.ecommerce.admin.notice.service;

import com.duboribu.ecommerce.admin.notice.dto.request.BoNoticeReq;
import com.duboribu.ecommerce.admin.notice.dto.response.BoNoticeRes;
import com.duboribu.ecommerce.admin.notice.repository.AdminNoticeCustomRepository;
import com.duboribu.ecommerce.admin.notice.repository.AdminNoticeJpaRepository;
import com.duboribu.ecommerce.entity.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNoticeService {
    private final AdminNoticeJpaRepository adminNoticeJpaRepository;
    private final AdminNoticeCustomRepository adminNoticeCustomRepository;

    @Transactional(readOnly = true)
    public Page<BoNoticeRes> findNoticeList(Pageable pageable, String searchType, String keyword) {
        return adminNoticeCustomRepository.findNoticeList(pageable, searchType, keyword);
    }

    @Transactional(readOnly = true)
    public BoNoticeRes findNoticeById(Long id) {
        return adminNoticeCustomRepository.findNoticeById(id);
    }

    @Transactional
    public BoNoticeRes save(BoNoticeReq boNoticeReq) {
        Notice notice = adminNoticeJpaRepository.save(boNoticeReq.toEntity());
        return new BoNoticeRes(notice);
    }

    @Transactional
    public BoNoticeRes update(BoNoticeReq boNoticeReq) {
        Notice notice = adminNoticeJpaRepository.findById(boNoticeReq.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다. id=" + boNoticeReq.getId()));
        
        notice.update(
                boNoticeReq.getTitle(),
                boNoticeReq.getComment(),
                boNoticeReq.getState(),
                boNoticeReq.getFilePath(),
                boNoticeReq.getNoticeType()
        );
        
        return new BoNoticeRes(notice);
    }

    @Transactional
    public void delete(Long id) {
        adminNoticeJpaRepository.deleteById(id);
    }
}
