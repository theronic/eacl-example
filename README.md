# EACL: Minimal Example from Clojure

This is a minimal example that demonstrates how to use [EACL](https://github.com/CloudAfrica/eacl), an embedded [SpiceDB-compatible](https://authzed.com/spicedb)* [ReBAC](https://en.wikipedia.org/wiki/Relationship-based_access_control) authorization library built in Clojure and backed by Datomic.

The ~70 lines in [`src/eacl_example/core.clj`](./src/eacl_example/core.clj) shows the following:

1. Install the latest EACL Datomic schema
2. Transact a small permission schema with resources for users, accounts and products:
  - where a user can own an account,
  - a product has under an account, and
  - users havae basic permissions.
4. Transact some permissioned entities, which must have `:eacl/type` & `:eacl/id` attributes.
5. Transact some relationships for `[user :owner account]` & `[account :account product]` so EACL can traverse the graph betweend `user -> account <- product`.
6. Run some `eacl/can?` permission checks, which return `true` or `false`.
7. Enumerate the resources a subject can access via `eacl/lookup-resources`.

## Add EACL to a Clojure Project

If you'd like to try this from your project, you'll need the Datomic Peer library and the EACL dependencies in your `deps.edn` file:

```clojure
cloudafrica/eacl {:git/url "git@github.com:cloudafrica/eacl.git"
                  :git/sha "ed3c437ac94f3c4049e838a794f3a6987d1eb1d1"}
```
(or use a newer `:git/sha`, until EACL is published on Clojars)
