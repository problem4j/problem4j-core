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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ProblemTest {

  @Test
  void givenIntStatus_shouldSetStatus() {
    Problem problem = Problem.of(ProblemStatus.MULTI_STATUS.getStatus());

    assertThat(problem.getTitle()).isEqualTo(ProblemStatus.MULTI_STATUS.getTitle());
    assertThat(problem.getStatus()).isEqualTo(ProblemStatus.MULTI_STATUS.getStatus());
  }

  @Test
  void givenTitleAndStatus_shouldSetFields() {
    Problem problem = Problem.of("Test", 400);

    assertThat(problem.getTitle()).isEqualTo("Test");
    assertThat(problem.getStatus()).isEqualTo(400);
  }

  @Test
  void givenTitleStatusAndDetail_shouldSetFields() {
    Problem problem = Problem.of("Test", 400, "Detail");

    assertThat(problem.getTitle()).isEqualTo("Test");
    assertThat(problem.getStatus()).isEqualTo(400);
    assertThat(problem.getDetail()).isEqualTo("Detail");
  }

  @Test
  void givenTypeTitleAndStatus_shouldSetFields() {
    URI type = URI.create("urn:test:type");

    Problem problem = Problem.of(type, "Test", 400);

    assertThat(problem.getType()).isEqualTo(type);
    assertThat(problem.getTitle()).isEqualTo("Test");
    assertThat(problem.getStatus()).isEqualTo(400);
  }

  @Test
  void givenTypeTitleStatusAndDetail_shouldSetFields() {
    URI type = URI.create("urn:test:type");

    Problem problem = Problem.of(type, "Test", 400, "Detail");

    assertThat(problem.getType()).isEqualTo(type);
    assertThat(problem.getDetail()).isEqualTo("Detail");
  }

  @Test
  void givenProblem_whenToBuilder_shouldBeAbleToRecreateOriginal() {
    Problem problem =
        Problem.builder()
            .type(URI.create("http://example.org/problem1"))
            .title("Problem1")
            .status(400)
            .detail("this is problem1")
            .instance(URI.create("http://example.org/instance1"))
            .extension("extCode1", "extValue1")
            .extension(Problem.extension("extCode2", "extValue2"))
            .extensions(
                Arrays.asList(
                    Problem.extension("extCode3", "extValue3"),
                    Problem.extension("extCode4", "extValue4")))
            .extensions(
                Stream.of(
                        Problem.extension("extCode5", "extValue5"),
                        Problem.extension("extCode6", "extValue6"),
                        Problem.extension("extCode7", "extValue7"))
                    .collect(Collectors.toMap(Problem.Extension::getKey, Function.identity())))
            .build();

    ProblemBuilder builder = problem.toBuilder();
    Problem copy = builder.build();

    assertThat(problem).isNotSameAs(copy);
    assertThat(problem).isEqualTo(copy);
  }

  @Test
  void givenProblemExtensionWithNullKey_shouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Problem.extension(null, "v"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("key cannot be null");
  }

  @Test
  void givenProblemExtensionWithKey_shouldCreateExtension() {
    Problem.Extension ext = Problem.extension("code", 123);

    assertThat(ext.getKey()).isEqualTo("code");
    assertThat(ext.getValue()).isEqualTo(123);
  }
}
