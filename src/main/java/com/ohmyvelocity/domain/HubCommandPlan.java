package com.ohmyvelocity.domain;

public record HubCommandPlan(HubCommandAction action, String targetServer, String message) {
}
