package com.duboribu.ecommerce.admin.notice.dto.response;

import com.duboribu.ecommerce.entity.Notice;
import com.duboribu.ecommerce.enums.NoticeType;
import com.duboribu.ecommerce.front.enums.State;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class BoNoticeRes {
    private Long id;
    private String title;
    private String comment;
    private State state;
    private String filePath;
    private NoticeType noticeType;
    private String createdBy;
    private String createdAt;
    private String updatedAt;

    public BoNoticeRes(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.comment = notice.getComment();
        this.state = notice.getState();
        this.filePath = notice.getFilePath();
        this.noticeType = notice.getNoticeType();
        this.createdBy = notice.getCreatedBy();
        this.createdAt = formatDateTime(notice.getCreatedAt());
        this.updatedAt = formatDateTime(notice.getUpdatedAt());
    }

    @QueryProjection
    public BoNoticeRes(Long id, String title, String comment, State state, 
                       String filePath, NoticeType noticeType, String createdBy,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.state = state;
        this.filePath = filePath;
        this.noticeType = noticeType;
        this.createdBy = createdBy;
        this.createdAt = formatDateTime(createdAt);
        this.updatedAt = formatDateTime(updatedAt);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
