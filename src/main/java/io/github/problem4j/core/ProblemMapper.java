/*
 * Copyright 2025-2026 The Problem4J Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * <p>Implementations return an empty {@link ProblemBuilder} if {@code t} is {@code null} or the
 * exception class is not annotated with {@link ProblemMapping}, and may throw {@link
 * ProblemMappingException} if an error occurs during problem creation.
 *
 * <p>An empty builder is indistinguishable from a builder produced by an annotation with all blank
 * fields; callers should use {@link #isMappingCandidate(Throwable)} to determine whether an
 * exception is eligible for mapping before calling {@code toProblemBuilder}.
 *
 * @since 1.3.0
 */
public interface ProblemMapper {

  /**
   * Convert {@link Throwable} -&gt; {@link ProblemBuilder} according to its {@link ProblemMapping}
   * annotation. Such builder can be further extended or executed to create {@link Problem}
   * response.
   *
   * @param t {@link Throwable} to convert (may be {@code null})
   * @return a {@link ProblemBuilder} instance
   * @throws ProblemMappingException when something goes wrong while building the Problem
   * @since 1.3.0
   */
  default ProblemBuilder toProblemBuilder(@Nullable Throwable t) {
    return toProblemBuilder(t, null);
  }

  /**
   * Convert {@link Throwable} -&gt; {@link ProblemBuilder} according to its {@link ProblemMapping}
   * annotation. Such builder can be further extended or executed to create {@link Problem}
   * response.
   *
   * @param t {@link Throwable} to convert (may be {@code null})
   * @param context optional {@link ProblemContext} (may be {@code null})
   * @return a {@link ProblemBuilder} instance
   * @throws ProblemMappingException when something goes wrong while building the {@link Problem}
   * @since 1.3.0
   */
  ProblemBuilder toProblemBuilder(@Nullable Throwable t, @Nullable ProblemContext context);

  /**
   * Checks whether the given exception class is annotated with {@link ProblemMapping}.
   *
   * @param t {@link Throwable} to check (may be {@code null})
   * @return {@code true} if the exception is annotated with {@link ProblemMapping}, {@code false}
   *     otherwise
   * @since 1.3.0
   */
  default boolean isMappingCandidate(@Nullable Throwable t) {
    return t != null && t.getClass().isAnnotationPresent(ProblemMapping.class);
  }
}
