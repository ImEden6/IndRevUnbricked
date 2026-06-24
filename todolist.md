# Code Review Fix Plan

## HIGH Priority

### 1. Fix `MixinWItemSlot.java` / `WCustomTabPanel.java` mismatch

- **What:** `src/main/java/me/mervyn/indrev_unbricked/mixin/client/MixinWItemSlot.java` contains the full `WCustomTabPanel` widget class, not a mixin. The mixin config at `src/main/resources/indrev_unbricked.mixins.json` references `"client.MixinWItemSlot"` which does not exist as a mixin.
- **Why:** Will cause a mixin load error at runtime — Mixin tries to apply non-existent mixin class. The widget class also won't be loadable since it's named wrong.
- **Evidence:**
  - `MixinWItemSlot.java` — class declaration is `public class WCustomTabPanel extends WPanel`, no `@Mixin` annotation or mixin target.
  - `indrev_unbricked.mixins.json:26` — `"client.MixinWItemSlot"` listed under `"client"` array.

### 2. Fix inverted chance logic in `IRRecipe.kt:57`

- **What:** `if (chance >= 1.0 || random != null && random.nextDouble() > chance)` — `>` should be `<`.
- **Why:** A recipe with `"chance": 0.8` (80% probability) currently drops only ~20% of the time because `nextDouble() > 0.8` is true only 20% of the time. The correct probability is `nextDouble() < 0.8` (true 80%).
- **Evidence:**
  - Code: `src/main/kotlin/.../recipes/machines/IRRecipe.kt:54-58`
  - Recipe data: `src/main/resources/data/.../recipes/pulverizer/sulfur_dust.json:12-14` has `"chance": 0.8` — intended as 80% drop rate.

### 3. Remove `jcenter()` from `settings.gradle`

- **What:** Remove the `jcenter()` repository entry.
- **Why:** JCenter is read-only/sunset since 2022. No longer receives updates.

### 4. Fix `compatibilityLevel` in mixins config

- **What:** Change `"compatibilityLevel": "JAVA_8"` to `"JAVA_17"` in `indrev_unbricked.mixins.json`.
- **Why:** Project targets Java 17 (`build.gradle:221`). Misleading value is harmless but incorrect.

---

## MEDIUM Priority

### 5. Replace deprecated `ScreenHandlerRegistry`

- **What:** `utils.kt` uses `ScreenHandlerRegistry.registerExtended()` which is removed in later Fabric versions.
- **Why:** May break when upgrading Fabric loader/API. Use `ExtendedScreenHandlerType` constructor directly.

### 6. Replace deprecated `FabricModelPredicateProviderRegistry`

- **What:** `IndustrialRevolutionClient.kt:165` uses the Fabric API version.
- **Why:** Deprecated in favor of vanilla `ModelPredicateProviderRegistry` in 1.20.1.

### 7. Replace deprecated `archivesBaseName`

- **What:** `build.gradle:12` uses `archivesBaseName` (removed in Gradle 8).
- **Why:** Will break on Gradle 8+. Use `base { archivesName = "..." }`.

### 8. Replace hardcoded lib versions with project properties

- **What:** `build.gradle:157-175` has hardcoded versions for Jankson, fiber, omega-config, etc.
- **Why:** Inconsistent with the rest of the build file; makes version bumps harder.

### 9. Replace `assert` with proper guard in `IRRecipe.kt:74`

- **What:** `assert(input.size == 1)` — assertions are disabled at runtime by default on the JVM.
- **Why:** If multi-input recipes reach this path, they silently pass instead of failing predictably.
- **Evidence:** `src/main/kotlin/.../recipes/machines/IRRecipe.kt:74`

### 10. Clean up unused imports

- **What:** Remove unused imports across many files.
- **Why:** Code clutter; minor compilation overhead.
- **Evidence:** e.g. `IndustrialRevolution.kt:20` imports `ClientLifecycleEvents` (never used, client class in a server-side initializer).

---

## LOW Priority

### 11. Fix `draw2Colors` rendering in `clientutils.kt`

- **What:** Both `BufferRenderer.draw(this)` calls are commented out.
- **Why:** The function may not render anything in 1.20.1 without those draw calls.
- **Evidence:** `src/main/kotlin/.../utils/clientutils.kt:56,62`

### 12. Review `hiddenitems.kt` approach

- **What:** Hardcoded string list of ~45 item IDs to hide from the creative menu.
- **Why:** Fragile — requires code changes to add/remove hidden items. Consider tags, annotations, or a config-driven approach.
- **Evidence:** `src/main/kotlin/.../utils/hiddenitems.kt`

### 13. Review `object` singleton state for multi-world safety

- **What:** Several `object` singletons (e.g. `GlobalStateController`, `MachineRegistry.MAP`, `itemApiCache`) store mutable per-world or per-session state.
- **Why:** On dedicated servers with multiple worlds, or during `/reload`, stale state from old worlds persists and can cause wrong lookups or memory leaks.
- **Evidence:** `MachineRegistry.kt:178` — `val MAP = hashMapOf<Identifier, MachineRegistry>()` is a global static map; `itemutils.kt:10` — `val itemApiCache = WeakHashMap<World, ...>()`.

### 14. Fix potential race condition in `itemApiCache`

- **What:** `WeakHashMap` is not thread-safe; item lookups may happen from multiple threads.
- **Why:** Can cause `ConcurrentModificationException` or data corruption under load.
- **Evidence:** `src/main/kotlin/.../utils/itemutils.kt:10`

### 15. Batch `Transaction.openOuter()` usage

- **What:** `energyutils.kt` and `IRFluidTank.kt` create a new transaction on every single energy/fluid operation.
- **Why:** Transaction creation has overhead; when machines tick, hundreds of operations per second can cause performance degradation.
- **Evidence:** `src/main/kotlin/.../utils/energyutils.kt:47-58`, `src/main/kotlin/.../utils/IRFluidTank.kt:60-90`
