# Manual Smoke

Manual smoke testing must start a real Velocity proxy. Do not claim integration
coverage without doing so.

## Checklist

1. Build the JAR.
2. Copy it to a clean Velocity proxy with no extra plugin dependencies.
3. Start the proxy.
4. Confirm the plugin loads.
5. Confirm default config is created.
6. Connect a player.
7. Confirm join message.
8. Confirm `/hub` disabled or unavailable behavior.
9. Confirm MOTD changes in the server list.
10. Confirm tab header, footer, and display name.
11. Run `/omv reload`.
12. Confirm invalid config fails reload without crashing the proxy.
13. Run `/omv restart status`.
14. Run `/omv restart now` only in a controlled environment.
