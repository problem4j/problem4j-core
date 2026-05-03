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

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Context passed for problem processing. Used by {@link ProblemMapper}. Provides access to values
 * used for message interpolation or metadata enrichment.
 *
 * <p><b>Well-known contracts:</b>
 *
 * <p>Use {@link ProblemSupport#equals(ProblemContext, ProblemContext)}, {@link
 * ProblemSupport#hashCode(ProblemContext)}, and {@link ProblemSupport#toString(ProblemContext)} as
 * the canonical reference implementations of these methods.
 *
 * @since 1.3.0
 */
public interface ProblemContext {

  /**
   * Creates a new, empty {@link ProblemContext}.
   *
   * @return new {@link ProblemContext} instance
   * @since 1.3.0
   */
  static ProblemContext create() {
    return new DefaultProblemContext();
  }

  /**
   * Checks if the context contains a value for the given key.
   *
   * @param key the key to check
   * @return {@code true} if the context contains a value for the key, {@code false} otherwise
   * @since 1.3.0
   */
  default boolean containsKey(String key) {
    return toMap().containsKey(key);
  }

  /**
   * Retrieves the value associated with the given key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value associated with the key, or {@code null} if no value is found
   * @since 1.3.0
   */
  @Nullable String get(String key);

  /**
   * Associates the specified value with the specified key in the context and returns the context
   * itself. Passing {@code null} will unset context value. This allows for method chaining.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * context.put("userId", "12345")
   *        .put("traceId", "abcde");
   * }</pre>
   *
   * @param key the key with which the specified value is to be associated
   * @param value the value to be associated with the specified key (may be {@code null})
   * @return current context instance with the new entry added
   * @since 1.3.0
   */
  ProblemContext put(String key, @Nullable String value);

  /**
   * Associates all key-value pairs from the provided map with the context and returns the context
   * itself. Passing {@code null} entries in map will unset context values. This allows for method
   * chaining.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Map<String, String> entries = Map.of("userId", "12345", "traceId", "abcde");
   * context.putAll(entries);
   * }</pre>
   *
   * @param entries the map of key-value pairs to be added to the context
   * @return current context instance with the new entries added
   * @since 2.0.0
   */
  default ProblemContext putAll(Map<String, ? extends @Nullable String> entries) {
    entries.forEach(this::put);
    return this;
  }

  /**
   * Merges all key-value pairs from the provided context into this context and returns this context
   * instance. This allows for method chaining.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * ProblemContext otherContext = ...;
   * context.merge(otherContext);
   * }</pre>
   *
   * @param context the {@link ProblemContext} whose entries are to be merged into this context
   * @return current context instance with the entries from the provided context merged in
   * @since 2.0.0
   */
  default ProblemContext merge(ProblemContext context) {
    return putAll(context.toMap());
  }

  /**
   * Returns an immutable snapshot of the current context as a {@link Map}.
   *
   * @return an immutable {@link Map} containing the current context entries
   * @since 1.3.0
   */
  Map<String, String> toMap();
}
