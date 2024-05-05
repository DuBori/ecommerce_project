package com.duboribu.ecommerce.front.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum State {
    Y("활성화"),
    N("비활성화");
    
    private final String desc;

    public static State getmatcheState(String state) {
        return Arrays.stream(State.values())
                .filter(it -> state.toUpperCase().equals(it.name()))
                .findFirst()
                .get();
    }
}
