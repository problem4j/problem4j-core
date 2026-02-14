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

import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Context passed for problem processing. Used by {@link ProblemMapper}. Provides access to values
 * used for message interpolation or metadata enrichment.
 */
public interface ProblemContext {

  /**
   * Creates a new, empty {@link ProblemContext}.
   *
   * @return new {@link ProblemContext} instance
   */
  static ProblemContext create() {
    return new ProblemContextImpl();
  }

  /**
   * Checks if the context contains a value for the given key.
   *
   * @param key the key to check
   * @return {@code true} if the context contains a value for the key, {@code false} otherwise
   */
  boolean containsKey(String key);

  /**
   * Retrieves the value associated with the given key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value associated with the key, or {@code null} if no value is found
   */
  @Nullable String get(String key);

  /**
   * Associates the specified value with the specified key in the context and returns the context
   * itself. This allows for method chaining.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * context.put("userId", "12345")
   *        .put("traceId", "abcde");
   * }</pre>
   *
   * @param key the key with which the specified value is to be associated
   * @param value the value to be associated with the specified key
   * @return the previous value associated with the key, or {@code null} if there was no mapping for
   *     the key
   */
  ProblemContext put(String key, @Nullable String value);

  /**
   * Returns an immutable snapshot of the current context as a {@link Map}.
   *
   * @return an immutable {@link Map} containing the current context entries
   */
  Map<String, String> toMap();
}
