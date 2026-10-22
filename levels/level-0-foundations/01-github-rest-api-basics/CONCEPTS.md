# Concepts — A REST API for issues

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Client–server and HTTP

Apps (clients) send **requests** to servers over HTTP. A request has a **method**, a **path**, headers and maybe a body; the response has a **status code**, headers and a body (usually JSON). Everything else in web backends is built on this.

## Resources and URLs

REST models the API as **resources** (nouns) at URLs: `/issues` is the collection, `/issues/42` one item. Actions are expressed by methods, not by verbs in the URL (`POST /issues`, not `/createIssue`).

## HTTP methods

- `GET` — read; **safe** (changes nothing) and idempotent.
- `POST` — create; not idempotent (two POSTs make two items).
- `PUT` — replace entirely; idempotent.
- `PATCH` — change some fields.
- `DELETE` — remove; idempotent in effect (the item is gone either way, even if the second call returns 404).

## Status codes

`2xx` success (`200 OK`, `201 Created`, `204 No Content`); `4xx` the client did something wrong (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `405 Method Not Allowed`, `409 Conflict`, `429 Too Many Requests`); `5xx` the server failed (`500`, `502`, `503`). Clients decide whether to retry based on these: 5xx and 429 usually yes, 4xx usually no.

## Validation

Never trust input. Validate the **whole** request before changing anything, and return a clear error. Half-applied updates are a classic bug.

## API design beyond REST

**GraphQL** lets clients ask for exactly the fields they need; **gRPC** uses binary messages and generated code for fast service-to-service calls. REST remains the default for public APIs.
