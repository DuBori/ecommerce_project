package com.duboribu.ecommerce.entity;

import com.duboribu.ecommerce.entity.member.Member;
import com.duboribu.ecommerce.enums.NoticeType;
import com.duboribu.ecommerce.front.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Notice extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String comment;
    @Enumerated(EnumType.STRING)
    private State state;
    private String filePath;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;
    @OneToMany(mappedBy = "notice", cascade = CascadeType.PERSIST)
    List<Comment> commentList = new ArrayList<>();

    public Notice(String title, String comment, State state, Member member, NoticeType noticeType) {
        this.title = title;
        this.comment = comment;
        this.state = state;
        this.member = member;
        this.noticeType = noticeType;
    }
}
