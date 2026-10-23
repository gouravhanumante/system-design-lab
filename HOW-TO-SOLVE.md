# How to solve a problem

A problem is not done when the tests pass. It's done when you can **explain** your design and **what would break at 1,000× the scale**. That second part is what system-design interviews are actually about.

## The loop (about 1–3 hours)

### 1. Read `PROBLEM.md` (5–10 min)
- Read the *real-world situation* first. Picture the app on your phone.
- Read **Rules** twice. Every rule is tested. The **Example** is your first mental test case.

### 2. Read `CONCEPTS.md` (10–20 min)
- It explains the ideas you need (sliding windows, consistent hashing, idempotency…) without giving the solution.
- If a term is still unclear, look it up in [CONCEPTS.md](CONCEPTS.md) at the root — another problem may explain it from a different angle.

### 3. Design on paper before typing (10–15 min)
Answer these in a few words:
1. **What state do I need to remember?** (This decides your data structures.)
2. **What does each operation do to that state?**
3. **Which edge cases do the rules mention?** (exactly-at-the-boundary times, duplicates, unknown ids, empty inputs)

Write a rough version straight into `NOTES.md` section 2.

### 4. Code it (30–120 min)
- Open `src/main/kotlin/…kt`. Every field and function you need is already declared, with a comment saying exactly what it must do — you only write the bodies (replace the `TODO()`s).
- Follow **"Your plan, step by step"** in `PROBLEM.md`: it gives the order, and 🎯 marks the tests that check each step. Run those tests after each step.
- You may change the private parts (rename, add helpers, use other data structures) if you have a better idea — just don't change the public API (names, parameters, return types).
- Run the tests early and often: `./gradlew :<module>:test`, or ▶ in the IDE.
- **Don't edit the tests.** If you believe a test is wrong, write it down in `NOTES.md` and ask for a review.
- Stuck for 20+ minutes? Open **one** hint in `HINTS.md`. Then try again before opening the next.

### 5. Write `NOTES.md` (15–20 min)
This is the part that turns coding practice into system-design practice:
- your approach and **why** you picked those data structures,
- time and space complexity,
- **what breaks at 1,000× and 1,000,000× scale** and how you'd fix it (sharding, caching, queues, replication…),
- alternatives you rejected,
- answers to the *Think about* questions from `PROBLEM.md`.

### 6. Score it in `REVIEW.md`
Set `Status: solved`, fill in your **Self** column using [RATING.md](RATING.md), then get a review (see RATING.md → *Getting a review*). Run `python3 tools/scoreboard.py` to update [PROGRESS.md](PROGRESS.md).

### 7. Commit
```bash
git add levels/level-2-building-blocks/09-netflix-lru-edge-cache
git commit -m "Solve L2 Netflix LRU edge cache"
```
A steady stream of commits — each one a solved, explained problem — is a much better GitHub profile than one big dump.

## "Small scale" means small, not fake

You're building with arrays, lists and maps in memory. That's on purpose: the **logic** of a geo index, a rate limiter or a replication protocol is the same at any scale; the scale-up parts (more machines, a real database, a network) are what you describe in `NOTES.md` section 4. Interviewers want both: correct core logic, and a clear story for scaling it.

## Tips

- **Read the error message.** Test names describe the rule they check (`expiresExactlyAtTtl`, `refusedRequestSpendsNothing`).
- **Edge cases are where the learning is.** "Exactly at the timeout", "the same request twice", "the clock went backwards" — real outages come from exactly these.
- **Prefer clear over clever.** A readable O(n log n) solution with a good scale-up note beats an unreadable O(n) one.
- **Re-solve old problems** a month later without looking. If you can't, your notes weren't clear enough — improve them.
