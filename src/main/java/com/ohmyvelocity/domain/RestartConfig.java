package com.ohmyvelocity.domain;

import java.util.List;

public record RestartConfig(
        boolean scheduleEnabled,
        boolean manualCommandEnabled,
        int intervalHours,
        int randomJitterMinutes,
        List<Integer> warningMinutes,
        LocalizedMessagesConfig warningMessage,
        LocalizedMessagesConfig kickMessage,
        String mode,
        String externalHook,
        int externalHookTimeoutSeconds) {

    public RestartConfig {
        warningMinutes = List.copyOf(warningMinutes);
    }

    public boolean enabled() {
        return scheduleEnabled;
    }

    public boolean externalHookMode() {
        return "external_hook".equalsIgnoreCase(mode);
    }
}
