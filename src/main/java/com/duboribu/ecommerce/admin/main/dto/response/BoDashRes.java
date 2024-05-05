package com.duboribu.ecommerce.admin.main.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class BoDashRes {
    private long totalCount;
    private long currentYearCount;
    private long dcrt;
    private String state = "increase";

    public BoDashRes(long totalCount, long currentYearCount, long lastYearCount) {
        this.totalCount = totalCount;
        this.currentYearCount = currentYearCount;

        long increase = currentYearCount - lastYearCount;
        double increasePercent = ((double) increase / lastYearCount) * 100;
        if (lastYearCount == 0) increasePercent = 100;

        this.dcrt = Math.round(increasePercent);

        if (dcrt < 0) state = "decrease";
    }
}
