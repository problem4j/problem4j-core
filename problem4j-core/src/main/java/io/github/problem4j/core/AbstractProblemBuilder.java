/*
 * Copyright (c) 2025 Damian Malczewski
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Builder interface for constructing {@link Problem} instances.
 *
 * <p>Provides a fluent API to set standard fields and custom extensions before creating an
 * immutable {@link Problem}.
 */
public abstract class AbstractProblemBuilder implements ProblemBuilder, Serializable {

  private static final long serialVersionUID = 1L;

  private URI type;
  private String title;
  private int status = 0;
  private String detail;
  private URI instance;
  private final Map<String, Object> extensions = new HashMap<>();

  /**
   * Creates a new, empty {@code AbstractProblemBuilder}.
   *
   * <p>Use this builder to incrementally construct an {@link Problem} instance.
   */
  public AbstractProblemBuilder() {}

  /**
   * Creates a new {@code AbstractProblemBuilder} initialized with values from an existing {@code
   * Problem}.
   *
   * <p>This copies all properties from the given {@code problem} so that modifications to the
   * builder do not affect the original {@code Problem} instance.
   *
   * @param problem the {@code Problem} whose values are copied into the builder; must not be {@code
   *     null}
   */
  public AbstractProblemBuilder(Problem problem) {
    this.type = problem.getType();
    this.title = problem.getTitle();
    this.status = problem.getStatus();
    this.detail = problem.getDetail();
    this.instance = problem.getInstance();
    this.extensions.putAll(problem.getExtensionMembers());
  }

  /**
   * Sets the problem type URI.
   *
   * @param type the URI identifying the problem type
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder type(URI type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the problem type from a string representation of a URI.
   *
   * @param type string URI identifying the problem type
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if the string is not a valid URI
   */
  @Override
  public ProblemBuilder type(String type) {
    return type != null ? type(URI.create(type)) : type((URI) null);
  }

  /**
   * Sets the short, human-readable title for the problem.
   *
   * @param title the problem title
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Sets the HTTP status code for this problem.
   *
   * @param status HTTP status code
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder status(int status) {
    this.status = status;
    return this;
  }

  /**
   * Sets the HTTP status code using a {@link ProblemStatus} enum.
   *
   * @param status the {@link ProblemStatus} representing the HTTP status
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder status(ProblemStatus status) {
    return status != null ? status(status.getStatus()) : status(0);
  }

  /**
   * Sets a detailed, human-readable description of this problem instance.
   *
   * @param detail the detail message
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder detail(String detail) {
    this.detail = detail;
    return this;
  }

  /**
   * Sets the URI identifying this specific occurrence of the problem.
   *
   * @param instance the instance URI
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder instance(URI instance) {
    this.instance = instance;
    return this;
  }

  /**
   * Sets the instance URI from a string representation.
   *
   * @param instance string URI identifying the problem occurrence
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if the string is not a valid URI
   */
  @Override
  public ProblemBuilder instance(String instance) {
    return instance != null ? instance(URI.create(instance)) : instance((URI) null);
  }

  /**
   * Adds a single custom extension.
   *
   * @param name the extension key
   * @param value the extension value
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder extension(String name, Object value) {
    if (name != null && value != null) {
      extensions.put(name, value);
    }
    return this;
  }

  /**
   * Adds multiple custom extensions from a map.
   *
   * @param extensions map of extension keys and values
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder extension(Map<String, Object> extensions) {
    if (extensions != null && !extensions.isEmpty()) {
      extensions.forEach(
          (key, value) -> {
            if (key != null && value != null) {
              this.extensions.put(key, value);
            }
          });
    }
    return this;
  }

  /**
   * Adds multiple custom extensions from varargs of {@link Problem.Extension}.
   *
   * @param extensions array of extensions
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder extension(Problem.Extension... extensions) {
    if (extensions != null && extensions.length > 0) {
      Stream.of(extensions)
          .filter(AbstractProblemBuilder::isExtensionValid)
          .forEach(e -> this.extensions.put(e.getKey(), e.getValue()));
    }
    return this;
  }

  /**
   * Adds multiple custom extensions from a collection of {@link Problem.Extension}.
   *
   * @param extensions collection of extensions
   * @return this builder instance for chaining
   */
  @Override
  public ProblemBuilder extension(Collection<? extends Problem.Extension> extensions) {
    if (extensions != null && !extensions.isEmpty()) {
      extensions.stream()
          .filter(AbstractProblemBuilder::isExtensionValid)
          .forEach(e -> this.extensions.put(e.getKey(), e.getValue()));
    }
    return this;
  }

  /**
   * Builds an immutable {@link Problem} instance with the configured properties and extensions.
   *
   * <p>Default value evaluation (all defaults are applied at build time):
   *
   * <ul>
   *   <li>If no type was provided, the resulting {@code Problem} will use {@code
   *       Problem#BLANK_TYPE}.
   *   <li>If no title was provided, but the numeric status corresponds to a known {@code
   *       ProblemStatus}, the builder will use the matching {@code ProblemStatus#getTitle()} as the
   *       problem title.
   *   <li>The numeric status defaults to <code>0</code> when not set; a title will not be derived
   *       from status when it is <code>0</code> or when it does not map to any known {@code
   *       ProblemStatus}.
   *   <li>Any extensions configured on the builder will be present on the created {@code Problem}.
   * </ul>
   *
   * @return a new {@link Problem} instance
   */
  @Override
  public Problem build() {
    URI type = this.type;
    if (type == null) {
      type = Problem.BLANK_TYPE;
    }
    String title = this.title;
    if (title == null) {
      Optional<ProblemStatus> status = ProblemStatus.findValue(this.status);
      if (status.isPresent()) {
        title = status.get().getTitle();
      }
    }
    return new ProblemImpl(type, title, status, detail, instance, extensions);
  }

  private static boolean isExtensionValid(Problem.Extension extension) {
    return extension != null && extension.getKey() != null && extension.getValue() != null;
  }
}
