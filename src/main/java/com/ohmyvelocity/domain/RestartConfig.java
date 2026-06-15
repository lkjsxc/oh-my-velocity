package com.ohmyvelocity.domain;

import java.util.List;

public record RestartConfig(
        boolean enabled,
        int intervalHours,
        int randomJitterMinutes,
        List<Integer> warningMinutes,
        String kickMessage,
        String mode,
        String externalHook) {

    public boolean externalHookMode() {
        return "external_hook".equalsIgnoreCase(mode);
    }
}
