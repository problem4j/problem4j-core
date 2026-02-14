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

/**
 * Thrown when processing an annotated exception into a {@link Problem} fails.
 * {@code @RestControllerAdvice} or any other handlers can catch this and return a safe {@code 500}.
 * No other exception is supposed to be thrown from {@link ProblemMapper}.
 */
public class ProblemMappingException extends RuntimeException {

  /** Creates a new exception with no detail message and no cause. */
  public ProblemMappingException() {
    super();
  }

  /**
   * Creates a new exception with the specified detail message.
   *
   * @param message human-readable explanation of the failure
   */
  public ProblemMappingException(String message) {
    super(message);
  }

  /**
   * Creates a new exception wrapping the given cause.
   *
   * @param cause underlying cause (may be {@code null})
   */
  public ProblemMappingException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new exception with the specified detail message and cause.
   *
   * @param message human-readable explanation
   * @param cause underlying cause (may be {@code null})
   */
  public ProblemMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Advanced constructor allowing full control over suppression and stack trace writability.
   * Typically used only internally or in tests.
   *
   * @param message detail message
   * @param cause underlying cause (may be {@code null})
   * @param enableSuppression whether suppression is enabled or disabled
   * @param writableStackTrace whether the stack trace should be writable
   */
  protected ProblemMappingException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
