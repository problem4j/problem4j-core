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
package io.github.problem4j.core;

import org.jspecify.annotations.Nullable;

/**
 * Converts exceptions annotated with {@link ProblemMapping} into {@link ProblemBuilder} instances,
 * which can be further extended or executed to create {@link Problem} response.
 *
 * <p>Implementations may optionally make use of a {@link ProblemContext} to provide request- or
 * application-specific data such as trace IDs.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ProblemMapper mapper = ...;
 * ProblemContext context = ...;
 *
 * Throwable ex = new ValidationException("user-123", "email");
 *
 * if (mapper.isMappingCandidate(ex)) {
 *     Problem problem = mapper.toProblemBuilder(ex, context).build();
 * }
 * }</pre>
 *
 * <p>Implementations should return {@code null} if the exception class is not annotated with {@link
 * ProblemMapping}, and may throw {@link ProblemMappingException} if an error occurs during problem
 * creation.
 */
public interface ProblemMapper {

  /**
   * Creates a default {@link ProblemMapper} instance. The returned mapper provides the standard
   * mapping behavior defined by this library.
   *
   * <p>Note that because default implementation is thread-safe and stateless, since {@code v1.3.3}
   * this method returns a singleton instance.
   *
   * @return a new {@link ProblemMapper} instance
   */
  static ProblemMapper create() {
    return ProblemMapperImpl.INSTANCE;
  }

  /**
   * Convert {@link Throwable} -&gt; {@link ProblemBuilder} according to its {@link ProblemMapping}
   * annotation. Such builder can be further extended or executed to create {@link Problem}
   * response.
   *
   * @param t {@link Throwable} to convert (may be {@code null})
   * @return a {@link ProblemBuilder} instance
   * @throws ProblemMappingException when something goes wrong while building the Problem
   */
  ProblemBuilder toProblemBuilder(@Nullable Throwable t);

  /**
   * Convert {@link Throwable} -&gt; {@link ProblemBuilder} according to its {@link ProblemMapping}
   * annotation. Such builder can be further extended or executed to create {@link Problem}
   * response.
   *
   * @param t {@link Throwable} to convert (may be {@code null})
   * @param context optional {@link ProblemContext} (may be {@code null})
   * @return a {@link ProblemBuilder} instance
   * @throws ProblemMappingException when something goes wrong while building the {@link Problem}
   */
  ProblemBuilder toProblemBuilder(@Nullable Throwable t, @Nullable ProblemContext context);

  /**
   * Checks whether the given exception class is annotated with {@link ProblemMapping}.
   *
   * @param t {@link Throwable} to check (can be {@code null})
   * @return {@code true} if the exception is annotated with {@link ProblemMapping}, {@code false}
   *     otherwise
   */
  boolean isMappingCandidate(@Nullable Throwable t);
}
