package com.ohmyvelocity.domain;

public record RestartTickResult(Type type, String warningMessage, RestartConfig restartConfig) {
    public enum Type {
        IDLE,
        WARNING,
        RESTART_NOW
    }

    public static RestartTickResult idle() {
        return new RestartTickResult(Type.IDLE, "", null);
    }

    public static RestartTickResult warning(String message) {
        return new RestartTickResult(Type.WARNING, message, null);
    }

    public static RestartTickResult restartNow(RestartConfig config) {
        return new RestartTickResult(Type.RESTART_NOW, "", config);
    }
}
