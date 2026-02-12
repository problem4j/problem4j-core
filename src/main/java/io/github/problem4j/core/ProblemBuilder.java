/*
 * Copyright (c) 2025-2026 Damian Malczewski
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
import java.util.Collection;
import java.util.Map;

/**
 * Builder interface for constructing {@link Problem} instances.
 *
 * <p>Provides a fluent API to set standard fields and custom extensions before creating an
 * immutable {@link Problem}.
 */
public interface ProblemBuilder {

  /**
   * Sets the problem type URI.
   *
   * @param type the URI identifying the problem type
   * @return this builder instance for chaining
   */
  ProblemBuilder type(URI type);

  /**
   * Sets the problem type from a string representation of a URI.
   *
   * @param type string URI identifying the problem type
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if the string is not a valid URI
   */
  ProblemBuilder type(String type);

  /**
   * Sets the short, human-readable title for the problem.
   *
   * @param title the problem title
   * @return this builder instance for chaining
   */
  ProblemBuilder title(String title);

  /**
   * Sets the HTTP status code for this problem.
   *
   * @param status HTTP status code
   * @return this builder instance for chaining
   */
  ProblemBuilder status(int status);

  /**
   * Sets the HTTP status code using a {@link ProblemStatus} enum.
   *
   * @param status the {@link ProblemStatus} representing the HTTP status
   * @return this builder instance for chaining
   */
  ProblemBuilder status(ProblemStatus status);

  /**
   * Sets a detailed, human-readable description of this problem instance.
   *
   * @param detail the detail message
   * @return this builder instance for chaining
   */
  ProblemBuilder detail(String detail);

  /**
   * Sets the URI identifying this specific occurrence of the problem.
   *
   * @param instance the instance URI
   * @return this builder instance for chaining
   */
  ProblemBuilder instance(URI instance);

  /**
   * Sets the instance URI from a string representation.
   *
   * @param instance string URI identifying the problem occurrence
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if the string is not a valid URI
   */
  ProblemBuilder instance(String instance);

  /**
   * Adds a single custom extension.
   *
   * @param name the extension key
   * @param value the extension value
   * @return this builder instance for chaining
   */
  ProblemBuilder extension(String name, Object value);

  /**
   * Adds multiple custom extensions from a map.
   *
   * @param extensions map of extension keys and values
   * @return this builder instance for chaining
   */
  ProblemBuilder extensions(Map<String, Object> extensions);

  /**
   * Adds single custom extension from {@link Problem.Extension}.
   *
   * @param extension array of extensions
   * @return this builder instance for chaining
   */
  default ProblemBuilder extension(Problem.Extension extension) {
    return extensions(extension);
  }

  /**
   * Adds multiple custom extensions from varargs of {@link Problem.Extension}.
   *
   * @param extensions array of extensions
   * @return this builder instance for chaining
   */
  ProblemBuilder extensions(Problem.Extension... extensions);

  /**
   * Adds multiple custom extensions from a collection of {@link Problem.Extension}.
   *
   * @param extensions collection of extensions
   * @return this builder instance for chaining
   */
  ProblemBuilder extensions(Collection<? extends Problem.Extension> extensions);

  /**
   * Builds an immutable {@link Problem} instance with the configured properties and extensions.
   *
   * <p>Default value evaluation (all defaults are applied at build time):
   *
   * <ul>
   *   <li>If no type was provided, the resulting {@link Problem} will use {@link
   *       Problem#BLANK_TYPE}.
   *   <li>If no title was provided, but the numeric status corresponds to a known {@link
   *       ProblemStatus}, the builder will use the matching {@link ProblemStatus#getTitle()} as the
   *       problem title.
   *   <li>The numeric status defaults to <code>0</code> when not set; a title will not be derived
   *       from status when it is <code>0</code> or when it does not map to any known {@link
   *       ProblemStatus}.
   *   <li>Any extensions configured on the builder will be present on the created {@link Problem}.
   * </ul>
   *
   * @return a new {@link Problem} instance
   */
  Problem build();

  /**
   * Adds multiple custom extensions from a map.
   *
   * <p><b>Deprecated</b> due to confusing name as singular "extension" suggests adding a single
   * extension, while the method actually adds multiple extensions from the provided map.
   *
   * @param extensions map of extension keys and values
   * @return this builder instance for chaining
   * @deprecated use {@link #extensions(Map)} instead
   */
  @Deprecated
  default ProblemBuilder extension(Map<String, Object> extensions) {
    return extensions(extensions);
  }

  /**
   * Adds multiple custom extensions from varargs of {@link Problem.Extension}.
   *
   * <p><b>Deprecated</b> due to confusing name as singular "extension" suggests adding a single
   * extension, while the method actually adds multiple extensions from the provided vararg.
   *
   * @param extensions array of extensions
   * @return this builder instance for chaining
   * @deprecated use {@link #extensions(Problem.Extension...)} instead
   */
  @Deprecated
  default ProblemBuilder extension(Problem.Extension... extensions) {
    return extensions(extensions);
  }

  /**
   * Adds multiple custom extensions from a collection of {@link Problem.Extension}.
   *
   * <p><b>Deprecated</b> due to confusing name as singular "extension" suggests adding a single
   * extension, while the method actually adds multiple extensions from the provided collection.
   *
   * @param extensions collection of extensions
   * @return this builder instance for chaining
   * @deprecated use {@link #extensions(Collection)} instead
   */
  @Deprecated
  default ProblemBuilder extension(Collection<? extends Problem.Extension> extensions) {
    return extensions(extensions);
  }
}
