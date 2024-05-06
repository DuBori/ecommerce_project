package com.duboribu.ecommerce.front.qna.dto.request;

import com.duboribu.ecommerce.enums.NoticeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class NoticeRequest {
    private String userId;
    private NoticeType noticeType;
    public NoticeRequest(String userId, NoticeType noticeType) {
        this.userId = userId;
        this.noticeType = noticeType;
    }

    public NoticeRequest(NoticeType noticeType) {
        this.noticeType = noticeType;
    }
}
