# Rating system

Every problem is scored out of **20** — once by you (**Self**) and once by a reviewer (**Reviewer**). The gap between the two is the most useful number: it shows where you over- or under-estimate yourself.

## The rubric

| Area | Max | What earns full marks |
|------|----:|------------------------|
| **Correctness** | 6 | All tests pass. 6 = all pass on the first full run; 5 = all pass after fixing a few failures; 3 = most pass; 0 = doesn't compile |
| **Edge cases** | 3 | Boundaries, duplicates, empty/unknown inputs handled *deliberately* (not by luck). Code makes it obvious which rule handles which case |
| **Design & trade-offs** (NOTES §2, §5) | 4 | Data structures fit the operations; you explain *why* and name at least one alternative you rejected |
| **Scale-up reasoning** (NOTES §4) | 4 | Concrete plan for 1,000× and 1,000,000×: where the bottleneck is, which technique fixes it (sharding, caching, batching, queues, replication…) and what that costs |
| **Code clarity** | 3 | Small functions, good names, no dead code, a newcomer could follow it |
| **Total** | **20** | |

### Grades

| Score | Grade | Meaning |
|------:|-------|---------|
| 18–20 | **S** | Interview-ready on this topic |
| 15–17 | **A** | Solid — polish the notes |
| 12–14 | **B** | Works, but the *why* or the *scale* story is thin |
| 8–11 | **C** | Come back to it in a few weeks |
| 0–7 | **D** | Re-read CONCEPTS.md and redo it |

### Hints and score

Using hints is fine — that's what they're for. Note how many you opened in `REVIEW.md` ("What went well"). If you needed hint 3, consider re-solving the problem from scratch a week later.

## Getting a review

Once tests pass and `NOTES.md` is written, ask a reviewer — a friend, a senior colleague, or an AI assistant. For an AI review, paste this:

> Review my solution to `levels/<level>/<problem>` in my System Design Lab repo. Read PROBLEM.md, CONCEPTS.md, my code in src/main and my NOTES.md. **Do not rewrite my solution.** Score it with the rubric in RATING.md (Correctness 6, Edge cases 3, Design 4, Scale-up 4, Clarity 3), fill in the *Reviewer* column of REVIEW.md, and write 3 concrete points under "What to improve": one about the code, one about the design, one about scaling.

The reviewer fills the **Reviewer** column and the `Reviewer-score:` line in `REVIEW.md`. You then write "Next time I will…" yourself.

## The three lines the scoreboard reads

The top of every `REVIEW.md` has:

```
Status: solved
Self-score: 16
Reviewer-score: 14
```

`Status` is `not-started`, `in-progress` or `solved`. Scores are plain numbers out of 20 (or `-`). Keep these lines exactly in this format — `tools/scoreboard.py` reads them to build [PROGRESS.md](PROGRESS.md).
