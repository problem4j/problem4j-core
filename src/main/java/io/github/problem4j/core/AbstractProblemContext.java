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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

/**
 * Base implementation of {@link ProblemContext} backed by a {@link Map}.
 *
 * <p>This abstract class provides common functionality for managing key-value pairs associated with
 * a problem context. Internally, it maintains a mutable map while exposing an immutable snapshot
 * via {@link #toMap()}.
 *
 * <p>Contexts created from another {@link ProblemContext} or a parent map perform a <em>shallow
 * copy</em> of the provided entries. Subsequent modifications to this context do not affect the
 * original source.
 *
 * <p>Implementations may extend this class to add domain-specific behavior or convenience methods.
 */
public abstract class AbstractProblemContext implements ProblemContext, Serializable {

  private static final long serialVersionUID = 1L;

  private final Map<String, String> context;

  /**
   * Creates an empty problem context.
   *
   * <p>The resulting context contains no entries and is independent of any parent context.
   */
  public AbstractProblemContext() {
    this(Collections.emptyMap());
  }

  /**
   * Creates a new problem context by copying all entries from the given {@link ProblemContext}.
   *
   * <p>The entries are copied at construction time. Changes to the provided context after
   * construction do not affect this context, and vice versa.
   *
   * @param context the parent context whose entries should be copied
   */
  public AbstractProblemContext(ProblemContext context) {
    this(context.toMap());
  }

  /**
   * Creates a new problem context by copying all entries from the given map.
   *
   * <p>The provided map is used only as a source of initial values. The underlying context
   * maintains its own internal storage, and modifications to the provided map after construction do
   * not affect this context.
   *
   * @param context the map whose entries should be copied into this context
   */
  public AbstractProblemContext(Map<String, String> context) {
    this.context = new HashMap<>(context);
  }

  /**
   * Checks if the context contains a value for the given key.
   *
   * @param key the key to check
   * @return {@code true} if the context contains a value for the key, {@code false} otherwise
   */
  @Override
  public boolean containsKey(String key) {
    return context.containsKey(key);
  }

  /**
   * Retrieves the value associated with the given key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value associated with the key, or {@code null} if no value is found
   */
  @Override
  public @Nullable String get(String key) {
    return context.get(key);
  }

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
  @Override
  public ProblemContext put(String key, @Nullable String value) {
    if (value == null) {
      context.remove(key);
    } else {
      context.put(key, value);
    }
    return this;
  }

  /**
   * Returns an immutable snapshot of the current context as a {@link Map}.
   *
   * @return an immutable {@link Map} containing the current context entries
   */
  @Override
  public Map<String, String> toMap() {
    return Collections.unmodifiableMap(context);
  }

  /**
   * Compares this context to the specified object for equality. Two contexts are considered equal
   * if they contain the same key-value pairs, regardless of their internal implementation or order.
   *
   * @param obj the reference object with which to compare
   * @return {@code true} if this context is equal to the specified object, {@code false} otherwise
   */
  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ProblemContext)) {
      return false;
    }
    ProblemContext context = (ProblemContext) obj;
    return Objects.equals(toMap(), context.toMap());
  }

  /**
   * Returns a hash code value for this context. The hash code is computed based on the entries in
   * the context, ensuring that equal contexts have the same hash code regardless of their internal
   * implementation or order.
   *
   * @return a hash code value for this context
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(toMap());
  }

  /**
   * Returns a string representation of this context. The string is formatted as {@code
   * "ProblemContext{key1=value1, key2=value2, ...}"}, where the entries are sorted by key for
   * consistent ordering.
   *
   * @return a string representation of this context
   */
  @Override
  public String toString() {
    return "ProblemContext{"
        + toMap().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(this::toEntryLine)
            .collect(Collectors.joining(", "))
        + "}";
  }

  private String toEntryLine(Map.Entry<String, String> entry) {
    return entry.getKey() + "=" + entry.getValue();
  }
}
