package com.duboribu.ecommerce.front.notice.dto.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class FoNoticeComment {
    private Long commentId;
    private Long parentCommentId;
    private String userName;
    private String detail;
    private List<FoNoticeComment> NoticeCommentChildList;
    @QueryProjection
    public FoNoticeComment(Long commentId, Long parentCommentId, String userName, String detail) {
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.userName = userName;
        this.detail = detail;
    }
    public void matchedList(List<FoNoticeComment> list) {
        NoticeCommentChildList = list;
    }
}
