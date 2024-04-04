package com.duboribu.ecommerce.warehouse;

import com.duboribu.ecommerce.warehouse.enums.WmsExceptionType;
import lombok.Getter;

@Getter
public class WmsException extends RuntimeException {
    private final int code;

    public WmsException(final WmsExceptionType wmsExceptionType) {
        super(wmsExceptionType.getDesc());
        this.code = wmsExceptionType.getCode();
    }
}
