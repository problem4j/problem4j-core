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

/**
 * Runtime exception that wraps a {@link Problem} instance.
 *
 * <p>Provides a convenient way to throw exceptions associated with problem details according to RFC
 * 7807. The exception message is automatically generated from the problem's title, detail, and
 * status unless explicitly provided.
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
   */
  public ProblemException(Problem problem) {
    super(produceExceptionMessage(problem));
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with a custom message and the given {@link Problem}.
   *
   * @param message custom exception message
   * @param problem the problem instance to associate with this exception
   */
  public ProblemException(String message, Problem problem) {
    super(message);
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
   */
  public ProblemException(Problem problem, Throwable cause) {
    super(produceExceptionMessage(problem), cause);
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with a custom message, the given {@link Problem}, and a
   * cause.
   *
   * @param message custom exception message
   * @param problem the problem instance to associate with this exception
   * @param cause the root cause of this exception (a {@code null} value is permitted, and indicates
   *     that the cause is nonexistent or unknown)
   */
  public ProblemException(String message, Problem problem, Throwable cause) {
    super(message, cause);
    this.problem = problem;
  }

  /**
   * Constructs a {@link ProblemException} with full control over exception properties.
   *
   * <p>This constructor allows specifying a custom message, associated {@link Problem}, a root
   * cause, and advanced options such as whether suppression is enabled and whether the stack trace
   * should be writable.
   *
   * @param message custom exception message
   * @param problem the problem instance to associate with this exception
   * @param cause the root cause of this exception (a {@code null} value is permitted, and indicates
   *     that the cause is nonexistent or unknown)
   * @param enableSuppression whether suppression is enabled
   * @param writableStackTrace whether the stack trace should be writable
   */
  protected ProblemException(
      String message,
      Problem problem,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.problem = problem;
  }

  /**
   * Produces a string message for the exception based on the problem's title, detail, and status.
   *
   * <p>Format:
   *
   * <ul>
   *   <li>Title
   *   <li>Title: Detail
   *   <li>Title: Detail (code: STATUS)
   * </ul>
   *
   * If no information is available, returns {@code null}.
   *
   * @param problem the problem instance
   * @return a formatted exception message or {@code null} if empty
   */
  private static String produceExceptionMessage(Problem problem) {
    StringBuilder builder = new StringBuilder();

    if (problem.getTitle() != null) {
      builder.append(problem.getTitle());
    }

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
   */
  public Problem getProblem() {
    return problem;
  }
}
