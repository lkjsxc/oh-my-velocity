# Layering

## plugin

Composition root: `OhMyVelocityPlugin`, `ServiceBootstrap`, registries, `Services`.

## domain

Pure Java: messages, placeholders, restart schedule math, config models.
No imports from `com.velocitypowered`.

## command

Brigadier `/omv` commands; delegates to domain and feature services.

## feature.*

Velocity adapters: event listeners, scheduler tasks, shutdown executor.

## Dependency Rule

`feature` and `command` may depend on `domain`. `domain` depends on nothing
Velocity-specific.
