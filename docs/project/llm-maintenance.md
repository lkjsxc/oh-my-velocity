# LLM Maintenance

Future agents should treat docs as the control surface.

## Change Order

1. Read the active product contract for the feature being changed.
2. Update docs first when behavior changes.
3. Make implementation match the docs exactly.
4. Add or update focused tests for the changed contract.
5. Run Docker Compose verification before claiming completion.

## Quality Rules

- Keep docs and Java files at or below 200 lines.
- Prefer deletion and existing utilities before adding abstraction.
- Do not preserve old behavior unless active docs say it is desired.
- Move speculative ideas to research-only docs or leave them out.
- Commit coherent chunks with a decision-focused message.
