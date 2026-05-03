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
 * Runtime exception that wraps a {@link Problem} instance.
 *
 * <p>Provides a convenient way to throw exceptions associated with problem details according to RFC
 * 7807. The exception message is automatically generated from the problem's title, detail, and
 * status unless explicitly provided.
 *
 * @since 1.3.0
 */
public class ProblemException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /** The underlying {@link Problem} instance associated with this exception. */
  private final Problem problem;

  /**
   * Constructs a {@link ProblemException} with the given {@link Problem}.
   *
   * <p>The exception message is automatically generated from the problem's title, detail, and
   * status code.
   *
   * @param problem the problem instance to associate with this exception
   * @since 1.3.0
   */
  public ProblemException(Problem problem) {
    super(produceExceptionMessage(problem));
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with a custom message and the given {@link Problem}.
   *
   * @param message custom exception message (may be {@code null})
   * @param problem the problem instance to associate with this exception
   * @since 1.3.0
   */
  public ProblemException(@Nullable String message, Problem problem) {
    super(message != null && !message.isEmpty() ? message : produceExceptionMessage(problem));
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with the given {@link Problem} and a cause.
   *
   * <p>The exception message is automatically generated from the problem's title, detail, and
   * status code.
   *
   * @param problem the problem instance to associate with this exception
   * @param cause the root cause of this exception (a {@code null} value is permitted, and indicates
   *     that the cause is nonexistent or unknown)
   * @since 1.3.0
   */
  public ProblemException(Problem problem, @Nullable Throwable cause) {
    super(produceExceptionMessage(problem), cause);
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with a custom message, the given {@link Problem}, and a
   * cause.
   *
   * @param message custom exception message (may be {@code null})
   * @param problem the problem instance to associate with this exception
   * @param cause the root cause of this exception (a {@code null} value is permitted, and indicates
   *     that the cause is nonexistent or unknown)
   * @since 1.3.0
   */
  public ProblemException(@Nullable String message, Problem problem, @Nullable Throwable cause) {
    super(
        message != null && !message.isEmpty() ? message : produceExceptionMessage(problem), cause);
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with full control over exception properties.
   *
   * <p>This constructor allows specifying a custom message, associated {@link Problem}, a root
   * cause, and advanced options such as whether suppression is enabled and whether the stack trace
   * should be writable.
   *
   * @param message custom exception message (may be {@code null})
   * @param problem the problem instance to associate with this exception
   * @param cause the root cause of this exception (a {@code null} value is permitted, and indicates
   *     that the cause is nonexistent or unknown)
   * @param enableSuppression whether suppression is enabled
   * @param writableStackTrace whether the stack trace should be writable
   * @since 1.3.0
   */
  protected ProblemException(
      @Nullable String message,
      Problem problem,
      @Nullable Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(
        message != null && !message.isEmpty() ? message : produceExceptionMessage(problem),
        cause,
        enableSuppression,
        writableStackTrace);
    this.problem = problem;
  }

  /**
   * Produces a string message for the exception based on the problem's title, detail, and status.
   *
   * <p>Format:
   *
   * <ul>
   *   <li>Title
   *   <li>Title (code: STATUS)
   *   <li>Title: Detail
   *   <li>Title: Detail (code: STATUS)
   * </ul>
   *
   * If no information is available, returns {@code null}.
   *
   * @param problem the problem instance
   * @return a formatted exception message or {@code null} if empty
   */
  private static @Nullable String produceExceptionMessage(Problem problem) {
    StringBuilder builder = new StringBuilder();
    builder.append(problem.getTitle());

    if (problem.getDetail() != null) {
      if (builder.length() > 0) {
        builder.append(": ");
      }
      builder.append(problem.getDetail());
    }

    if (problem.getStatus() != 0) {
      if (builder.length() > 0) {
        builder.append(" ");
      }
      builder.append("(code: ").append(problem.getStatus()).append(")");
    }

    if (builder.length() == 0) {
      return null;
    }

    return builder.toString();
  }

  /**
   * Returns the underlying {@link Problem} associated with this exception.
   *
   * @return the {@link Problem} instance
   * @since 1.3.0
   */
  public Problem getProblem() {
    return problem;
  }
}
