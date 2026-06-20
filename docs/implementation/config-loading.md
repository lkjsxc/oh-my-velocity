# Config Loading

Config loading has two parts:

- Adapter IO reads `config.yml` and writes the default file.
- Pure parser and mapper transform lines into config records.

## YAML Subset

Supported syntax:

- Nested maps by indentation.
- Lists of scalars.
- Lists of maps.
- Quoted and unquoted scalars.
- Empty strings.
- Comments beginning with `#` outside quotes.

Unsupported syntax should fail clearly instead of being skipped.

## Validation

Validation runs after mapping and before runtime config swap. Tests cover valid
defaults, invalid values, malformed indentation, and legacy keys that are no
longer active.
