package com.duboribu.ecommerce.admin.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoSearchCommand {
    private int page = 0;
    private int size = 20;
}
