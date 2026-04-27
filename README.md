# charactor

A Scala simulation of independent actors on a 2D world map. Each charactor has its own state, attributes (vision, speed, resistance, cannibalism, persistence, …), and a message-driven loop for moving, sensing surroundings, and consuming food sources.

Built around an explicit message protocol (`AttackMessage`, `MoveMessage`, `ConsumeFoodMessage`, `EnergyComparisonMessage`, …) and a composable attribute system. Single-process.

Built manually against the JARs in `lib/`.

## See also

- [`charactor-clustered`](https://github.com/PiotrTrzpil/charactor-clustered) — follow-up that runs a minimal mover across an Akka cluster.
