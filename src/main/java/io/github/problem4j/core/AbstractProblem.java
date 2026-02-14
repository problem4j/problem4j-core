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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Represents a problem detail according to the <a href="https://tools.ietf.org/html/rfc7807">RFC
 * 7807</a> (aka. <a href="https://tools.ietf.org/html/rfc9457">RFC 9457</a>) specification.
 *
 * <p>Instances of {@link Problem} are intended to be immutable. They provide standard fields such
 * as:
 *
 * <ul>
 *   <li>{@code type} - a URI identifying the type of the problem
 *   <li>{@code title} - a short, human-readable summary of the problem
 *   <li>{@code status} - the HTTP status code generated for this problem
 *   <li>{@code detail} - a human-readable explanation specific to this occurrence
 *   <li>{@code instance} - a URI identifying the specific occurrence of the problem
 * </ul>
 *
 * <p>In addition, custom extensions can be added to provide extra context.
 */
public abstract class AbstractProblem implements Problem, Serializable {

  private static final long serialVersionUID = 1L;

  private final URI type;
  private final String title;
  private final int status;
  private final @Nullable String detail;
  private final @Nullable URI instance;
  private final Map<String, Object> extensions;

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param status the HTTP status code applicable to this problem
   */
  public AbstractProblem(int status) {
    this(status, null);
  }

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   */
  public AbstractProblem(int status, @Nullable String detail) {
    this(
        Problem.BLANK_TYPE,
        ProblemStatus.findValue(status).map(ProblemStatus::getTitle).orElse(Problem.UNKNOWN_TITLE),
        status,
        detail,
        null,
        Collections.emptyMap());
  }

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   */
  public AbstractProblem(String title, int status) {
    this(title, status, null);
  }

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   */
  public AbstractProblem(String title, int status, @Nullable String detail) {
    this(Problem.BLANK_TYPE, title, status, detail, null, Collections.emptyMap());
  }

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem; must not be {@code null}
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   */
  public AbstractProblem(URI type, String title, int status) {
    this(type, title, status, null);
  }

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem; must not be {@code null}
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   */
  public AbstractProblem(URI type, String title, int status, @Nullable String detail) {
    this(type, title, status, detail, null, Collections.emptyMap());
  }

  /**
   * Constructs a new {@link AbstractProblem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem; must not be {@code null}
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   */
  public AbstractProblem(
      URI type,
      String title,
      int status,
      @Nullable String detail,
      @Nullable URI instance,
      @Nullable Map<String, Object> extensions) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.extensions = extensions != null ? new HashMap<>(extensions) : new HashMap<>();
  }

  /**
   * @return the URI identifying the type of this problem
   */
  @Override
  public URI getType() {
    return this.type;
  }

  /**
   * @return a short, human-readable title describing the problem
   */
  @Override
  public String getTitle() {
    return this.title;
  }

  /**
   * @return the HTTP status code generated for this problem
   */
  @Override
  public int getStatus() {
    return this.status;
  }

  /**
   * @return a detailed, human-readable explanation specific to this occurrence
   */
  @Override
  public @Nullable String getDetail() {
    return this.detail;
  }

  /**
   * @return a URI identifying the specific occurrence of the problem
   */
  @Override
  public @Nullable URI getInstance() {
    return this.instance;
  }

  /**
   * @return an unmodifiable set of custom extension keys present in this problem
   */
  @Override
  public Set<String> getExtensions() {
    return Collections.unmodifiableSet(extensions.keySet());
  }

  /**
   * Gets the value associated with a named extension.
   *
   * @param name the extension key
   * @return the value of the extension, or {@code null} if not present
   */
  @Override
  public @Nullable Object getExtensionValue(String name) {
    return extensions.get(name);
  }

  /**
   * Checks whether a given extension key is present.
   *
   * @param extension the extension key
   * @return {@code true} if the extension exists, {@code false} otherwise
   */
  @Override
  public boolean hasExtension(String extension) {
    return extensions.containsKey(extension);
  }

  /**
   * Returns a map of all extension members.
   *
   * <p>This method provides a complete view of all extension key-value pairs, which is useful when
   * callers need to handle extensions in bulk rather than querying them individually. It
   * complements {@link #getExtensionValue(String)} and {@link #hasExtension(String)} by exposing
   * the full extension payload at once.
   *
   * @return an unmodifiable map of extension members
   */
  @Override
  public Map<String, Object> getExtensionMembers() {
    return Collections.unmodifiableMap(extensions);
  }

  /**
   * Converts this problem instance into a {@link Problem} builder, pre-populated with its values.
   * Useful for creating a modified copy.
   *
   * @return a builder with the current problem's values
   */
  @Override
  public ProblemBuilder toBuilder() {
    return new ProblemBuilderImpl(this);
  }

  /**
   * Compares this problem instance to another object for equality. Two problem instances are
   * considered equal if they have the same type, title, status, detail, instance, and extension
   * members.
   *
   * @param obj the reference object with which to compare.
   * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Problem)) {
      return false;
    }
    Problem problem = (Problem) obj;
    return Objects.equals(getType(), problem.getType())
        && Objects.equals(getTitle(), problem.getTitle())
        && getStatus() == problem.getStatus()
        && Objects.equals(getDetail(), problem.getDetail())
        && Objects.equals(getInstance(), problem.getInstance())
        && Objects.equals(getExtensionMembers(), problem.getExtensionMembers());
  }

  /**
   * Returns a hash code value for this problem instance. The hash code is computed based on all
   * standard fields (type, title, status, detail, instance) and the extension members.
   *
   * @return a hash code value for this problem instance
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        getType(), getTitle(), getStatus(), getDetail(), getInstance(), getExtensionMembers());
  }

  /**
   * Returns a string representation of this problem instance, including all standard fields and
   * extensions in the format {@code "Problem{type=..., title=..., status=..., detail=...,
   * instance=..., ext1=..., ext2=..., ...}"}, where each field is included only if it is not {@code
   * null}. Extensions are sorted by their keys for consistent output. This method is useful for
   * debugging and logging purposes, providing a clear and comprehensive view of the problem's
   * state.
   *
   * @return a string representation of this problem instance
   */
  @Override
  public String toString() {
    List<String> entries = new ArrayList<>();
    entries.add("type=" + getType());
    entries.add("title=" + getTitle());
    entries.add("status=" + getStatus());
    if (getDetail() != null) {
      entries.add("detail=" + getDetail());
    }
    if (getInstance() != null) {
      entries.add("instance=" + getInstance());
    }

    getExtensionMembers().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> entries.add(entry.getKey() + "=" + entry.getValue()));

    return "Problem{" + String.join(", ", entries) + "}";
  }

  /** Represents a single key-value extension in a {@link Problem}. */
  public abstract static class AbstractExtension implements Extension, Serializable {

    private static final long serialVersionUID = 1L;

    private final String key;
    private @Nullable Object value;

    /**
     * Creates a new extension entry with the given key and value.
     *
     * @param key the extension field name, must not be {@code null}
     * @param value the extension value, may be {@code null}
     */
    public AbstractExtension(String key, @Nullable Object value) {
      this.key = key;
      this.value = value;
    }

    /**
     * @return the extension key
     */
    @Override
    public String getKey() {
      return key;
    }

    /**
     * @return the extension value
     */
    @Override
    public @Nullable Object getValue() {
      return value;
    }

    /**
     * Sets the extension value.
     *
     * @param value new value
     * @return the new value
     */
    @Override
    public @Nullable Object setValue(@Nullable Object value) {
      this.value = value;
      return value;
    }

    /**
     * Compares this extension to another object for equality. Two extensions are considered equal
     * if they have the same key and value.
     *
     * @param obj object to be compared for equality with this map entry
     * @return {@code true} if the specified object is equal to this map entry, {@code false}
     *     otherwise
     */
    @Override
    public boolean equals(@Nullable Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Extension)) {
        return false;
      }
      Extension extension = (Extension) obj;
      return Objects.equals(getKey(), extension.getKey())
          && Objects.equals(getValue(), extension.getValue());
    }

    /**
     * Returns the hash code value for this extension. The hash code is computed based on the key
     * and value of the extension, ensuring that equal extensions have the same hash code.
     *
     * @return the hash code value for this extension
     */
    @Override
    public int hashCode() {
      return Objects.hash(getKey(), getValue());
    }

    /**
     * Returns a string representation of this extension in the format {@code
     * "Extension{key=value}"}. This method is useful for debugging and logging purposes, providing
     * a clear and concise representation of the extension's key and value.
     *
     * @return a string representation of this extension
     */
    @Override
    public String toString() {
      return "Extension{" + getKey() + "=" + getValue() + "}";
    }
  }
}
