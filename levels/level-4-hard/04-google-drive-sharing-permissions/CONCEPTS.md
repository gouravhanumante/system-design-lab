# Concepts — Sharing, groups and inherited permissions

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Access control lists (ACL)

An **ACL** attaches to each resource a list of "who can do what": `bob: VIEW`, `team: EDIT`. To answer "can bob edit this?", you look up the entries that apply to bob. Most file-sharing systems are built on ACLs.

## Inheritance up a hierarchy

Sharing a folder should apply to everything inside it. Instead of copying grants into every child (expensive, and it goes stale), you **walk up** from the item to the root at check time and consider every ancestor's grants. Cost is the depth of the tree per check, so real systems cache results.

## Groups (indirection)

Granting access to a **group** instead of 500 individual people means one change when someone joins or leaves. At check time, the set of identities a user acts as is `{user} ∪ their groups`. Groups inside groups (nesting) make this a graph search.

## Permission resolution rules

When several grants apply, you need a rule: **highest wins** (common in Drive-style sharing), or **explicit deny wins** (common in corporate/Windows-style systems). Breaking inheritance (a restricted folder) is a third mechanism. Whatever the rules, write them down precisely — security bugs live in the edge cases.

## Zanzibar (relationship-based access control)

Google's **Zanzibar** system stores permissions as tuples like `doc:readme#viewer@group:eng#member` and answers "check" questions across billions of objects. Open-source versions: SpiceDB, OpenFGA. Worth reading after this problem.
