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

import java.net.URI;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * Builder interface for constructing {@link Problem} instances.
 *
 * <p>Provides a fluent API to set standard fields and custom extensions before creating an
 * immutable {@link Problem}.
 *
 * @since 1.3.0
 */
public interface ProblemBuilder {

  /**
   * Sets the problem type URI.
   *
   * @param type the URI identifying the problem type (may be {@code null})
   * @return this builder instance for chaining
   * @since 1.3.0
   */
  ProblemBuilder type(@Nullable URI type);

  /**
   * Sets the problem type from a string representation of a URI.
   *
   * @param type string URI identifying the problem type (may be {@code null})
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if the string is not a valid URI
   * @since 1.3.0
   */
  default ProblemBuilder type(@Nullable String type) {
    return type != null ? type(URI.create(type)) : type((URI) null);
  }

  /**
   * Sets the short, human-readable title for the problem.
   *
   * @param title the problem title (may be {@code null})
   * @return this builder instance for chaining
   * @since 1.3.0
   */
  ProblemBuilder title(@Nullable String title);

  /**
   * Sets the HTTP status code for this problem.
   *
   * @param status HTTP status code
   * @return this builder instance for chaining
   * @since 1.3.0
   */
  ProblemBuilder status(int status);

  /**
   * Sets a detailed, human-readable description of this problem instance.
   *
   * @param detail the detail message (may be {@code null})
   * @return this builder instance for chaining
   * @since 1.3.0
   */
  ProblemBuilder detail(@Nullable String detail);

  /**
   * Sets the URI identifying this specific occurrence of the problem.
   *
   * @param instance URI identifying the problem occurrence (may be {@code null})
   * @return this builder instance for chaining
   * @since 1.3.0
   */
  ProblemBuilder instance(@Nullable URI instance);

  /**
   * Sets the instance URI from a string representation.
   *
   * @param instance string URI identifying the problem occurrence (may be {@code null})
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if the string is not a valid URI
   * @since 1.3.0
   */
  default ProblemBuilder instance(@Nullable String instance) {
    return instance != null ? instance(URI.create(instance)) : instance((URI) null);
  }

  /**
   * Adds or removes a single custom extension. If {@code value} is {@code null} and an extension
   * with the given {@code name} already exists, it will be removed.
   *
   * @param name the extension key
   * @param value the extension value, or {@code null} to remove
   * @return this builder instance for chaining
   * @since 1.3.0
   */
  ProblemBuilder extension(String name, @Nullable Object value);

  /**
   * Adds multiple custom extensions from a map. If the value of any provided extension is {@code
   * null} and an extension with the same key already exists, it will be removed.
   *
   * @param extensions map of extension keys and values
   * @return this builder instance for chaining
   * @since 1.3.3
   */
  default ProblemBuilder extensions(Map<String, ? extends @Nullable Object> extensions) {
    extensions.forEach(this::extension);
    return this;
  }

  /**
   * Adds a single custom extension from {@link Problem.Extension}. If the value of the provided
   * extension is {@code null} and an extension with the same key already exists, it will be
   * removed.
   *
   * @param extension extension to add
   * @return this builder instance for chaining
   * @since 1.3.3
   */
  default ProblemBuilder extension(Problem.Extension extension) {
    return extension(extension.getName(), extension.getValue());
  }

  /**
   * Adds multiple custom extensions from varargs of {@link Problem.Extension}. If the value of any
   * provided extension is {@code null} and an extension with the same key already exists, it will
   * be removed.
   *
   * @param extensions vararg of extensions to add
   * @return this builder instance for chaining
   * @since 1.3.3
   */
  default ProblemBuilder extensions(Problem.Extension... extensions) {
    for (Problem.Extension e : extensions) {
      extension(e);
    }
    return this;
  }

  /**
   * Adds multiple custom extensions from a collection of {@link Problem.Extension}. If the value of
   * any provided extension is {@code null} and an extension with the same key already exists, it
   * will be removed.
   *
   * @param extensions collection of extensions to add
   * @return this builder instance for chaining
   * @since 2.0.0
   */
  default ProblemBuilder extensions(Iterable<? extends Problem.Extension> extensions) {
    extensions.forEach(this::extension);
    return this;
  }

  /**
   * Builds an immutable {@link Problem} instance with the configured properties and extensions.
   *
   * <p>Default value evaluation (all defaults are applied at build time):
   *
   * <ul>
   *   <li>If no type was provided, the resulting {@link Problem} will use {@link
   *       Problem#BLANK_TYPE}.
   *   <li>If no title was provided, but the numeric status corresponds to a known HTTP status, the
   *       builder will resolve it using {@link StatusTitleResolver}. It can be customized via SPI
   *       implementation.
   *   <li>The numeric status defaults to <code>0</code> when not set;
   *   <li>Any extensions configured on the builder will be present on the created {@link Problem}.
   * </ul>
   *
   * @return a new {@link Problem} instance
   * @since 1.3.0
   */
  Problem build();
}
