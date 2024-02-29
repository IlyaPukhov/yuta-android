package com.ilyap.yuta.utils;

import com.ilyap.yuta.R;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    PROCESS(R.string.project_in_process),
    PAUSE(R.string.project_paused),
    COMPLETE(R.string.project_complete);

    private final int stringId;
}