/*
 * Copyright (c) 2025-2026 Problem4J Team & Contributors
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

import java.net.URI;
import java.util.Map;
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
 * In addition, custom extensions can be added to provide extra context.
 */
public interface Problem {

  /** Default type URI for generic problems. */
  URI BLANK_TYPE = URI.create("about:blank");

  /**
   * Fallback to resolution of {@code title} field while calling {@link ProblemBuilder#build()}
   * method.
   */
  String UNKNOWN_TITLE = "Unknown Error";

  /** MIME content type for this problem. */
  String CONTENT_TYPE = "application/problem+json";

  /**
   * Creates a new builder for constructing {@link Problem} instances.
   *
   * @return a new {@link ProblemBuilder}
   */
  static ProblemBuilder builder() {
    return new ProblemBuilderImpl();
  }

  /**
   * Creates a new {@link Problem} instance with the given HTTP status code.
   *
   * @param status the HTTP status code applicable to this problem
   * @return a new {@link Problem} instance
   */
  static Problem of(int status) {
    return builder().status(status).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given {@link ProblemStatus}.
   *
   * @param status the {@link ProblemStatus} applicable to this problem
   * @return a new {@link Problem} instance
   */
  static Problem of(ProblemStatus status) {
    return builder().status(status).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @return a new {@link Problem} instance
   */
  static Problem of(String title, int status) {
    return builder().title(title).status(status).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @return a new {@link Problem} instance
   */
  static Problem of(String title, int status, @Nullable URI instance) {
    return builder().title(title).status(status).instance(instance).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(String title, int status, @Nullable Map<String, @Nullable Object> extensions) {
    return builder().title(title).status(status).extensions(extensions).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      String title,
      int status,
      @Nullable URI instance,
      @Nullable Map<String, @Nullable Object> extensions) {
    return builder().title(title).status(status).instance(instance).extensions(extensions).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @return a new {@link Problem} instance
   */
  static Problem of(String title, int status, @Nullable String detail) {
    return builder().title(title).status(status).detail(detail).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @return a new {@link Problem} instance
   */
  static Problem of(String title, int status, @Nullable String detail, @Nullable URI instance) {
    return builder().title(title).status(status).detail(detail).instance(instance).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      String title,
      int status,
      @Nullable String detail,
      @Nullable Map<String, @Nullable Object> extensions) {
    return builder().title(title).status(status).detail(detail).extensions(extensions).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      String title,
      int status,
      @Nullable String detail,
      @Nullable URI instance,
      @Nullable Map<String, @Nullable Object> extensions) {
    return builder()
        .title(title)
        .status(status)
        .detail(detail)
        .instance(instance)
        .extensions(extensions)
        .build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @return a new {@link Problem} instance
   */
  static Problem of(URI type, String title, int status) {
    return builder().type(type).title(title).status(status).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @return a new {@link Problem} instance
   */
  static Problem of(URI type, String title, int status, @Nullable URI instance) {
    return builder().type(type).title(title).status(status).instance(instance).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      URI type, String title, int status, @Nullable Map<String, @Nullable Object> extensions) {
    return builder().type(type).title(title).status(status).extensions(extensions).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      URI type,
      String title,
      int status,
      @Nullable URI instance,
      @Nullable Map<String, @Nullable Object> extensions) {
    return builder()
        .type(type)
        .title(title)
        .status(status)
        .instance(instance)
        .extensions(extensions)
        .build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @return a new {@link Problem} instance
   */
  static Problem of(URI type, String title, int status, @Nullable String detail) {
    return builder().type(type).title(title).status(status).detail(detail).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @return a new {@link Problem} instance
   */
  static Problem of(
      URI type, String title, int status, @Nullable String detail, @Nullable URI instance) {
    return builder()
        .type(type)
        .title(title)
        .status(status)
        .detail(detail)
        .instance(instance)
        .build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      URI type,
      String title,
      int status,
      @Nullable String detail,
      @Nullable Map<String, @Nullable Object> extensions) {
    return builder()
        .type(type)
        .title(title)
        .status(status)
        .detail(detail)
        .extensions(extensions)
        .build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   * @return a new {@link Problem} instance
   */
  static Problem of(
      URI type,
      String title,
      int status,
      @Nullable String detail,
      @Nullable URI instance,
      @Nullable Map<String, @Nullable Object> extensions) {
    return builder()
        .type(type)
        .title(title)
        .status(status)
        .detail(detail)
        .instance(instance)
        .extensions(extensions)
        .build();
  }

  /**
   * Creates a named extension for use in a {@link Problem}.
   *
   * @param key the extension key, must not be {@code null}
   * @param value the extension value
   * @return a new {@link Problem.Extension} instance
   * @throws IllegalArgumentException if the {@code key} is {@code null}
   */
  static Extension extension(@Nullable String key, @Nullable Object value) {
    // with JSpecify, the nullability check might not be necessary, kept to not break existing
    // behavior, may be revisited in future major versions
    if (key == null) {
      throw new IllegalArgumentException("key cannot be null");
    }
    return new ProblemImpl.ExtensionImpl(key, value);
  }

  /**
   * @return the URI identifying the type of this problem
   */
  URI getType();

  /**
   * @return a short, human-readable title describing the problem
   */
  String getTitle();

  /**
   * @return the HTTP status code generated for this problem
   */
  int getStatus();

  /**
   * @return a detailed, human-readable explanation specific to this occurrence
   */
  @Nullable String getDetail();

  /**
   * @return a URI identifying the specific occurrence of the problem
   */
  @Nullable URI getInstance();

  /**
   * @return an unmodifiable set of custom extension keys present in this problem
   */
  Set<String> getExtensions();

  /**
   * Gets the value associated with a named extension.
   *
   * @param name the extension key
   * @return the value of the extension, or {@code null} if not present
   */
  @Nullable Object getExtensionValue(String name);

  /**
   * Checks whether a given extension key is present.
   *
   * @param extension the extension key
   * @return {@code true} if the extension exists, {@code false} otherwise
   */
  boolean hasExtension(String extension);

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
  Map<String, Object> getExtensionMembers();

  /**
   * Converts this problem instance into a {@link Problem} builder, pre-populated with its values.
   * Useful for creating a modified copy.
   *
   * @return a builder with the current problem's values
   */
  ProblemBuilder toBuilder();

  /**
   * A convenience method to verify if {@code type} field was assigned, as {@link #BLANK_TYPE} also
   * mean that type is unassigned.
   *
   * @return {@code true} if {@code type} is assigned to a non-blank value, {@code false} otherwise
   */
  default boolean isTypeNonBlank() {
    return !getType().equals(BLANK_TYPE) && !getType().toString().isEmpty();
  }

  /** Represents a single key-value extension in a {@link Problem}. */
  interface Extension extends Map.Entry<String, @Nullable Object> {

    /**
     * @return the extension key
     */
    @Override
    String getKey();

    /**
     * @return the extension value
     */
    @Override
    @Nullable Object getValue();

    /**
     * Sets the extension value.
     *
     * @param value new value
     * @return the new value
     */
    @Override
    @Nullable Object setValue(@Nullable Object value);
  }
}
