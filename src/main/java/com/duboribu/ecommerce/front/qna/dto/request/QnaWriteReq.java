package com.duboribu.ecommerce.front.qna.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaWriteReq {
    private String title;
    private String cont;
}
