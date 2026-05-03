# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog][keepachangelog], and this project adheres to [Semantic Versioning][semver].

## [Unreleased]

### Added

- Add `DefaultProblemMapper` to replace `AbstractProblemMapper` and `ProblemMapper.create()`.
- Add `StatusTitleResolver` SPI to allow users to provide custom resolution of `status` code into `title`. To be used
  with HTTP status enums depending on the user's framework.
- Add `ProblemSupport` for common utilities.

### Changed

- Make all methods in `Problem`, `ProblemBuilder` and `ProblemContext` interfaces non-`default`.
- Make `Problem.Extension` not extend `Map.Entry` and instead be a simple data class with `name` and `value` properties.
- Reorganize interface `default` methods.

### Removed

- Remove deprecated methods from `ProblemBuilder`.
- Minify number of methods in `Problem` - plain `getExtensions()` returning `Map<String, Object>` is enough.
- Remove deprecated `ProblemStatus` enum from public API. The resolution of `status` code into `title` is still
  supported and available for extension via `StatusTitleResolver` SPI.
- Remove `AbstractProblem` class - use `Problem` objects created via `Problem.builder()` or static factory methods.
  Custom implementations of `Problem` can be created by implementing the `Problem` interface.
- Remove `AbstractProblemBuilder` class - use `Problem.builder()` static method to get default builder implementation.
  Custom implementations of `ProblemBuilder` can be created by implementing the `ProblemBuilder` interface.
- Remove `AbstractProblemContext` class - use `ProblemContext` objects created via `ProblemContext.create()` static
  method. Custom implementations of `ProblemContext` can be created by implementing the `ProblemContext` interface.
- Remove `AbstractProblemMapper` class - use `DefaultProblemMapper` instead.

## [1.4.3] - 2026-03-14

### Changed

- Deprecate `ProblemStatus` enum (see JavaDocs for details).

## [1.4.2] - 2026-02-26

### Fixed

- Make `ProblemBuilder` remove extension member if `null` values is passed, instead of silently ignoring it and keeping
  the previous value.
- Cleanup `default` methods in `ProblemBuilder` interface.

## [1.4.1] - 2026-02-17

### Fixed

- Change type from `Object` to `? extends Object` for generics in method arguments to support covariant arguments.

## [1.4.0] - 2026-02-16

### Added

- Add various static `Problem.of(...)` factory methods for in-place creation convenience.
- Add support for JSpecify annotations for nullability and Kotlin interop.
- Add support for **Java Platform Module System** if using Java version 9+, due to producing multi-release JAR
  artifacts.
  ```java
  module org.example.project {
      requires io.github.problem4j.core;
  }
  ```

## [1.3.3] - 2026-02-12

### Fixed

- Pull back from the idea of returning JSON-alike strings in `toString()` methods of this library's classes. While it
  was nice, since `Content-Type` is named `application/problem+json`, these strings were not 100% valid JSONs in all
  scenarios, and it could be tempting to use it as a response body somewhere. Instead of that, `toString` methods will
  now produce strings that are simple, useful for logging/debugging and JSON representations should be delegated to
  `jackson-databind` and [`problem4j-jackson`](https://github.com/problem4j/problem4j-jackson), or any other library
  that may be supported in the future.
- Seal the contract between `toProblemBuilder` method overloading in `AbstractProblemMapper` by making one method
  `final`. Any extensions this logic should be performed by overriding the second method as the first one must only
  delegate to it.
  ```java
  @Override
  public final ProblemBuilder toProblemBuilder(Throwable t) {
      return toProblemBuilder(t, null);
  }

  @Override
  public ProblemBuilder toProblemBuilder(Throwable t, ProblemContext context) {
      // ...
  ```
- Re-use instance of default implementation of `ProblemMapper` returned by `ProblemMapper.create()`, because it's
  thread-safe, stateless and immutable.
- Make resolution of deprecated HTTP status codes in `ProblemStatus` lazy, by delegating to nested class.
- Apply minor improvements to JavaDocs.

## [1.3.2] - 2026-01-29

### Fixed

- Resolve confusing naming of `ProblemBuilder` interface methods having singular naming, but actually taking plural
  arguments (and deprecate old ones).
- Apply minor fixes in JavaDocs, `toString` and exception messages.

## [1.3.1] - 2026-01-13

### Added

- Override `toString` in `AbstractProblemBuilder` (probably won't be used but useful in debug mode).
- Unify `toString` overrides of `AbstractProblem`, `AbstractProblemBuilder` and `AbstractProblemContext` to return
  output in similar style.
- Finalize missing JavaDocs (and fix various existing) - all `public` classes and methods now have proper JavaDocs.
- Annotate `ProblemMapping` with `@Documented` so it will appear in JavaDocs.

### Changed

- Use `HashMap` instead of `LinkedHashMap` for `AbstractProblemBuilder`, as order of insertion of extension members does
  not matter.

## [1.3.0] - 2025-12-24

This release of `problem4j-core` is considered a first "public" release, so the entry aggregates changes from the
`v1.0.x` to `v1.2.x` release lines into single entry.

### Added

- Add `Problem`, `ProblemBuilder` interfaces with static `Problem.builder()` method for fluent construction of `Problem`
  objects using default builder implementation.
- Add `ProblemException` class as a dedicated unchecked exception for error handling.
- Add `@ProblemMapping` annotation and `ProblemMapper` (with abstract base implementation) to allow declarative approach
  in converting exception instances into `Problem` objects with static `ProblemMapper.create()` to get its default
  implementation.
- All interfaces provided by this library have abstract implementations that can be extended or used as-is, so users can
  choose to implement their own or use the provided ones.

[keepachangelog]: https://keepachangelog.com/en/1.1.0/

[semver]: https://semver.org/spec/v2.0.0.html
