# Code Review Action Items

## HIGH Priority

- [ ] **Fix `MixinWItemSlot.java`/`WCustomTabPanel.java` mismatch** — `MixinWItemSlot.java` contains `WCustomTabPanel` class (not a mixin). The mixin config references `client.MixinWItemSlot` which doesn't exist. Either rename the file or move the class, and remove from mixin config.

- [ ] **Fix inverted chance logic** — `src/main/kotlin/.../recipes/machines/IRRecipe.kt:57`: `random.nextDouble() > chance` should be `< chance`. Currently items drop when they shouldn't and vice versa.

- [ ] **Remove `jcenter()` from `settings.gradle:2`** — JCenter is read-only/sunset. It adds no value over `gradlePluginPortal()`.

- [ ] **Fix `compatibilityLevel` in mixins JSON** — `indrev_unbricked.mixions.json:4` says `JAVA_8` but target is Java 17. Change to `JAVA_17`.

## MEDIUM Priority

- [ ] **Replace deprecated `ScreenHandlerRegistry`** — `utils.kt` uses `ScreenHandlerRegistry.registerExtended()` which is removed in later Fabric. Use `ExtendedScreenHandlerType` constructor directly.

- [ ] **Replace deprecated `FabricModelPredicateProviderRegistry`** — `IndustrialRevolutionClient.kt:165` uses the Fabric version which is deprecated. Use vanilla `ModelPredicateProviderRegistry` in 1.20.1.

- [ ] **Replace deprecated `archivesBaseName`** — `build.gradle:12`: Removed in Gradle 8. Use `base { archivesName = "..." }` block.

- [ ] **Replace hardcoded lib versions with project properties** — `build.gradle:157-175`: Jankson, fiber, omega-config versions are hardcoded instead of using `gradle.properties`.

- [ ] **Fix `assert(input.size == 1)` in `IRRecipe.kt:74`** — Assertions are disabled at runtime by default. Make this a proper check or remove.

- [ ] **Remove unused imports** — Many files (`IndustrialRevolution.kt`, `IndustrialRevolutionClient.kt`, multiple mixins) have unused imports.

## LOW Priority

- [ ] **Refactor `draw2Colors` in `clientutils.kt`** — `BufferRenderer.draw(this)` calls are commented out; the function may not render at all in 1.20.1.

- [ ] **Review `hiddenitems.kt` approach** — Hardcoded string list of IDs to hide from creative menu. Consider using tags or an annotation system instead.

- [ ] **Review `object` singleton state** — `GlobalStateController` and other `object`-based classes store per-world state which is incorrect for multiplayer worlds.

- [ ] **Fix race condition in `itemApiCache`** — `itemutils.kt:10`: `WeakHashMap` is not thread-safe but may be accessed from multiple threads.

- [ ] **Batch `Transaction.openOuter()` usage** — `energyutils.kt` and `IRFluidTank.kt` create new transactions on every energy/fluid operation. Batch where possible for performance.

- [ ] **Remove unused mixin entries from config** — `MixinClientPlayerInteractionManager`, `MixinItemRenderer`, and `MixinLivingEntity` still appear in the mixin config but have no active mixins. Their presence adds tiny overhead at startup.
