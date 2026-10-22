# Concepts — Resumable chunked upload

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Chunked uploads

Big files are split into **chunks** (e.g. 8 MB each) that are uploaded separately. If chunk 512 fails, you retry only that chunk, not the whole 4 GB. Chunks can also be uploaded **in parallel** to use more bandwidth. S3 calls this *multipart upload*; YouTube and Google Drive use *resumable uploads*.

## Resumability

After a crash or network change, the client asks the server "which chunks do you have?" and sends only the missing ones. That needs the server to keep a small **session record**: total chunks and which ones have arrived (a list, a set, or a bitmap).

## Checksums

A **checksum** is a small number computed from the data. The client computes it before sending; the server recomputes it on arrival. If they differ, the data was corrupted on the way. Real systems use CRC32 or MD5/SHA-256; the simple byte sum in this problem shows the idea but would miss errors like swapped bytes.

## Idempotency

Because clients retry, the same chunk can arrive twice. Making `receive` **idempotent** (the second copy is harmless) means the client can retry freely without fear of corrupting the upload. Idempotency is the main tool for building reliable systems on unreliable networks.
