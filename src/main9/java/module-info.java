/*
 * Copyright (c) 2025-2026 The Problem4J Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.jspecify.annotations.NullMarked;

/**
 * Core classes and interfaces for the Problem4J set of libraries, implementing <a
 * href="https://tools.ietf.org/html/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>.
 *
 * <p>RFC 7807 was later extended in <a href="https://tools.ietf.org/html/rfc9457">RFC 9457</a>,
 * however core concepts remain the same.
 *
 * <p>This package provides the foundational types for modeling "Problem Details" objects, which are
 * used to convey machine-readable error details in HTTP responses.
 *
 * <p>Key components include:
 *
 * <ul>
 *   <li>{@code Problem}: The central immutable model representing a problem detail.
 *   <li>{@code ProblemBuilder}: A fluent builder for creating {@code Problem} instances.
 *   <li>{@code ProblemException}: A runtime exception that wraps a {@code Problem}, allowing it to
 *       be thrown and handled by structured error handlers.
 *   <li>{@code ProblemMapping}: An annotation for configuring how exceptions should be mapped to
 *       {@code Problem} instances, supporting message interpolation and automatic mapping.
 *   <li>{@code ProblemMapper}: A utility for mapping exceptions to {@code Problem} instances,
 *       supporting annotation-based configuration for automatic mapping.
 * </ul>
 *
 * <p>This library is framework-agnostic and intended to be used as a basis for higher-level
 * integrations (e.g., with Spring Boot, Micronaut, or other web frameworks).
 */
@NullMarked
module io.github.problem4j.core {
  requires static org.jspecify;

  exports io.github.problem4j.core;
}
