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
 * Thrown when processing an annotated exception into a {@link Problem} fails.
 * {@code @RestControllerAdvice} or any other handlers can catch this and return a safe {@code 500}.
 * No other exception is supposed to be thrown from {@link ProblemMapper}.
 *
 * @since 1.3.0
 */
public class ProblemMappingException extends RuntimeException {

  private static final long serialVersionUID = 2L;

  /**
   * Creates a new exception with no detail message and no cause.
   *
   * @since 1.3.0
   */
  public ProblemMappingException() {
    super();
  }

  /**
   * Creates a new exception with the specified detail message.
   *
   * @param message human-readable explanation of the failure (may be {@code null})
   * @since 1.3.0
   */
  public ProblemMappingException(@Nullable String message) {
    super(message);
  }

  /**
   * Creates a new exception wrapping the given cause.
   *
   * @param cause underlying cause (may be {@code null})
   * @since 1.3.0
   */
  public ProblemMappingException(@Nullable Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new exception with the specified detail message and cause.
   *
   * @param message human-readable explanation of the failure (may be {@code null})
   * @param cause underlying cause (may be {@code null})
   * @since 1.3.0
   */
  public ProblemMappingException(@Nullable String message, @Nullable Throwable cause) {
    super(message, cause);
  }

  /**
   * Advanced constructor allowing full control over suppression and stack trace writability.
   * Typically used only internally or in tests.
   *
   * @param message detail message (may be {@code null})
   * @param cause underlying cause (may be {@code null})
   * @param enableSuppression whether suppression is enabled or disabled
   * @param writableStackTrace whether the stack trace should be writable
   * @since 1.3.0
   */
  protected ProblemMappingException(
      @Nullable String message,
      @Nullable Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
