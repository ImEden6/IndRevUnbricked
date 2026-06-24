# Handoff: Dependency Fixes for IndRev Unbricked

## Goal
Fix the `runClient` crash caused by missing/incompatible mod dependencies so the game launches successfully.

## Current Progress

### Fixed: `LibGui` missing `jankson` and `libninepatch`
- **Root cause**: `LibGui-8.1.1+1.20.1.jar` was included via `include(files(...))` in `build.gradle`, but Fabric Loom's JAR-in-JAR (`META-INF/jars/`) mechanism does not work with local `files()` dependencies — the nested JARs are silently dropped during build.
- **Fix**: Extracted nested JARs from `LibGui-8.1.1+1.20.1.jar` (and all other local JARs) into `libs/`, then added them as explicit `modImplementation`/`implementation` + `include` entries in `build.gradle`. Done for:
  - `Jankson-Fabric-6.0.0+j1.2.3.jar`
  - `jankson-1.2.3.jar` (plain library, nested inside Jankson-Fabric)
  - `libninepatch-1.2.0.jar`
  - `fiber-0.23.0-2.jar` (nested inside Patchouli)
  - `omega-config-base-1.0.8-1.17.jar` (nested inside Magna)

### Fixed: `Magna` crash at startup
- **Root cause**: Pre-built `magna-1.10.0+1.20.1.jar` was compiled against incompatible intermediary mappings (`class_2520` does not exist in YARN 1.20.1+build.10).
- **Fix**: Built Magna v1.10.1 from the source project at `references/magna-1.20.1/` (which targets `yarn_mappings=1.20.1+build.10`), and deployed the resulting JAR as `libs/magna-1.10.1+1.20.1.jar`. Updated `gradle.properties` to reference the new version.

## What Worked
- Extracting `META-INF/jars/*` from all local JARs using `jar xf` and adding them as top-level dependencies.
- Using `implementation` for plain library JARs (not Fabric mods) and `modImplementation` for Fabric mod JARs, paired with `include` to embed them.

## What Didn't Work
- Relying on Fabric Loom's `include(files(...))` to preserve `META-INF/jars/` nested JARs — they are **not** carried over to the final build JAR.
- Trying to fetch `jankson`/`libninepatch` from the CottonMC maven (`server.bbkr.space`) — it is no longer publicly accessible.

## Next Steps
1. **Replace or fix Magna**: Find a Magna JAR that is correctly compiled for Minecraft 1.20.1 (with proper intermediary mappings), or remove/disable Magna and the features that depend on it.
2. **Verify Patchouli**: After Magna is resolved, verify Patchouli and fiber load correctly (the fiber dependency was added but hasn't been fully tested since Magna blocks earlier).
3. **Clean up `libs/`**: Consider removing the old nested JARs from the original mod JARs (e.g., the `META-INF/jars/` directories inside `LibGui-*.jar` are no longer needed since their contents are extracted), though they don't cause harm.
4. **Build and run**: Run `./gradlew runClient` to test the full startup.
