# Change Sequence

1. Identify the product or architecture contract.
2. Edit docs first when behavior changes.
3. Implement the smallest code diff that satisfies the contract.
4. Add or update unit tests for domain logic.
5. Run `./scripts/verify.sh` locally or via Docker compose verify.
