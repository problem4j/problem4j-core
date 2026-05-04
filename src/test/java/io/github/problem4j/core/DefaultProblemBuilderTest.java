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

import static io.github.problem4j.core.Problem.extension;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools.
 */
class DefaultProblemBuilderTest {

  private DefaultProblemBuilder newInstance() {
    return new DefaultProblemBuilder();
  }

  @Test
  void givenNullURIType_shouldNotSetIt() {
    Problem problem = newInstance().type((URI) null).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenNullStringType_shouldNotSetIt() {
    Problem problem = newInstance().type((String) null).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenBlankURIType_shouldNotSetIt() {
    Problem problem = newInstance().type(Problem.BLANK_TYPE).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenBlankStringType_shouldNotSetIt() {
    Problem problem = newInstance().type(Problem.BLANK_TYPE.toString()).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenExplicitTitle_thenStatusProblemStatus_shouldNotOverrideTitle() {
    Problem problem = newInstance().title("Custom Title").status(400).build();

    assertThat(problem.getStatus()).isEqualTo(400);
    assertThat(problem.getTitle()).isEqualTo("Custom Title");
  }

  @Test
  void givenStatusProblemStatus_thenExplicitTitle_shouldOverrideDerivedTitle() {
    Problem problem = newInstance().status(404).title("My Title").build();

    assertThat(problem.getStatus()).isEqualTo(404);
    assertThat(problem.getTitle()).isEqualTo("My Title");
  }

  @Test
  void givenInvalidTypeString_shouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> newInstance().type("ht tp://not a uri"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenInvalidInstanceString_shouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> newInstance().instance("::://invalid"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenNullURIInstance_shouldNotSetIt() {
    Problem problem = newInstance().instance((URI) null).build();

    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenNullStringInstance_shouldNotSetIt() {
    Problem problem = newInstance().instance((String) null).build();

    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenNullValueExtension_shouldNotIncludeIt() {
    Problem problem = newInstance().extension("key", null).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions().containsKey("key")).isFalse();
    assertThat(problem.getExtensions().get("key")).isNull();
    assertThat(problem.getExtensions()).isEqualTo(Map.of());
  }

  @Test
  void givenNullValueExtensionViaVarargs_shouldNotIncludeIt() {
    Problem problem =
        newInstance().extensions(extension("key1", null), extension("key2", null)).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions()).isEqualTo(Map.of());

    assertThat(problem.getExtensions().containsKey("key1")).isFalse();
    assertThat(problem.getExtensions().get("key1")).isNull();

    assertThat(problem.getExtensions().containsKey("key2")).isFalse();
    assertThat(problem.getExtensions().get("key2")).isNull();
  }

  @Test
  void givenNullValueExtensionViaObject_shouldNotIncludeIt() {
    Problem problem =
        newInstance()
            .extensions(Arrays.asList(extension("key1", null), extension("key2", null)))
            .build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions()).isEqualTo(Map.of());

    assertThat(problem.getExtensions().containsKey("key1")).isFalse();
    assertThat(problem.getExtensions().get("key1")).isNull();

    assertThat(problem.getExtensions().containsKey("key2")).isFalse();
    assertThat(problem.getExtensions().get("key2")).isNull();
  }

  @Test
  void givenMapExtensionWithNullValue_shouldIgnoreNullValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("ignored", null);
    map.put("a", "b");

    Problem problem = newInstance().extensions(map).build();

    assertThat(problem.getExtensions().keySet()).containsExactly("a");
    assertThat(problem.getExtensions().containsKey("a")).isTrue();
    assertThat(problem.getExtensions().get("a")).isEqualTo("b");
    assertThat(problem.getExtensions()).isEqualTo(Map.of("a", "b"));
  }

  @Test
  void givenNumericStatus_shouldDeriveTitleWhenKnown() {
    Problem problem = newInstance().status(207).build();

    assertThat(problem.getStatus()).isEqualTo(207);
    assertThat(problem.getTitle()).isEqualTo("Multi-Status");
  }

  @Test
  void givenUnknownNumericStatus_shouldNotDeriveTitle() {
    Problem problem = newInstance().status(999).build();

    assertThat(problem.getStatus()).isEqualTo(999);
    assertThat(problem.getTitle()).isEqualTo("Unknown Error");
  }

  @Test
  void givenTypeAndInstance_stringOverloads_shouldAcceptValidUris() {
    String t = "http://example.org/type";
    String i = "http://example.org/instance";

    Problem problem = newInstance().type(t).instance(i).build();

    assertThat(problem.getType()).isEqualTo(URI.create(t));
    assertThat(problem.getInstance()).isEqualTo(URI.create(i));
  }

  @Test
  void givenEmptyMapExtension_shouldBeIgnored() {
    Map<String, Object> m = new HashMap<>();
    Problem problem = newInstance().extensions(m).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions()).isEqualTo(emptyMap());
  }

  @Test
  void givenAssigningTheSameExtensionLater_shouldOverwriteEarlierValues() {
    Problem problem = newInstance().extension("k", "v1").extension(extension("k", "v2")).build();

    assertThat(problem.getExtensions().get("k")).isEqualTo("v2");
    assertThat(problem.getExtensions().keySet()).containsExactly("k");
    assertThat(problem.getExtensions()).isEqualTo(Map.of("k", "v2"));
  }

  @Test
  void givenExtensionSetThenUnsetWithNull_shouldRemoveExtension() {
    Problem problem = newInstance().extension("name", "Mark").extension("name", null).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions().containsKey("name")).isFalse();
    assertThat(problem.getExtensions().get("name")).isNull();
    assertThat(problem.getExtensions()).isEqualTo(Map.of());
  }

  @Test
  void givenExtensionSetThenUnsetViaMap_shouldRemoveExtension() {
    Map<String, Object> removals = new HashMap<>();
    removals.put("name", null);

    Problem problem = newInstance().extension("name", "Mark").extensions(removals).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions().containsKey("name")).isFalse();
  }

  @Test
  void givenExtensionSetThenUnsetViaVarargs_shouldRemoveExtension() {
    Problem problem =
        newInstance().extension("name", "Mark").extensions(Problem.extension("name", null)).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions().containsKey("name")).isFalse();
  }

  @Test
  void givenExtensionSetThenUnsetViaCollection_shouldRemoveExtension() {
    Problem problem =
        newInstance()
            .extension("name", "Mark")
            .extensions(Collections.singletonList(Problem.extension("name", null)))
            .build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensions().containsKey("name")).isFalse();
  }

  @Test
  void givenAllFieldsPopulated_whenToString_thenContainsAllFields() {
    URI type = URI.create("https://example.com/problem");
    String title = "Test Problem";
    int status = 400;
    String detail = "Something went wrong";
    URI instance = URI.create("https://example.com/instance");
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("stringExt", "value");
    extensions.put("numberExt", 42);
    extensions.put("booleanExt", true);
    extensions.put("objectExt", new DummyObject("boo\nfoo"));

    ProblemBuilder builder =
        newInstance().type(type).title(title).status(status).detail(detail).instance(instance);
    extensions.forEach(builder::extension);

    String result = builder.toString();

    assertThat(result).contains("type=" + type);
    assertThat(result).contains("title=" + title);
    assertThat(result).contains("status=" + status);
    assertThat(result).contains("detail=" + detail);
    assertThat(result).contains("instance=" + instance);
    assertThat(result).contains("stringExt=value");
    assertThat(result).contains("numberExt=42");
    assertThat(result).contains("booleanExt=true");
    assertThat(result).contains("objectExt=DummyObject{value=boo\nfoo}");
  }

  @Test
  void givenNoValuesAssigned_whenToString_thenToStringReturnsStatus() {
    ProblemBuilder builder = newInstance();
    String result = builder.toString();
    assertThat(result).isEqualTo("ProblemBuilder[status=0]");
  }

  @Test
  void givenNullExtensionsAndNullableFields_whenToString_thenOmitsNulls() {
    ProblemBuilder builder = newInstance().status(200);
    String result = builder.toString();
    assertThat(result).isEqualTo("ProblemBuilder[status=200]");
  }

  @Test
  void givenOnlyNumberExtensions_whenToString_thenContainsNumbers() {
    ProblemBuilder builder = newInstance().extension("ext1", 123).extension("ext2", 456.78);
    String result = builder.toString();
    assertThat(result).contains("ext1=123");
    assertThat(result).contains("ext2=456.78");
  }

  @Test
  void givenOnlyBooleanExtensions_whenToString_thenContainsBooleans() {
    ProblemBuilder builder = newInstance().extension("flag1", true).extension("flag2", false);

    String result = builder.toString();
    assertThat(result).contains("flag1=true");
    assertThat(result).contains("flag2=false");
  }

  @Test
  void givenNonPrimitiveExtension_whenToString_thenUsesItsToString() {
    ProblemBuilder builder = newInstance();
    builder.extension("obj", new DummyObject("biz\tbar"));
    String result = builder.toString();
    assertThat(result).contains("obj=DummyObject{value=biz\tbar}");
  }

  @Test
  void givenTypeSetToBlankType_whenToString_thenTypeOmitted() {
    ProblemBuilder builder = newInstance().type(Problem.BLANK_TYPE).status(200);
    String result = builder.toString();
    assertThat(result).doesNotContain("type=");
    assertThat(result).contains("status=200");
  }

  @Test
  void givenCustomResolver_whenStatusIsKnown_thenUsesResolvedTitle() {
    StatusTitleResolver resolver =
        status -> status == 200 ? Optional.of("Custom OK") : Optional.empty();

    Problem problem = new DefaultProblemBuilder(resolver).status(200).build();

    assertThat(problem.getTitle()).isEqualTo("Custom OK");
  }

  @Test
  void givenCustomResolver_whenStatusNotResolved_thenUsesUnknownTitle() {
    StatusTitleResolver resolver = status -> Optional.empty();

    Problem problem = new DefaultProblemBuilder(resolver).status(200).build();

    assertThat(problem.getTitle()).isEqualTo(Problem.UNKNOWN_TITLE);
  }

  @Test
  void givenCustomResolverAndExplicitTitle_whenBuild_thenExplicitTitleTakesPrecedence() {
    StatusTitleResolver resolver = status -> Optional.of("Should Not Be Used");

    Problem problem =
        new DefaultProblemBuilder(resolver).status(200).title("Explicit Title").build();

    assertThat(problem.getTitle()).isEqualTo("Explicit Title");
  }

  @Test
  void givenBuilderWithState_whenSerialized_thenDeserializedCanBuild() throws Exception {
    ProblemBuilder original =
        new DefaultProblemBuilder(new DefaultStatusTitleResolver())
            .type("https://example.com/problem")
            .status(400)
            .detail("Something went wrong")
            .instance("https://example.com/instance")
            .extension("key", "value");

    ProblemBuilder deserialized = Serialization.roundTrip(original);
    Problem result = deserialized.build();

    assertThat(deserialized).isNotSameAs(original);
    assertThat(result.getType()).isEqualTo(URI.create("https://example.com/problem"));
    assertThat(result.getStatus()).isEqualTo(400);
    assertThat(result.getDetail()).isEqualTo("Something went wrong");
    assertThat(result.getInstance()).isEqualTo(URI.create("https://example.com/instance"));
    assertThat(result.getExtensions().get("key")).isEqualTo("value");
  }
}
