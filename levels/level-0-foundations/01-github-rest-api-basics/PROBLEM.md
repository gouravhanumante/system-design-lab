# A REST API for issues

| App | Level | Concept |
|-----|-------|---------|
| GitHub | 0 — Foundations | Resources, HTTP methods, status codes, validation |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Stuck? [`HINTS.md`](HINTS.md).

## The real-world situation

Every app you've built talks to a backend through an API. GitHub's REST API is a textbook example: **resources** live at URLs (`/issues`, `/issues/42`), **HTTP methods** say what to do (`GET` read, `POST` create, `PATCH` update, `DELETE` remove), and **status codes** say what happened (`201 Created`, `404 Not Found`…). Getting these conventions right is the first thing interviewers check in "design the API" questions.

## Build this (small scale)

A tiny HTTP router for an `issues` resource. No real network — requests and responses are plain objects.

## API

```kotlin
data class Request(val method: String, val path: String,
                   val body: Map<String, String> = emptyMap(), val query: Map<String, String> = emptyMap())
data class Response(val status: Int, val body: Map<String, String> = emptyMap(),
                    val items: List<Map<String, String>> = emptyList())

class IssuesApi {
    fun handle(req: Request): Response
}
```

An issue is represented as the map `{"id": "1", "title": "...", "state": "open"}`. Ids are `1, 2, 3, …` and are never reused.

## Rules

| Request | Success | Errors |
|---------|---------|--------|
| `POST /issues` with `title` | `201`, body = the new issue (state `open`) | missing or blank title → `400` |
| `GET /issues` | `200`, `items` = issues sorted by id; optional query `state=open` / `state=closed` filters | any other `state` value → `400` |
| `GET /issues/{id}` | `200`, body = the issue | unknown id → `404` |
| `PATCH /issues/{id}` with optional `title` and/or `state` | `200`, body = updated issue | unknown id → `404`; blank title or state not `open`/`closed` → `400` (nothing changes) |
| `DELETE /issues/{id}` | `204`, empty body | unknown id → `404` |

- Any path other than `/issues` and `/issues/{id}` → `404`. A known path with an unsupported method (e.g. `PUT /issues`, `POST /issues/3`) → `405`.
- An `{id}` that is not a positive number → `404`.
- Every error response has a body `{"error": "<some message>"}` (tests only check the key exists).

## Example

`POST /issues {title: "Crash on login"}` → `201 {id: "1", title: "Crash on login", state: "open"}`. `DELETE /issues/1` twice → `204`, then `404`.

## Think about (answer in NOTES.md)

- Which of GET, POST, PATCH, PUT, DELETE are **idempotent**? Why does that matter when a mobile app retries?
- Why `201` and not `200` for create? Why `204` for delete? What would `409 Conflict` and `429 Too Many Requests` be used for?
- How would you add pagination to `GET /issues` (hint: the Twitter cursor problem)?

---

## Run the tests

```bash
./gradlew :l0-github-rest-api-basics:test
```

Your code goes in `src/main/kotlin/IssuesApi.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
