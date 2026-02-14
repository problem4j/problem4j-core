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

import static io.github.problem4j.core.MapUtils.mapOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools. These
 * tests help ensure that the coverage reports correctly reflect different execution paths, edge
 * cases, and instrumentation scenarios.
 */
class ProblemBuilderTest {

  @Test
  void givenNullURIType_shouldNotSetIt() {
    Problem problem = Problem.builder().type((URI) null).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenNullStringType_shouldNotSetIt() {
    Problem problem = Problem.builder().type((String) null).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenBlankURIType_shouldNotSetIt() {
    Problem problem = Problem.builder().type(Problem.BLANK_TYPE).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenBlankStringType_shouldNotSetIt() {
    Problem problem = Problem.builder().type(Problem.BLANK_TYPE.toString()).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.isTypeNonBlank()).isFalse();
  }

  @Test
  void givenNullProblemStatus_shouldNotSetTitleOrStatus() {
    Problem problem = Problem.builder().status(null).build();

    assertThat(problem.getStatus()).isZero();
    assertThat(problem.getTitle()).isNull();
  }

  @Test
  void givenProblemStatus_shouldSetNumericStatusAndTitle() {
    Problem problem = Problem.builder().status(ProblemStatus.BAD_REQUEST).build();

    assertThat(problem.getStatus()).isEqualTo(ProblemStatus.BAD_REQUEST.getStatus());
    assertThat(problem.getTitle()).isEqualTo(ProblemStatus.BAD_REQUEST.getTitle());
  }

  @Test
  void givenProblemStatus_shouldPreferExplicitStatusValueWhenSetEarlier() {
    Problem problem = Problem.builder().status(405).status(ProblemStatus.I_AM_A_TEAPOT).build();

    assertThat(problem.getStatus()).isEqualTo(ProblemStatus.I_AM_A_TEAPOT.getStatus());
    assertThat(problem.getTitle()).isEqualTo(ProblemStatus.I_AM_A_TEAPOT.getTitle());
  }

  @Test
  void givenExplicitTitle_thenStatusProblemStatus_shouldNotOverrideTitle() {
    Problem problem =
        Problem.builder().title("Custom Title").status(ProblemStatus.BAD_REQUEST).build();

    assertThat(problem.getStatus()).isEqualTo(ProblemStatus.BAD_REQUEST.getStatus());
    assertThat(problem.getTitle()).isEqualTo("Custom Title");
  }

  @Test
  void givenStatusProblemStatus_thenExplicitTitle_shouldOverrideDerivedTitle() {
    Problem problem = Problem.builder().status(ProblemStatus.NOT_FOUND).title("My Title").build();

    assertThat(problem.getStatus()).isEqualTo(ProblemStatus.NOT_FOUND.getStatus());
    assertThat(problem.getTitle()).isEqualTo("My Title");
  }

  @Test
  void givenInvalidTypeString_shouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Problem.builder().type("ht tp://not a uri"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenInvalidInstanceString_shouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> Problem.builder().instance("::://invalid"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void givenNullURIInstance_shouldNotSetIt() {
    Problem problem = Problem.builder().instance((URI) null).build();

    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenNullStringInstance_shouldNotSetIt() {
    Problem problem = Problem.builder().instance((String) null).build();

    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenNullNameExtension_shouldIgnoreIt() {
    Problem problem = Problem.builder().extension(null, "value").build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenNullValueExtension_shouldNotIncludeIt() {
    Problem problem = Problem.builder().extension("key", null).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.hasExtension("key")).isFalse();
    assertThat(problem.getExtensionValue("key")).isNull();
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf());
  }

  @Test
  void givenNullValueExtensionViaVarargs_shouldNotIncludeIt() {
    Problem problem =
        Problem.builder()
            .extension(Problem.extension("key1", null), Problem.extension("key2", null))
            .build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf());

    assertThat(problem.hasExtension("key1")).isFalse();
    assertThat(problem.getExtensionValue("key1")).isNull();

    assertThat(problem.hasExtension("key2")).isFalse();
    assertThat(problem.getExtensionValue("key2")).isNull();
  }

  @Test
  void givenNullValueExtensionViaObject_shouldNotIncludeIt() {
    Problem problem =
        Problem.builder()
            .extension(
                Arrays.asList(Problem.extension("key1", null), Problem.extension("key2", null)))
            .build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf());

    assertThat(problem.hasExtension("key1")).isFalse();
    assertThat(problem.getExtensionValue("key1")).isNull();

    assertThat(problem.hasExtension("key2")).isFalse();
    assertThat(problem.getExtensionValue("key2")).isNull();
  }

  @Test
  void givenNullMapExtension_shouldIgnoreIt() {
    Problem problem = Problem.builder().extension((Map<String, Object>) null).build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenMapExtensionWithNullKey_shouldIgnoreNullKey() {
    Map<String, Object> map = new HashMap<>();
    map.put(null, "ignored");
    map.put("a", "b");

    Problem problem = Problem.builder().extension(map).build();

    assertThat(problem.getExtensions()).containsExactly("a");
    assertThat(problem.hasExtension("a")).isTrue();
    assertThat(problem.getExtensionValue("a")).isEqualTo("b");
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf("a", "b"));
  }

  @Test
  void givenMapExtensionWithNullValue_shouldIgnoreNullValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("ignored", null);
    map.put("a", "b");

    Problem problem = Problem.builder().extension(map).build();

    assertThat(problem.getExtensions()).containsExactly("a");
    assertThat(problem.hasExtension("a")).isTrue();
    assertThat(problem.getExtensionValue("a")).isEqualTo("b");
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf("a", "b"));
  }

  @Test
  void givenNullVarargArray_shouldIgnoreIt() {
    Problem problem = Problem.builder().extension((Problem.Extension[]) null).build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenVarargWithNullElement_shouldIgnoreNullElement() {
    Problem problem =
        Problem.builder()
            .extension(Problem.extension("a", 1), null, Problem.extension("b", 2))
            .build();

    assertThat(problem.getExtensions()).containsExactlyInAnyOrder("a", "b");
    assertThat(problem.hasExtension("a")).isTrue();
    assertThat(problem.hasExtension("b")).isTrue();
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf("a", 1, "b", 2));
  }

  @Test
  void givenNullCollection_shouldIgnoreIt() {
    Problem problem = Problem.builder().extension((Collection<Problem.Extension>) null).build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenCollectionWithNullElement_shouldIgnoreNullElement() {
    Problem problem =
        Problem.builder()
            .extension(
                Arrays.asList(Problem.extension("x", "1"), null, Problem.extension("y", "2")))
            .build();

    assertThat(problem.getExtensions()).containsExactlyInAnyOrder("x", "y");
    assertThat(problem.hasExtension("x")).isTrue();
    assertThat(problem.hasExtension("y")).isTrue();
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf("x", "1", "y", "2"));
  }

  @Test
  void givenNumericStatus_shouldDeriveTitleWhenKnown() {
    Problem problem = Problem.builder().status(ProblemStatus.MULTI_STATUS.getStatus()).build();

    assertThat(problem.getStatus()).isEqualTo(ProblemStatus.MULTI_STATUS.getStatus());
    assertThat(problem.getTitle()).isEqualTo(ProblemStatus.MULTI_STATUS.getTitle());
  }

  @Test
  void givenUnknownNumericStatus_shouldNotDeriveTitle() {
    Problem problem = Problem.builder().status(999).build();

    assertThat(problem.getStatus()).isEqualTo(999);
    assertThat(problem.getTitle()).isNull();
  }

  @Test
  void givenTypeAndInstance_stringOverloads_shouldAcceptValidUris() {
    String t = "http://example.org/type";
    String i = "http://example.org/instance";

    Problem problem = Problem.builder().type(t).instance(i).build();

    assertThat(problem.getType()).isEqualTo(URI.create(t));
    assertThat(problem.getInstance()).isEqualTo(URI.create(i));
  }

  @Test
  void givenEmptyMapExtension_shouldBeIgnored() {
    Map<String, Object> m = new HashMap<>();
    Problem problem = Problem.builder().extension(m).build();

    assertThat(problem.getExtensions()).isEmpty();
    assertThat(problem.getExtensionMembers()).isEqualTo(Collections.emptyMap());
  }

  @Test
  void givenAssigningTheSameExtensionLater_shouldOverwriteEarlierValues() {
    Problem problem =
        Problem.builder().extension("k", "v1").extension(Problem.extension("k", "v2")).build();

    assertThat(problem.getExtensionValue("k")).isEqualTo("v2");
    assertThat(problem.getExtensions()).containsExactly("k");
    assertThat(problem.getExtensionMembers()).isEqualTo(mapOf("k", "v2"));
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
        Problem.builder().type(type).title(title).status(status).detail(detail).instance(instance);
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
  void givenNullExtensionsAndNullableFields_whenToString_thenOmitsNulls() {
    ProblemBuilder builder = Problem.builder().status(200);
    String result = builder.toString();
    assertThat(result).isEqualTo("ProblemBuilder{status=200}");
  }

  @Test
  void givenOnlyNumberExtensions_whenToString_thenContainsNumbers() {
    ProblemBuilder builder = Problem.builder();
    builder.extension("ext1", 123);
    builder.extension("ext2", 456.78);
    String result = builder.toString();
    assertThat(result).contains("ext1=123");
    assertThat(result).contains("ext2=456.78");
  }

  @Test
  void givenOnlyBooleanExtensions_whenToString_thenContainsBooleans() {
    ProblemBuilder builder = Problem.builder();
    builder.extension("flag1", true);
    builder.extension("flag2", false);
    String result = builder.toString();
    assertThat(result).contains("flag1=true");
    assertThat(result).contains("flag2=false");
  }

  @Test
  void givenNonPrimitiveExtension_whenToString_thenUsesItsToString() {
    ProblemBuilder builder = Problem.builder();
    builder.extension("obj", new DummyObject("biz\tbar"));
    String result = builder.toString();
    assertThat(result).contains("obj=DummyObject{value=biz\tbar}");
  }
}
