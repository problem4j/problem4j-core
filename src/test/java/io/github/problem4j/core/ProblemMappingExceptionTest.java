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
