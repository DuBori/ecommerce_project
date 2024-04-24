package com.duboribu.ecommerce.front.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum State {
    Y,
    N;

    public static State getmatcheState(String state) {
        return Arrays.stream(State.values())
                .filter(it -> state.toUpperCase().equals(it.name()))
                .findFirst()
                .get();
    }
}
