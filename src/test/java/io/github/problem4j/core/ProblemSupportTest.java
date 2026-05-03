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

import java.net.URI;
import org.junit.jupiter.api.Test;

class ProblemSupportTest {

  @Test
  void givenProblemsWithIdenticalFields_whenEquals_thenTrue() {
    Problem p1 =
        Problem.builder()
            .type("urn:type")
            .title("Title")
            .status(400)
            .detail("Detail")
            .instance("urn:inst")
            .extension("k", "v")
            .build();
    Problem p2 =
        Problem.builder()
            .type("urn:type")
            .title("Title")
            .status(400)
            .detail("Detail")
            .instance("urn:inst")
            .extension("k", "v")
            .build();

    assertThat(ProblemSupport.equals(p1, p2)).isTrue();
  }

  @Test
  void givenProblemsWithDifferentType_whenEquals_thenFalse() {
    Problem p1 = Problem.builder().type("urn:a").title("T").status(400).build();
    Problem p2 = Problem.builder().type("urn:b").title("T").status(400).build();

    assertThat(ProblemSupport.equals(p1, p2)).isFalse();
  }

  @Test
  void givenProblemsWithDifferentTitle_whenEquals_thenFalse() {
    Problem p1 = Problem.builder().title("A").status(400).build();
    Problem p2 = Problem.builder().title("B").status(400).build();

    assertThat(ProblemSupport.equals(p1, p2)).isFalse();
  }

  @Test
  void givenProblemsWithDifferentStatus_whenEquals_thenFalse() {
    Problem p1 = Problem.builder().title("T").status(400).build();
    Problem p2 = Problem.builder().title("T").status(500).build();

    assertThat(ProblemSupport.equals(p1, p2)).isFalse();
  }

  @Test
  void givenProblemsWithDifferentDetail_whenEquals_thenFalse() {
    Problem p1 = Problem.builder().title("T").status(400).detail("D1").build();
    Problem p2 = Problem.builder().title("T").status(400).detail("D2").build();

    assertThat(ProblemSupport.equals(p1, p2)).isFalse();
  }

  @Test
  void givenProblemsWithDifferentInstance_whenEquals_thenFalse() {
    Problem p1 = Problem.builder().title("T").status(400).instance("urn:i1").build();
    Problem p2 = Problem.builder().title("T").status(400).instance("urn:i2").build();

    assertThat(ProblemSupport.equals(p1, p2)).isFalse();
  }

  @Test
  void givenProblemsWithDifferentExtensions_whenEquals_thenFalse() {
    Problem p1 = Problem.builder().title("T").status(400).extension("k", "v1").build();
    Problem p2 = Problem.builder().title("T").status(400).extension("k", "v2").build();

    assertThat(ProblemSupport.equals(p1, p2)).isFalse();
  }

  @Test
  void givenEqualProblems_whenHashCode_thenEqual() {
    Problem p1 = Problem.builder().title("T").status(400).detail("D").extension("k", "v").build();
    Problem p2 = Problem.builder().title("T").status(400).detail("D").extension("k", "v").build();

    assertThat(ProblemSupport.hashCode(p1)).isEqualTo(ProblemSupport.hashCode(p2));
  }

  @Test
  void givenMinimalProblem_whenToStringWithLabel_thenTypeOmittedAndOnlyRequiredFields() {
    Problem p = Problem.builder().title("T").status(200).build();

    assertThat(ProblemSupport.toString("Problem", p)).isEqualTo("Problem[title=T, status=200]");
  }

  @Test
  void givenProblemWithNonBlankType_whenToStringWithLabel_thenTypeIncluded() {
    Problem p = Problem.builder().type("urn:example").title("T").status(400).build();

    assertThat(ProblemSupport.toString("Problem", p)).startsWith("Problem[type=urn:example,");
  }

  @Test
  void givenProblemWithAllOptionalFields_whenToStringWithLabel_thenAllIncluded() {
    Problem p =
        Problem.builder()
            .type("urn:type")
            .title("T")
            .status(400)
            .detail("D")
            .instance("urn:inst")
            .build();

    String result = ProblemSupport.toString("Problem", p);
    assertThat(result).contains("type=urn:type");
    assertThat(result).contains("detail=D");
    assertThat(result).contains("instance=urn:inst");
  }

  @Test
  void givenProblemWithExtensions_whenToStringWithLabel_thenExtensionsSortedByKey() {
    Problem p =
        Problem.builder()
            .title("T")
            .status(400)
            .extension("z", "last")
            .extension("a", "first")
            .extension("m", "middle")
            .build();

    String result = ProblemSupport.toString("Problem", p);
    assertThat(result.indexOf("a=first")).isLessThan(result.indexOf("m=middle"));
    assertThat(result.indexOf("m=middle")).isLessThan(result.indexOf("z=last"));
  }

  @Test
  void givenProblemAndCustomLabel_whenToStringWithLabel_thenLabelUsedAsPrefix() {
    Problem p = Problem.builder().title("T").status(200).build();

    assertThat(ProblemSupport.toString("MyProblem", p)).isEqualTo("MyProblem[title=T, status=200]");
  }

  @Test
  void givenDefaultProblem_whenToString_thenSimpleClassNameUsedAsPrefix() {
    Problem p = Problem.builder().title("T").status(200).build();

    assertThat(ProblemSupport.toString(p)).startsWith("DefaultProblem[");
  }

  @Test
  void givenExtensionsWithSameNameAndValue_whenEquals_thenTrue() {
    Problem.Extension e1 = Problem.extension("k", "v");
    Problem.Extension e2 = Problem.extension("k", "v");

    assertThat(ProblemSupport.equals(e1, e2)).isTrue();
  }

  @Test
  void givenExtensionsWithDifferentName_whenEquals_thenFalse() {
    Problem.Extension e1 = Problem.extension("k1", "v");
    Problem.Extension e2 = Problem.extension("k2", "v");

    assertThat(ProblemSupport.equals(e1, e2)).isFalse();
  }

  @Test
  void givenExtensionsWithDifferentValue_whenEquals_thenFalse() {
    Problem.Extension e1 = Problem.extension("k", "v1");
    Problem.Extension e2 = Problem.extension("k", "v2");

    assertThat(ProblemSupport.equals(e1, e2)).isFalse();
  }

  @Test
  void givenExtensionsWithNullValues_whenEquals_thenTrue() {
    Problem.Extension e1 = Problem.extension("k", null);
    Problem.Extension e2 = Problem.extension("k", null);

    assertThat(ProblemSupport.equals(e1, e2)).isTrue();
  }

  @Test
  void givenEqualExtensions_whenHashCode_thenEqual() {
    Problem.Extension e1 = Problem.extension("k", "v");
    Problem.Extension e2 = Problem.extension("k", "v");

    assertThat(ProblemSupport.hashCode(e1)).isEqualTo(ProblemSupport.hashCode(e2));
  }

  @Test
  void givenExtension_whenToStringWithLabel_thenCorrectFormat() {
    assertThat(ProblemSupport.toString("Extension", Problem.extension("key", 42)))
        .isEqualTo("Extension[key=42]");
  }

  @Test
  void givenExtensionWithNullValue_whenToStringWithLabel_thenNullRendered() {
    assertThat(ProblemSupport.toString("Extension", Problem.extension("key", null)))
        .isEqualTo("Extension[key=null]");
  }

  @Test
  void givenExtensionAndCustomLabel_whenToStringWithLabel_thenLabelUsedAsPrefix() {
    assertThat(ProblemSupport.toString("MyExtension", Problem.extension("key", 42)))
        .isEqualTo("MyExtension[key=42]");
  }

  @Test
  void givenDefaultExtension_whenToString_thenSimpleClassNameUsedAsPrefix() {
    Problem.Extension e = Problem.extension("key", 42);

    assertThat(ProblemSupport.toString(e)).startsWith("DefaultExtension[");
  }

  @Test
  void givenContextsWithSameContent_whenEquals_thenTrue() {
    ProblemContext c1 = ProblemContext.create().put("k", "v");
    ProblemContext c2 = ProblemContext.create().put("k", "v");

    assertThat(ProblemSupport.equals(c1, c2)).isTrue();
  }

  @Test
  void givenContextsWithDifferentContent_whenEquals_thenFalse() {
    ProblemContext c1 = ProblemContext.create().put("k", "v1");
    ProblemContext c2 = ProblemContext.create().put("k", "v2");

    assertThat(ProblemSupport.equals(c1, c2)).isFalse();
  }

  @Test
  void givenEmptyContexts_whenEquals_thenTrue() {
    ProblemContext c1 = ProblemContext.create();
    ProblemContext c2 = ProblemContext.create();

    assertThat(ProblemSupport.equals(c1, c2)).isTrue();
  }

  @Test
  void givenEqualContexts_whenHashCode_thenEqual() {
    ProblemContext c1 = ProblemContext.create().put("k", "v");
    ProblemContext c2 = ProblemContext.create().put("k", "v");

    assertThat(ProblemSupport.hashCode(c1)).isEqualTo(ProblemSupport.hashCode(c2));
  }

  @Test
  void givenContextWithEntries_whenToStringWithLabel_thenSortedByKey() {
    ProblemContext context =
        ProblemContext.create().put("z", "last").put("a", "first").put("m", "middle");

    String result = ProblemSupport.toString("ProblemContext", context);
    assertThat(result).startsWith("ProblemContext[");
    assertThat(result.indexOf("a=first")).isLessThan(result.indexOf("m=middle"));
    assertThat(result.indexOf("m=middle")).isLessThan(result.indexOf("z=last"));
  }

  @Test
  void givenContextAndCustomLabel_whenToStringWithLabel_thenLabelUsedAsPrefix() {
    assertThat(ProblemSupport.toString("MyContext", ProblemContext.create().put("key", "val")))
        .isEqualTo("MyContext[key=val]");
  }

  @Test
  void givenEmptyContextAndCustomLabel_whenToStringWithLabel_thenLabelUsedAsPrefix() {
    assertThat(ProblemSupport.toString("MyContext", ProblemContext.create()))
        .isEqualTo("MyContext[EMPTY]");
  }

  @Test
  void givenDefaultProblemContext_whenToString_thenSimpleClassNameUsedAsPrefix() {
    ProblemContext c = ProblemContext.create();

    assertThat(ProblemSupport.toString(c)).startsWith("DefaultProblemContext[");
  }

  @Test
  void givenBlankType_whenIsTypeBlank_thenTrue() {
    assertThat(ProblemSupport.isTypeBlank(Problem.BLANK_TYPE)).isTrue();
  }

  @Test
  void givenEmptyUriType_whenIsTypeBlank_thenTrue() {
    assertThat(ProblemSupport.isTypeBlank(URI.create(""))).isTrue();
  }

  @Test
  void givenNonBlankType_whenIsTypeBlank_thenFalse() {
    assertThat(ProblemSupport.isTypeBlank(URI.create("urn:example"))).isFalse();
  }

  @Test
  void givenNonBlankType_whenIsTypeNonBlank_thenTrue() {
    assertThat(ProblemSupport.isTypeNonBlank(URI.create("urn:example"))).isTrue();
  }

  @Test
  void givenBlankType_whenIsTypeNonBlank_thenFalse() {
    assertThat(ProblemSupport.isTypeNonBlank(Problem.BLANK_TYPE)).isFalse();
  }

  @Test
  void givenEmptyUriType_whenIsTypeNonBlank_thenFalse() {
    assertThat(ProblemSupport.isTypeNonBlank(URI.create(""))).isFalse();
  }
}
