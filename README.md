# Problem4J Core

[![Build Status](https://github.com/problem4j/problem4j-core/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/problem4j/problem4j-core/actions/workflows/gradle-build.yml)
[![Sonatype](https://img.shields.io/maven-central/v/io.github.problem4j/problem4j-core)](https://central.sonatype.com/artifact/io.github.problem4j/problem4j-core)
[![License](https://img.shields.io/github/license/problem4j/problem4j-core)](https://github.com/problem4j/problem4j-core/blob/main/LICENSE)

This library provides a minimal, framework-agnostic Java model of the [RFC 7807][rfc7807] "Problem Details" object, with
an immutable `Problem` class and a fluent `ProblemBuilder` for convenient construction.

> Note that [RFC 7807][rfc7807] was later extended in [RFC 9457][rfc9457], however core concepts remain the same.

It is intended to be used as a **foundation** for other libraries or applications that add framework-specific behavior
(e.g. Jackson, Spring - see [Problem4J Links](#problem4j-links) chapter).

## Table of Contents

- [Features](#features)
- [Example](#example)
- [Usage](#usage)
- [Project Status](#project-status)
- [Problem4J Links](#problem4j-links)
- [Building from source](#building-from-source)

## Features

- ✅ Immutable `Problem` data model.
- ✅ Dedicated unchecked `ProblemException` to be used in error handling.
- ✅ Builder pattern for fluent construction.
- ✅ Static `Problem.of(...)` factory methods for in-place creation convenience (since `v1.4.0`).
- ✅ `@ProblemMapping` annotation and `ProblemMapper` to allow declarative approach in converting exception instances
  into `Problem` objects.
- ✅ Serializable and easy to log or format.
- ✅ HTTP-agnostic (no external dependencies).
- ✅ Follows [RFC 7807][rfc7807] semantics:
    - `type` (URI),
    - `title` (short summary),
    - `status` (numeric code),
    - `detail` (detailed description),
    - `instance` (URI to the specific occurrence),
    - custom field extensions.
- ✅ Integrated with JSpecify annotations for nullability and Kotlin interop (since `v1.4.0`).
- ✅ Supports Java version 8+, but due to producing multi-release JAR, can support **Java Platform Module System** if
  using Java version 9+ (since `v1.4.0`).
  ```java
  module org.example.project {
      requires io.github.problem4j.core;
  }
  ```

## Example

Throw an instance of `ProblemException`.

```java
import io.github.problem4j.core.Problem;
import io.github.problem4j.core.ProblemException;

Problem problem =
    Problem.builder()
        .type("https://example.com/errors/invalid-request")
        .title("Invalid Request")
        .status(400)
        .detail("not a valid json")
        .instance("https://example.com/instances/1234")
        .build();
throw new ProblemException(problem);
```

Throw an exception annotated with `@ProblemMapping`.

```java
import io.github.problem4j.core.ProblemMapping;
import io.github.problem4j.core.ProblemMapper;

@ProblemMapping(
        type = "https://example.org/probs/tests",
        title = "Test problem",
        status = 400,
        detail = "failed: {message}",
        extensions = {"subject"})
public class MessageException extends RuntimeException {

    private final String subject;

    public MessageException(String subject, String message) {
        super(message);
        this.subject = subject;
    }
}

MessageException ex = new MessageException("sub", "boom");

ProblemMapper mapper = ProblemMapper.create();
Problem problem = mapper.toProblemBuilder(ex).build();
```

## Usage

Add library as dependency to Maven or Gradle. See the actual versions on [Maven Central][maven-central]. **Java 8** or
higher is required to use this library.

1. Maven:
   ```xml
   <dependencies>
       <dependency>
           <groupId>io.github.problem4j</groupId>
           <artifactId>problem4j-core</artifactId>
           <version>1.3.3</version>
       </dependency>
   </dependencies>
   ```
2. Gradle (Groovy or Kotlin DSL):
   ```groovy
   dependencies {
       implementation("io.github.problem4j:problem4j-core:1.3.3")
   }
    ```

## Project Status

[![Status: Feature Complete](https://img.shields.io/badge/feature%20complete-darkblue?label=status)](#project-status)

**Problem4J Core** is considered *feature complete*. Only **bug fixes** will be added. New features may be included only
if there is a strong justification for them; otherwise, future projects are expected to build on this one as a
dependency.

## Problem4J Links

- [`problem4j.github.io`](https://problem4j.github.io) - Full documentation of all projects from Problem4J family.
- [`problem4j-core`][problem4j-core] - Core library defining `Problem` model and `ProblemException`.
- [`problem4j-jackson`][problem4j-jackson] - Jackson module for serializing and deserializing `Problem` objects.
- [`problem4j-spring`][problem4j-spring] - Spring modules extending `ResponseEntityExceptionHandler` for handling
  exceptions and returning `Problem` responses.

## Building from source

<details>
<summary><b>Expand...</b></summary>

Gradle **9.x+** requires **Java 17+** to run, but higher Java versions can also be used. All modules of this project are
compiled using a **Java 8 toolchain**, so the produced artifacts are compatible with **Java 8**, regardless of the Java
version Gradle runs on.

```bash
./gradlew build
```

To execute tests use `test` task.

```bash
./gradlew test
```

To format the code according to the style defined in [`build.gradle.kts`](./build.gradle.kts) rules use `spotlessApply`
task. **Note** that **building will fail** if code is not properly formatted.

```bash
./gradlew spotlessApply
```

To publish the built artifacts to local Maven repository, use `publishToMavenLocal` task. It produces artifacts with
`0.0.0-SNAPSHOT` version placeholder, so they won't conflict with any released versions in your local repository.

```bash
./gradlew publishToMavenLocal
```

Note that for using Maven Local artifacts in target projects, you need to add `mavenLocal()` repository.

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}
```

</details>

[maven-central]: https://central.sonatype.com/artifact/io.github.problem4j/problem4j-core

[problem4j-core]: https://github.com/problem4j/problem4j-core

[problem4j-jackson]: https://github.com/problem4j/problem4j-jackson

[problem4j-spring]: https://github.com/problem4j/problem4j-spring

[rfc7807]: https://datatracker.ietf.org/doc/html/rfc7807

[rfc9457]: https://datatracker.ietf.org/doc/html/rfc9457
