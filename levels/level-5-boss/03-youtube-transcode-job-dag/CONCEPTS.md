# Concepts — Video processing pipeline (job DAG scheduler)

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Directed acyclic graph (DAG)

A **DAG** is a set of tasks with arrows "A must finish before B", and no cycles (a cycle would mean nothing can start). Build systems (Gradle!), CI pipelines, data pipelines (Airflow) and video processing are all DAGs.

## Topological order and cycle detection

A **topological order** lists tasks so every task comes after its dependencies. **Kahn's algorithm**: repeatedly take tasks with no unfinished dependencies, remove them, and update the others. If tasks remain but none is free, there is a cycle. This is also how you validate the DAG before running anything.

## Discrete-event simulation

Instead of ticking a clock second by second, a **discrete-event simulation** jumps straight from one interesting moment to the next (here: the next time a job ends). At each moment you process what happened, then decide what starts. It's how schedulers, network simulators and games with "turns" are modelled efficiently.

## Worker pools and scheduling policy

A fixed number of **workers** run jobs. When several jobs are ready, a **policy** decides who goes first: priority, first-come-first-served, shortest-job-first… The policy changes total time and fairness. Real schedulers (Kubernetes, YARN, Borg) are mostly sophisticated policies on top of this simple loop.

## Retries and failure propagation

Transient failures (a machine hiccup) deserve a **retry**; permanent failures must **stop** everything downstream, since it can't possibly succeed. Tracking "skipped because a dependency failed" separately from "failed" tells operators where the real problem is.
