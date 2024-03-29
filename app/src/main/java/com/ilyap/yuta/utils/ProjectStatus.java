package com.ilyap.yuta.utils;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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

    public static ProjectStatus fromText(String text) throws IllegalArgumentException {
        return Arrays.stream(ProjectStatus.values())
                .filter(v -> v.toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown value: " + text));
    }
}