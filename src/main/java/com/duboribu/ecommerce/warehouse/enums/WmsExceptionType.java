package com.duboribu.ecommerce.warehouse.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WmsExceptionType {
    DATE_REQUIRED_ERROR("지정날자를 입력하여 주세요.", 600),
    COMPANY_CODE_REQUIRED_ERROR("회사 코드는 필수입니다.", 601),
    WMS_LIST_EMPTY("프로세스를 진행시킬 리스트를 확인해 주세요.", 602),
    WMS_STATE_REQUIRED("진행시킬 프로세스를 선택해 주세요.", 603);


    private final String desc;
    private final int code;
}
