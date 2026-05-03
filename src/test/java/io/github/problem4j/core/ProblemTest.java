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

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ProblemTest {

  @Test
  void givenIntStatus_shouldSetStatus() {
    Problem problem = Problem.of(207);

    assertThat(problem.getTitle()).isEqualTo("Multi-Status");
    assertThat(problem.getStatus()).isEqualTo(207);
  }

  @Test
  void givenStatusAndDetail_shouldSetFields() {
    Problem problem = Problem.of(400, "bad input");

    assertThat(problem.getStatus()).isEqualTo(400);
    assertThat(problem.getDetail()).isEqualTo("bad input");
    assertThat(problem.getTitle()).isEqualTo("Bad Request");
  }

  @Test
  void givenTypeWithEmptyUriString_whenIsTypeNonBlank_thenReturnsFalse() {
    Problem problem = Problem.builder().type(URI.create("")).status(200).build();

    assertThat(problem.isTypeNonBlank()).isFalse();
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
                    .collect(toMap(Problem.Extension::getName, identity())))
            .build();

    ProblemBuilder builder = problem.toBuilder();
    Problem copy = builder.build();

    assertThat(problem).isNotSameAs(copy);
    assertThat(problem).isEqualTo(copy);
  }

  @Test
  void givenProblemExtensionWithKey_shouldCreateExtension() {
    Problem.Extension ext = Problem.extension("code", 123);

    assertThat(ext.getName()).isEqualTo("code");
    assertThat(ext.getValue()).isEqualTo(123);
  }
}
