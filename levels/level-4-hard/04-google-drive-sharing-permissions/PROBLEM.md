# Sharing, groups and inherited permissions

| App | Level | Concept |
|-----|-------|---------|
| Google Drive | 4 — Hard | Permission inheritance up a tree, groups, max-wins resolution |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

"Share this folder with the marketing team (Editor), and this one person as Viewer." Everything inside inherits those rights — unless the owner marked a subfolder as *restricted*. Getting this wrong means leaking someone's tax documents. The hard part is not storing grants; it is answering **"what can this user do on this path?"** correctly with groups, inheritance and overrides.

## Build this (small scale)

A permission model over a folder tree.

## API

```kotlin
enum class Permission { NONE, VIEW, EDIT, OWNER }      // ordered: NONE < VIEW < EDIT < OWNER

class SharedDrive {
    fun addToGroup(group: String, user: String)
    fun createFolder(path: String, creator: String)
    fun share(path: String, principal: String, permission: Permission, by: String)
    fun revoke(path: String, principal: String, by: String)
    fun breakInheritance(path: String, by: String)
    fun permissionOf(user: String, path: String): Permission
}
```

A *principal* is either a user name or a group name.

## Rules

- Folder paths look like `/a/b`. The root `/` exists and has no grants.
- `createFolder`: parent must exist and the path must not (else `IllegalArgumentException`). Creating directly under `/` is allowed for anyone. Creating deeper needs **EDIT or OWNER** on the parent, else `SecurityException`. The creator gets an explicit `OWNER` grant on the new folder.
- `share`: `by` must be **OWNER** on `path` (else `SecurityException`). It sets the explicit grant of `principal` on that folder to `permission` (replacing any earlier grant of that principal there).
- `revoke`: same authority rule; removes the principal's explicit grant on that folder (nothing happens if there is none).
- `breakInheritance`: `by` must be OWNER. From then on, permissions on this folder and everything below it ignore grants made on **ancestors** of this folder (grants on this folder itself still count).
- `permissionOf(user, path)`: starting at `path` and walking **up** towards the root, collect every explicit grant whose principal is the user **or a group the user belongs to**. Stop walking after you have processed a folder that has inheritance broken. The answer is the **highest** permission found, or `NONE`. Unknown path → `IllegalArgumentException`.
- Group membership is global and can change at any time.

## Example

`alice` creates `/docs` (OWNER). She shares it with group `team` as EDIT. `bob` is in `team` → `permissionOf("bob", "/docs/q1")` is `EDIT` (inherited). She calls `breakInheritance("/docs/secret", …)` → bob has `NONE` on `/docs/secret` but alice still has `OWNER` (her own explicit grant is on that folder).

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`splitPath()`**, **`findFolder()`**, **`folderOrThrow()`**, **`addToGroup()`**.
2. **`createFolder()`** at the top level (creator becomes OWNER) + **`effectivePermission()`** without groups. 🎯 `creatorIsOwnerEveryoneElseHasNothing`, `invalidPaths`
3. **`share()`** / **`revoke()`** with **`requireOwner()`**, and inheritance in `effectivePermission()`. 🎯 `sharedPermissionIsInheritedByChildren`, `highestPermissionWins`, `onlyOwnersCanShareRevokeOrBreakInheritance`, `revokeRemovesTheExplicitGrant`, `sharingAgainReplacesTheEarlierGrant`
4. **Groups** in `effectivePermission()`. 🎯 `groupSharesApplyToMembersEvenIfTheyJoinLater`
5. **Sub-folders need EDIT** in `createFolder()`. 🎯 `creatingFoldersNeedsEditOnTheParent`
6. **`breakInheritance()`**. 🎯 `breakingInheritanceHidesFoldersFromAncestorGrants`

## Think about (answer in NOTES.md)

- Computing a permission walks up the whole path. For a path 30 levels deep and a million checks per second, what would you cache — and when must the cache be invalidated?
- Should "no access" be an explicit **deny** that beats everything else? What are the trade-offs versus "highest wins"?
- When a user is removed from a group, how quickly must access disappear? What would a design that is always instantly correct cost?

---

## Run the tests

```bash
./gradlew :l4-google-drive-sharing-permissions:test
```

Your code goes in `src/main/kotlin/SharedDrive.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
