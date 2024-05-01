package com.duboribu.ecommerce.front.notice.dto.reponse;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FoNoticeResponse {
    private Long id;
    private String title;
    private String detail;
    private String filePath;
    private String createAt;
    private String updateAt;
    private String createBy;
    private List<FoNoticeComment> comments;

    @QueryProjection
    public FoNoticeResponse(Long id, String title, String detail, String filePath, LocalDateTime createAt, LocalDateTime updateAt, String createBy) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.filePath = filePath;
        this.createAt = createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.updateAt = updateAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.createBy = createBy;
    }

    public int getCommentCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }
    public void matchedComments(List<FoNoticeComment> commentList) {
        comments = commentList;
    }
}
