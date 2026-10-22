# Concepts — Adaptive bitrate: picking video quality

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Adaptive bitrate streaming (ABR)

Video is cut into short **segments** (2–10 s), each encoded at several qualities (the **bitrate ladder**). The player picks a quality for each segment based on current conditions. Protocols: **HLS** (Apple) and **DASH**. The player is the decision maker; the server just serves files.

## Throughput estimation

Each downloaded segment gives a measurement: bits ÷ time. Network speed is noisy, so players combine several recent samples. The **harmonic mean** is preferred over the normal average because one unusually fast sample barely moves it, while slow samples pull it down — matching the goal of avoiding stalls.

## Buffer-based decisions

The playback **buffer** (seconds of video already downloaded) is a safety margin. A big buffer can absorb a slow download, so it's safe to try higher quality; a nearly empty buffer means a stall is imminent, so drop to the lowest quality immediately (panic mode).

## Safety margin

Choosing a bitrate *equal* to the estimated bandwidth leaves no room for error. Multiplying the estimate by a **safety factor** (like 0.8) keeps headroom.

## Hysteresis (asymmetric switching)

Frequent quality changes look bad. Making it **hard to go up** (only one step, only with a healthy buffer) and **easy to go down** (immediately, as far as needed) reduces flapping and stalls at the same time. The same idea appears in thermostats, autoscalers and health checks.
