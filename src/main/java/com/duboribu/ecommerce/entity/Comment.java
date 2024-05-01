package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.front.notice.dto.request.CreateCommentReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String detail;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn (name = "notice_id")
    private Notice notice;
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;


    public Comment(Notice notice, Member member, CreateCommentReq createCommentReq) {
        this.notice = notice;
        this.member = member;
        this.detail = createCommentReq.getDetail();
    }

    public Comment(Notice notice, Member member, CreateCommentReq createCommentReq,  Comment parentComment) {
        this.notice = notice;
        this.member = member;
        this.parentComment = parentComment;
        this.detail = createCommentReq.getDetail();
    }
}
