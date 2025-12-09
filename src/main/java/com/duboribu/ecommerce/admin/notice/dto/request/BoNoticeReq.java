package com.duboribu.ecommerce.admin.notice.dto.request;

import com.duboribu.ecommerce.entity.Notice;
import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.enums.NoticeType;
import com.duboribu.ecommerce.front.enums.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoNoticeReq {
    private Long id;
    private String title;
    private String comment;
    private State state;
    private String filePath;
    private Member member;
    private NoticeType noticeType;

    public Notice toEntity() {
        return new Notice(title, comment, state, member, noticeType);
    }
}
