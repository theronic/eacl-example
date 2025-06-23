# EACL Example

Refer to ~70 lines in the [`src/eacl_example/core.clj`](./src/eacl_example/core.clj) example.

This is a minimal example that demonstrates how to use [EACL](https://github.com/CloudAfrica/eacl) from Clojure:

It shows the following:
1. Install the latest EACL Datomic schema
2. Transact a small permission schema with resources for users, accounts and products:
  - where a user can own an account,
  - a product has under an account, and
  - users havae basic permissions.
4. Transact some permissioned entities, which must have `:eacl/type` & `:eacl/id` attributes.
5. Transact some relationships for `[user :owner account]` & `[account :account product]` so EACL can traverse the graph betweend `user -> account <- product`.
6. Run some `eacl/can?` permission checks, which return `true` or `false`.
7. Enumerate the resources a subject can access via `eacl/lookup-resources`.

## Usage in your Clojure Project

If you'd like to try this from your project, you'll need the Datomic Peer library and the EACL dependencies in your `deps.edn` file:

```clojure
cloudafrica/eacl {:git/url "git@github.com:cloudafrica/eacl.git"
                  :git/sha "ed3c437ac94f3c4049e838a794f3a6987d1eb1d1"}
```
(or newer `:git/sha`, until published on clojars)
