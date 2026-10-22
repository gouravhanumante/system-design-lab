# Concepts — "Share trip" route deviation alerts

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Polylines

A route is stored as a **polyline**: a list of points, where each consecutive pair forms a straight **segment**. Maps encode polylines compactly (Google's "encoded polyline" format). A curvy road is just many short segments.

## Distance from a point to a segment

The nearest point on a segment A→B to a point P is found by **projecting** P onto the line through A and B. The projection gives a factor `t` (0 at A, 1 at B). If `t` is outside `[0, 1]`, the nearest point is the closer end (A or B), so you **clamp** `t` into that range. Then measure the distance from P to that nearest point. Using squared distances avoids square roots when comparing.

## Debouncing and hysteresis

Sensors are noisy: one bad GPS fix shouldn't trigger an alarm. **Debouncing** requires the condition to hold for several readings in a row. **Hysteresis** means once you've alerted, you need a clear "back to normal" signal before alerting again, so you don't fire on every ping. Both ideas are used in monitoring systems (PagerDuty alert rules, CPU-usage alarms) to reduce false positives and alert spam.

## Per-entity state in a stream

Each trip keeps a little state (current streak, already alerted?) that updates with each event. Stream processors call this **keyed state**: state partitioned by a key (trip id), updated event by event. Because state is per key, the work can be spread over many machines by key.
