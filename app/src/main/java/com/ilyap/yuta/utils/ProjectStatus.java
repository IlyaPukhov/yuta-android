package com.ilyap.yuta.utils;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum ProjectStatus {
    PROCESS("в работе"),
    PAUSE("приостановлен"),
    COMPLETE("завершен");

    private final String text;

    @Override
    public @NotNull String toString() {
        return text;
    }
}