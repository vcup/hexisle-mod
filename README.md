# hexisle

A Minecraft **1.20.1** mod built for four loaders — **Fabric**, **Quilt**, **Forge** and
**NeoForge** — from one shared codebase.

This repository is currently a **scaffold**: the build, the four platform modules and the
loader entry points are in place and building, and the mod does nothing yet beyond logging
that it has loaded. Feature work goes in `common/`.

## Layout

```
common/     shared, loader-agnostic code (no loader APIs) + shared resources
fabric/     Fabric entry point, fabric.mod.json
quilt/      Quilt entry point, quilt.mod.json
forge/      legacy Forge (net.minecraftforge) entry point, META-INF/mods.toml
neoforge/   NeoForge entry point, META-INF/mods.toml
```

`common/` is never published on its own. Each platform module folds its **source
directories** straight into its own compilation (see the root `build.gradle`), so the same
`.java` files end up in all four jars, each remapped into that platform's runtime namespace.

## Loader-agnostic code

Shared code must not reference Fabric, Quilt, Forge or NeoForge types. When it needs
something loader-specific, it goes through the `HexislePlatform` seam:

```java
// common/
String loader = HexislePlatform.platformName();   // "Fabric" | "Quilt" | "Forge" | "NeoForge"
if (HexislePlatform.modLoaded("hexcasting")) { ... }
```

Each platform module supplies its own implementation and registers it under
`META-INF/services/moe.vcup.hexisle.HexislePlatform`, so `common` stays free of loader
imports while still being able to ask loader questions at runtime.

## Building

```bash
./gradlew build
```

Artifacts land in each module's `build/libs/`:

| Module | Artifact |
| --- | --- |
| `common` | *(not shipped — its sources are compiled into the others)* |
| `fabric` | `hexisle-fabric-0.1.0.jar` |
| `quilt` | `hexisle-quilt-0.1.0.jar` |
| `forge` | `hexisle-forge-0.1.0.jar` |
| `neoforge` | `hexisle-neoforge-0.1.0.jar` |

Development runs are available per platform, e.g. `./gradlew :fabric:runClient`,
`:quilt:runServer`, `:forge:runClient`, `:neoforge:runServer`.

### Toolchain

- **JDK 21** runs the build. It is declared as a Gradle Java toolchain, so Gradle
  locates it automatically; no installation path is hardcoded, and if it is missing
  the Foojay resolver declared in `settings.gradle` provisions it.
- **Java 17 bytecode** is what actually ships. `options.release = 17` is set for every
  compilation, because Minecraft 1.20.1 runs on Java 17 and a Java 21 class file would
  fail to load with `UnsupportedClassVersionError`. Using `release` (rather than only a
  target version) also pins the compile classpath to the Java 17 API, so no 18+ API can
  slip in unnoticed. Verify with
  `javap -verbose -classpath <jar> moe.vcup.hexisle.Hexisle | findstr major` → `61`.
- **Gradle 8.14.2** via the wrapper. Stay on the 8.x line: the Architectury Loom version
  pinned here (`1.9.x`) is a Gradle 8 release.

## Why one toolchain for four loaders

All four platform modules use **Architectury Loom**. That fork is the only one covering the
whole set: its `ModPlatform` enum is exactly `FABRIC`, `FORGE`, `QUILT`, `NEOFORGE`, and it
understands each loader's metadata (`fabric.mod.json`, `quilt.mod.json`, `mods.toml`). The
loader for each module is chosen with `loom.platform` in that module's `gradle.properties`.

Using `fabric-loom` / `quilt-loom` / `architectury-loom` side by side is not an option:
they are forks of one another and all claim the same `loom` extension and the same
Minecraft caches.

Four 1.20.1-specific points, all load-bearing:

- **NeoForge on 1.20.1 is a Forge 47.x republish.** It ships as `net.neoforged:forge` and
  keeps the `net.minecraftforge.*` packages and the `forge` mod id. So `neoforge/` also uses
  `loom.platform=forge`; only the dependency coordinate differs from `forge/`. This is
  Architectury's documented path for 1.20.1.
- **Official (Mojang) mappings are used on every platform.** The shared sources are written
  against Mojang names, so all four compilations must resolve them identically. Loom then
  remaps per platform: Fabric and Quilt jars come out in intermediary names
  (`net.minecraft.class_2960`), while the Forge and NeoForge jars come out in official names
  (`net.minecraft.resources.ResourceLocation`).
- **ForgeGradle is deliberately not used.** ForgeGradle 6 throws
  `NumberFormatException: "26.1-snapshot-1"` from its own static initializer while the
  plugin is being applied, which aborts the build. Architectury Loom drives legacy Forge's
  MCP/userdev setup without it.
- **`quilt/` excludes the whole `net.fabricmc.fabric-api` group.** Quilted Fabric API *is*
  Fabric API for Quilt — it republishes every `net.fabricmc.fabric` class under the
  `org.quiltmc.quilted-fabric-api` group. Hex Casting, Patchouli and Cloth Config each
  declare their own plain Fabric API dependency, so without a project-wide exclusion a clean
  build reports ~940 duplicate classes while remapping and two disagreeing copies of the API
  reach the runtime classpath. See the comment in `quilt/build.gradle`.

## CI

`.github/workflows/build.yml` builds all four jars on JDK 21 and uploads them as artifacts.

## License

MIT — see [LICENSE.txt](LICENSE.txt).
