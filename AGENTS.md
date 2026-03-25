# `problem4j-core` - Agent Instructions

Minimal Java library implementing RFC 7807/9457 "Problem Details". Immutable `Problem` model, builder, and exception.

## Build & Validate

- **Always run `./gradlew`** (default tasks: `spotlessApply build`) to format, compile, and test.
- If Spotless fails, run `./gradlew spotlessApply` to auto-fix, then re-run `./gradlew`.
- Java 17+ required for Gradle runtime; code compiles to Java 8 bytecode.
- Dependencies: `gradle/libs.versions.toml`. Refresh with `./gradlew --refresh-dependencies`.
- Always validate changes with a full `./gradlew` run before considering a task complete.

## Project Layout

| Path               | Contents                        |
|--------------------|---------------------------------|
| `src/main/java`    | Production source               |
| `src/test/java`    | Tests (JUnit Jupiter + AssertJ) |
| `build.gradle.kts` | Build config & Spotless setup   |
| `buildSrc`         | Custom Gradle plugins/scripts   |

## Agent Rules

- Do not use terminal commands (e.g., `cat`, `find`, `ls`) to read or list project files - use IDE/agent tools instead.
- Run tests once, save output to `build/test-run.log` inside the repo (`> build/test-run.log 2>&1`), then read from that
  file to extract errors. Never run the same test command multiple times, without changes in sources. Store test output
  in multiple files if you want to compare before/after changes (ex. `build/test-run-{i}.log`).

## Coding Rules

- No self-explaining comments - only add comments for non-obvious context.
- No wildcard imports.
- Follow existing code patterns and naming conventions.
- Let `spotlessApply` handle all formatting - never format manually.

## Test Conventions

- Method naming: `givenThis_whenThat_thenWhat`.
- No `// given`, `// when`, `// then` section comments.
- Cover both positive and negative cases.
- Use AssertJ for assertions.
