package com.ohmyvelocity.domain;

public record HubCommandConfig(boolean enabled, String targetServer, LocalizedMessagesConfig messages) {
}
