package com.duboribu.ecommerce.front.notice.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCommentReq {
    private Long id;
    private Long parentId;
    private String detail;

}
