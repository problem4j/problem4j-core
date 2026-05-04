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

import static java.util.Collections.emptyMap;

import java.net.URI;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Represents a problem detail according to the <a href="https://tools.ietf.org/html/rfc7807">RFC
 * 7807</a> (and <a href="https://tools.ietf.org/html/rfc9457">RFC 9457</a>) specification.
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
 *
 * <p>Use {@link ProblemSupport#equals(Problem, Problem)}, {@link ProblemSupport#hashCode(Problem)},
 * and {@link ProblemSupport#toString(Problem)} as the canonical reference implementations of these
 * methods.
 *
 * @since 1.3.0
 */
public interface Problem {

  /**
   * Default type URI for generic problems.
   *
   * @since 1.3.0
   */
  URI BLANK_TYPE = URI.create("about:blank");

  /**
   * Fallback {@code title} used when no title can be resolved during {@link
   * ProblemBuilder#build()}.
   *
   * @since 1.4.0
   */
  String UNKNOWN_TITLE = "Unknown Error";

  /**
   * MIME content type for problem instances.
   *
   * @since 1.3.0
   */
  String CONTENT_TYPE = "application/problem+json";

  /**
   * Creates a new builder for constructing {@link Problem} instances.
   *
   * @return a new {@link ProblemBuilder}
   * @since 1.3.0
   */
  static ProblemBuilder builder() {
    return new DefaultProblemBuilder();
  }

  /**
   * Creates a new {@link Problem} instance with the given HTTP status code.
   *
   * @param status the HTTP status code applicable to this problem
   * @return a new {@link Problem} instance
   * @since 1.4.0
   */
  static Problem of(int status) {
    return builder().status(status).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @return a new {@link Problem} instance
   * @since 1.4.0
   */
  static Problem of(int status, @Nullable String detail) {
    return builder().status(status).detail(detail).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @return a new {@link Problem} instance
   * @since 1.4.0
   */
  static Problem of(String title, int status) {
    return builder().title(title).status(status).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @return a new {@link Problem} instance
   * @since 1.4.0
   */
  static Problem of(String title, int status, @Nullable String detail) {
    return builder().title(title).status(status).detail(detail).build();
  }

  /**
   * Creates a new {@link Problem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @return a new {@link Problem} instance
   * @since 1.4.0
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
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @return a new {@link Problem} instance
   * @since 1.4.0
   */
  static Problem of(URI type, String title, int status, @Nullable String detail) {
    return builder().type(type).title(title).status(status).detail(detail).build();
  }

  /**
   * Creates a named extension for use in a {@link Problem}.
   *
   * @param name the extension name
   * @param value the extension value
   * @return a new {@link Problem.Extension} instance
   * @since 1.3.0
   */
  static Extension extension(String name, @Nullable Object value) {
    return new DefaultProblem.DefaultExtension(name, value);
  }

  /**
   * Gets the URI identifying the type of this problem.
   *
   * @return the URI identifying the type of this problem
   * @since 1.3.0
   */
  default URI getType() {
    return BLANK_TYPE;
  }

  /**
   * A convenience method to verify if {@code type} field was assigned, as {@link #BLANK_TYPE} also
   * means that type is unassigned.
   *
   * @return {@code true} if {@code type} is assigned to a non-blank value, {@code false} otherwise
   * @since 1.3.0
   */
  default boolean isTypeNonBlank() {
    return ProblemSupport.isTypeNonBlank(getType());
  }

  /**
   * Gets a short, human-readable title describing the problem.
   *
   * @return a short, human-readable title describing the problem
   * @since 1.3.0
   */
  String getTitle();

  /**
   * Gets the HTTP status code generated for this problem.
   *
   * @return the HTTP status code generated for this problem
   * @since 1.3.0
   */
  int getStatus();

  /**
   * Gets a detailed, human-readable explanation specific to this occurrence.
   *
   * @return a detailed, human-readable explanation specific to this occurrence
   * @since 1.3.0
   */
  default @Nullable String getDetail() {
    return null;
  }

  /**
   * Gets a URI identifying the specific occurrence of the problem.
   *
   * @return a URI identifying the specific occurrence of the problem
   * @since 1.3.0
   */
  default @Nullable URI getInstance() {
    return null;
  }

  /**
   * Returns an unmodifiable map of all custom extension members.
   *
   * @return an unmodifiable map of extension members
   * @since 2.0.0
   */
  default Map<String, Object> getExtensions() {
    return emptyMap();
  }

  /**
   * Converts this problem instance into a {@link Problem} builder, pre-populated with its values.
   * Useful for creating a modified copy.
   *
   * @return a builder with the current problem's values
   * @since 1.3.0
   */
  default ProblemBuilder toBuilder() {
    return new DefaultProblemBuilder(this);
  }

  /**
   * Represents a single key-value extension in a {@link Problem}.
   *
   * <p><b>Well-known contracts:</b>
   *
   * <p>Use {@link ProblemSupport#equals(Problem.Extension, Problem.Extension)}, {@link
   * ProblemSupport#hashCode(Problem.Extension)}, and {@link
   * ProblemSupport#toString(Problem.Extension)} as the canonical reference implementations of these
   * methods.
   *
   * @since 1.3.0
   */
  interface Extension {

    /**
     * Gets the extension name.
     *
     * @return the extension name
     * @since 2.0.0
     */
    String getName();

    /**
     * Gets the extension value.
     *
     * @return the extension value
     * @since 1.3.0
     */
    @Nullable Object getValue();
  }
}
