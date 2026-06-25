# Energy Cable System Analysis: IndRevUnbricked vs TechReborn

## Transfer Rates

| Tier | IndRevUnbricked (LF/t) | TechReborn (E/t) |
|------|----------------------|-------------------|
| Entry | MK1: 128 | Tin: 32 |
| Low | MK2: 512 | Copper: 128 |
| Mid | MK3: 4,096 | Gold: 512 |
| High | MK4: 16,384 | HV: 2,048 |
| End | — | Glass Fiber: 8,192 |
| Creative | — | Superconductor: ~2.1B |

IndRev rates are 2–4× higher per tier. Rates alone are not the problem.

---

## Architecture Comparison

### IndRevUnbricked: Network-Pool Architecture

All cables in a connected group share a **single `EnergyNetwork` object** that owns one shared `energy` field (`EnergyNetwork.kt:40`). Cables are "dumb terminals" — their `CableEnergyIo` (`CableEnergyIo.kt:10`) is just a thin proxy that reads/writes the network's shared `energy` field. Energy flows:

```
Generator → cable (via CableEnergyIo.insert) → network buffer (energy field)
   ↓ tick()
Network distributes proportionally to each machine's maxInput
```

### TechReborn: Distributed-Buffer Architecture

Each `CableBlockEntity` has its **own `SimpleSidedEnergyContainer`** (`CableBlockEntity.java:64-81`) with capacity = `transferRate * 4`. On tick, `CableTickManager`:

1. BFS-gathers all connected cables of the **same transfer rate** (`CableTickManager.java:106-126`)
2. Aggregates all their energy into a virtual `networkAmount` (lines 55-56)
3. Pushes/pulls via `dispatchTransfer()` (lines 131-161)
4. Splits remaining energy **evenly** across all cables (lines 81-88)

```
Each cable has its own buffer. On tick:
  sum all cable buffers → distribute to machines → split remainder back evenly
```

---

## Critical Flaws in IndRevUnbricked

### 1. Tier-Contamination Bug (`EnergyNetwork.kt:93-97`)

```kotlin
override fun appendPipe(block: Block, blockPos: BlockPos) {
    val cable = block as? CableBlock ?: return
    this.tier = cable.tier  // OVERWRITTEN every time
    super.appendPipe(block, blockPos)
}
```

**Problem**: The last cable visited during `deepScan()` sets `this.tier` for the **entire network**. BFS order depends on worldgen, iteration order of internal sets, and placement order. A single MK1 cable anywhere in a MK4 backbone drops the whole network to 128 LF/t — both for **accepting** energy into the network and **distributing** to machines.

**TechReborn**: `gatherCables()` (`CableTickManager.java:117`) only connects cables of **exactly the same `transferRate`**. Different tiers form separate networks entirely. No cross-contamination possible.

### 2. Energy Voided on Network Splits (`EnergyNetworkState.kt:17-39`)

```kotlin
// Remove
destroyedEnergy += network.energy

// Add — only gives to the FIRST sub-network that registers
network.energy += destroyedEnergy.coerceAtMost(network.capacity)
destroyedEnergy -= network.energy

// Tick start — ERASES remaining destroyedEnergy
destroyedEnergy = 0
```

**Problem**: Breaking a cable that splits a network into two sub-networks:
- `remove()` puts all energy into `destroyedEnergy`
- Only the **first** sub-network's `add()` gets any of it
- The second sub-network starts at zero
- If re-scanning crosses a tick boundary, `destroyedEnergy = 0` voids **everything**

Also `writeNbt` (`EnergyNetworkState.kt:44`) saves energy at `pipes.minByOrNull { it }` — an arbitrary position. On world reload, the network may re-form in a different order, restoring energy to the wrong sub-network, or duplicating/losing it entirely.

**TechReborn**: Each `CableBlockEntity` saves its own `energyContainer.amount` to its own NBT (`CableBlockEntity.java:245`). On reload, each cable independently restores its buffer. No shared state, no split/merge bugs. Cable breakage does not destroy energy.

### 3. Per-Machine Transfer Artificially Capped (`EnergyNetwork.kt:79`)

```kotlin
val toTransfer = ((maxInput / totalInput) * energy).toLong()
    .coerceAtMost(maxCableTransfer)  // ← per-machine rate cap
    .coerceAtMost(energy)
```

**Problem**: Even if the network buffer has 100,000 LF and a machine needs 5,000, each machine receives **at most `maxCableTransfer` per tick** (128/512/4096/16384). The network only ticks **once per tick**, so per-machine throughput is hard-limited.

For MK1 clusters, every machine gets ≤128 LF/tick total, **split proportionally** among all machines. A machine needing 1,000 LF/tick will never fill.

**TechReborn**: `dispatchTransfer()` (`CableTickManager.java:131-161`) also limits per-target to `cableType.transferRate` (line 149), but with a critical difference: `remainingAmount / remainingTargets` means **if there's only one target, it gets the full `transferRate`**. And the energy pooled from all cables can push into that single machine. No proportional starvation.

### 4. Cross-Mod Compatibility is Fragile (`factories.kt:37-40`)

```kotlin
val energyOf = energyOf(world, pos, direction.opposite)
if (energyOf != null) {
    network.appendContainer(pos, direction.opposite)
    if (energyOf.supportsInsertion()) network.insertables.add(pos)
}
```

**Problem**: Whether another mod's machine receives energy depends on:
- It implementing `EnergyStorage.SIDED` (TechReborn API) — many mods use Forge Energy, Tesla, or custom APIs
- Reporting `supportsInsertion() == true` — some mods return false from machine-facing sides
- The direction check uses `direction.opposite` from the cable's connecting side — may not match the side the other mod exposes
- The probe `insert(Long.MAX_VALUE, tx)` — many mods don't handle this gracefully in an aborted transaction

**TechReborn**: `appendTargets()` (`CableBlockEntity.java:167-214`) uses the same `EnergyStorage.SIDED` API, so the same compatibility surface applies. However, TechReborn is a **much more widely used** mod, so other mods are far more likely to have tested and fixed compat with it. IndRev has no such network effect.

### 5. No Per-Tick Intake Throttle (`CableEnergyIo.kt:16-26`)

```kotlin
val inserted = maxAmount.coerceAtMost(network.maxCableTransfer)
    .coerceAtMost(capacity - amount)
```

**Problem**: `insert()` is capped per-**call**, not per-**tick**. Multiple generators can independently inject into the same cable in one tick, each up to `maxCableTransfer`. The network distributes only **once per tick**, so a burst can pile up far more energy than gets distributed.

**TechReborn**: Same `SimpleSidedEnergyContainer.getMaxInsert()` returns `transferRate` per call. But because TR splits energy back to each cable post-tick (`CableTickManager.java:82-88`), the excess doesn't accumulate in a single shared buffer — it's distributed across the physical cable entities.

### 6. No Energy Prioritization

IndRevUnbricked's energy network has **no sorting** — only proportional split by `maxInput`. Item/fluid networks have `ROUND_ROBIN`, `FARTHEST_FIRST`, `NEAREST_FIRST`, `RANDOM` — but energy has none.

**TechReborn**: `dispatchTransfer()` (`CableTickManager.java:137-140`) shuffles targets then sorts by `simulationResult` (ascending — emptiest first), ensuring machines that can accept the most get filled first. Not perfect, but better than blind proportional split.

### 7. Energy Config Not Side-Per-Side

`ConfigurationType.ENERGY` is not user-configurable per side on machines. You cannot tell a cable "only insert from the bottom" or "pull from the top." Side-config is critical for complex builds and cross-mod compatibility (e.g., Mekanism machines that expose different sides differently).

**TechReborn**: `PowerAcceptorBlockEntity` uses `SimpleSidedEnergyContainer` which checks `getMaxInsert(side)` per direction. `canAcceptEnergy(side)` is overridable per machine. Plus, `blockedSides` (`CableBlockEntity.java:97`) prevents double-transfer during a single tick.

---

## Summary Table

| Aspect | IndRevUnbricked | TechReborn |
|--------|----------------|------------|
| **Architecture** | Shared network-pool (`EnergyNetwork.energy`) | Per-cable `SimpleSidedEnergyContainer`, aggregated on tick |
| **Tier mixing** | BUG: last-scanned cable overwrites whole network's tier | Cables grouped by exact `transferRate` only |
| **Network split** | BUG: energy lost/voided on split; NBT save corrupts on topology change | Per-cable NBT; no shared state |
| **Distribution** | Proportional by `maxInput`, capped at `maxCableTransfer` per machine | Shuffle + sort emptiest-first, even split across cables post-tick |
| **Cross-mod compat** | Fragile: `supportsInsertion()` + direction probe + `MAX_VALUE` simulation | Same API, but vastly wider testing/userbase ensures better compat |
| **Intake throttle** | Per-call cap only, burst can overfill shared buffer | Same `transferRate` limit, but excess stays distributed in cable entities |
| **Prioritization** | None (proportional only) | Emptiest-first via simulation sort |
| **Side config** | Not user-configurable for energy | Per-side `getMaxInsert(side)`, `blockedSides` mask prevents double-transfer |
| **NBT persistence** | Single network-level save at arbitrary cable position | Per-cable save, no topology dependency |

---

## Files Referenced

### IndRevUnbricked
- `EnergyNetwork.kt` — network pool, distribution, tier-bug
- `EnergyNetworkState.kt` — split/merge energy loss, NBT save/load
- `CableEnergyIo.kt` — cable-side EnergyStorage proxy
- `factories.kt` — deepScan insertion logic, cross-mod compat
- `energyutils.kt` — `energyOf()` caching and API lookup
- `BasePipeBlockEntity.kt` — cable block entity base
- `MachineBlockEntity.kt` — machine-side energy storage inner class
- `machineinteractions.kt` — direct neighbor transfer (race condition)
- `IRConfig.kt` — cable rate config values
- `IREnergyStorage.java` — side-based energy storage base

### TechReborn
- `CableBlockEntity.java` — per-cable buffer + `SimpleSidedEnergyContainer`, `blockedSides`, `appendTargets()`
- `CableTickManager.java` — BFS gather, single-tick aggregate, `dispatchTransfer()` with shuffle+sort, even split
- `OfferedEnergyStorage.java` — record wrapping adjacent storage with `afterTransfer()` side-blocking
- `TRContent.java:458-501` — `Cables` enum with `transferRate`, `tier`, `canKill`
- `PowerAcceptorBlockEntity.java` — machine-side `SimpleSidedEnergyContainer` with per-side `getMaxInsert`/`getMaxExtract`
- `SimpleSidedEnergyContainer` (team.reborn.energy.api.base) — side-aware energy storage from TeamReborn Energy API
