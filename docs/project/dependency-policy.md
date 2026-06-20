# Dependency Policy

Runtime dependencies must be explicit, documented, and packaged safely.

## Allowed Surfaces

- Velocity API.
- Java standard library.
- Adventure and MiniMessage surfaces provided through Velocity.
- JUnit, Gradle, Docker, and small repository scripts for tests and builds.

## Runtime Rules

- The plugin must not require another Minecraft or Velocity plugin to be
  installed.
- Compile-only dependencies must not hide runtime requirements.
- Plugin metadata must not declare third-party plugin dependencies.
- New runtime libraries require docs and either shading or proof that Velocity
  provides them as stable API.
- Product contracts must not promise behavior that requires missing first-party
  API support.

## Build Rules

- Keep external repositories to those needed by allowed dependencies.
- Prefer JDK-only implementation when it is small and readable.
- Avoid relying on transitive libraries that are not declared in the build.
