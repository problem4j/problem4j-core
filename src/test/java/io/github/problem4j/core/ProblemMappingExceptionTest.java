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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools.
 */
class ProblemMappingExceptionTest {

  @Test
  void givenNoArgs_whenCreatingException_thenMessageIsNull() {
    ProblemMappingException ex = new ProblemMappingException();

    assertThat(ex.getMessage()).isNull();
    assertThat(ex.getCause()).isNull();
  }

  @Test
  void givenMessage_whenCreatingException_thenMessageIsSet() {
    ProblemMappingException ex = new ProblemMappingException("mapping failed");

    assertThat(ex.getMessage()).isEqualTo("mapping failed");
    assertThat(ex.getCause()).isNull();
  }

  @Test
  void givenCause_whenCreatingException_thenCauseIsSet() {
    RuntimeException cause = new RuntimeException("root");
    ProblemMappingException ex = new ProblemMappingException(cause);

    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  void givenMessageAndCause_whenCreatingException_thenBothAreSet() {
    RuntimeException cause = new RuntimeException("root");
    ProblemMappingException ex = new ProblemMappingException("mapping failed", cause);

    assertThat(ex.getMessage()).isEqualTo("mapping failed");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  void givenFullConstructor_whenCreatingException_thenAllFieldsAreSet() {
    RuntimeException cause = new RuntimeException("root");
    ProblemMappingException ex =
        new TestProblemMappingException("mapping failed", cause, true, false);

    assertThat(ex.getMessage()).isEqualTo("mapping failed");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  private static class TestProblemMappingException extends ProblemMappingException {
    TestProblemMappingException(
        String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
  }
}
