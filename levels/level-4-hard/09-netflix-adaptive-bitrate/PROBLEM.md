# Adaptive bitrate: picking video quality

| App | Level | Concept |
|-----|-------|---------|
| Netflix | 4 — Hard | Throughput estimation (harmonic mean), buffer-based safety rules, hysteresis |

> New to an idea in here? Read [`CONCEPTS.md`](CONCEPTS.md) first — it explains how things work, without giving away the solution. Not sure what to write? Follow **Your plan, step by step** below. Stuck on a step? [`HINTS.md`](HINTS.md).

## The real-world situation

Netflix video is split into short segments, each available in several qualities (a **bitrate ladder**: 235 kbps … 16,000 kbps). Before each segment the player picks a quality. Too high → the buffer drains and the video **stalls**. Too low → blurry picture. Jumping up and down every few seconds also looks bad. The player estimates bandwidth from recent downloads and watches how many seconds of video are already buffered.

## Build this (small scale)

A quality selector driven by download measurements and the current buffer level.

## API

```kotlin
class BitrateSelector(ladderKbps: List<Int>, safetyFactor: Double = 0.8, window: Int = 3) {
    fun onSegmentDownloaded(bytes: Long, millis: Long)
    fun estimateKbps(): Double?
    fun choose(bufferSeconds: Double): Int
}
```

## Rules

- `ladderKbps` is sorted ascending and not empty (else `IllegalArgumentException`). The player **starts** at the lowest rung.
- Each download gives a throughput sample `kbps = bytes × 8 / millis` (bits per millisecond = kilobits per second). Keep only the last `window` samples.
- `estimateKbps()` = the **harmonic mean** of the kept samples: `n / (1/s1 + 1/s2 + … + 1/sn)`. No samples → `null`.
- `choose(bufferSeconds)` decides the next quality, remembers it as the current one, and returns it:
  1. no samples yet → lowest rung;
  2. `bufferSeconds < 5` → **panic**: lowest rung;
  3. otherwise `target` = the highest rung `≤ safetyFactor × estimate` (the lowest rung if none fits);
  4. `target < current` → switch **down** straight to `target`;
  5. `target > current` → switch **up**, but only if `bufferSeconds >= 15`, and only **one rung** at a time;
  6. otherwise stay at `current`.

## Example

Ladder `[500, 1000, 3000, 6000]`. One sample of 10,000 kbps → target 6000 (≤ 8000). With 20 s of buffer the player climbs one rung per decision: 1000, 3000, 6000. A sudden sample of 1,000 kbps pulls the harmonic mean down hard and the player drops immediately.

## Your plan, step by step

The starter file already declares **every function and field you need** — each one has a comment saying exactly what it must do. Fill them in in this order and run the tests after each step. 🎯 marks the tests that check each step (a few also need a later step before they go green).

1. **`init`** checks. 🎯 `invalidLadder`
2. **`onSegmentDownloaded()`** + **`estimateKbps()`**. 🎯 `throughputAndHarmonicMean`, `onlyTheLastWindowSamplesCount`
3. **`highestAffordableRung()`** + **`choose()`**. 🎯 `startsLowWithoutData`, `climbsOneRungAtATimeWithAHealthyBuffer`, `doesNotClimbWithAThinBuffer`, `dropsImmediatelyAndAllTheWayToTheTarget`, `panicModeWhenTheBufferIsNearlyEmpty`, `safetyFactorKeepsHeadroom`, `veryLowBandwidthStillPlaysTheLowestRung`

## Think about (answer in NOTES.md)

- Why harmonic mean rather than the normal average? Try samples 1,000 and 100,000 kbps with both.
- Switching *up* is cautious and *down* is immediate. Why is that asymmetry a good idea?
- Netflix also has "buffer-based" algorithms that mostly ignore bandwidth estimates. When would those be better?

---

## Run the tests

```bash
./gradlew :l4-netflix-adaptive-bitrate:test
```

Your code goes in `src/main/kotlin/BitrateSelector.kt` — replace the `TODO()`s. Do not edit the tests. When they all pass: fill in `NOTES.md`, then `REVIEW.md`.
