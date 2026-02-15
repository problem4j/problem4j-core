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

import static io.github.problem4j.core.MapUtils.mapOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools. These
 * tests help ensure that the coverage reports correctly reflect different execution paths, edge
 * cases, and instrumentation scenarios.
 */
class ProblemImplTest {

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

    Problem problem = new ProblemImpl(type, title, status, detail, instance, extensions);

    String result = problem.toString();

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
    Map<String, Object> extensions = new HashMap<>();
    Problem problem = new ProblemImpl(ProblemStatus.OK_TITLE, 200, extensions);

    String result = problem.toString();

    assertThat(result).isEqualTo("Problem{title=OK, status=200}");
  }

  @Test
  void givenOnlyNumberExtensions_whenToString_thenContainsNumbers() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("ext1", 123);
    extensions.put("ext2", 456.78);

    Problem problem = new ProblemImpl(ProblemStatus.INTERNAL_SERVER_ERROR_TITLE, 0, extensions);

    String result = problem.toString();

    assertThat(result).contains("ext1=123");
    assertThat(result).contains("ext2=456.78");
  }

  @Test
  void givenOnlyBooleanExtensions_whenToString_thenContainsBooleans() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("flag1", true);
    extensions.put("flag2", false);

    Problem problem = new ProblemImpl(ProblemStatus.INTERNAL_SERVER_ERROR_TITLE, 0, extensions);

    String result = problem.toString();

    assertThat(result).contains("flag1=true");
    assertThat(result).contains("flag2=false");
  }

  @Test
  void givenNonPrimitiveExtension_whenToString_thenUsesItsToString() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("obj", new DummyObject("biz\tbar"));

    Problem problem = new ProblemImpl(ProblemStatus.INTERNAL_SERVER_ERROR_TITLE, 0, extensions);

    String result = problem.toString();

    assertThat(result).contains("obj=DummyObject{value=biz\tbar}");
  }

  @Test
  void givenStringWithSpecialCharacters_whenToString_thenProperlyHandlesIt() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("ext", "a\"b\\c\nd");

    Problem problem = new ProblemImpl(ProblemStatus.INTERNAL_SERVER_ERROR_TITLE, 0, extensions);

    String result = problem.toString();

    assertThat(result).contains("ext=a\"b\\c\nd");
  }

  @Test
  void givenTwoEqualProblems_shouldBeEqual() {
    Problem problem1 = new ProblemImpl("title", 404, "detail", mapOf("key", "value"));
    Problem problem2 = new ProblemImpl("title", 404, "detail", mapOf("key", "value"));

    assertThat(problem1).isEqualTo(problem2);
  }

  @Test
  void givenSameProblems_shouldBeEqual() {
    Object problem;
    Object other;

    problem = other = new ProblemImpl("title", 404, "detail", mapOf("key", "value"));

    assertThat(problem).isEqualTo(other);
  }

  @Test
  void givenTwoDifferentProblems_shouldNotBeEqual() {
    Problem problem1 = new ProblemImpl("title1", 404, "detail1", mapOf("key", "value1"));
    Problem problem2 = new ProblemImpl("title2", 500, "detail2", mapOf("key", "value2"));

    assertThat(problem1).isNotEqualTo(problem2);
  }

  @Test
  void givenProblemAndDifferentObject_shouldNotBeEqual() {
    Object problem = new ProblemImpl("title1", 404, "detail1", mapOf("key", "value"));
    Object differentObject = "always wanted to be a problem";

    assertThat(problem).isNotEqualTo(differentObject);
  }

  @Test
  void givenTwoEqualProblems_shouldHaveSameHashCode() {
    Problem problem1 = new ProblemImpl("title", 404, "detail", mapOf("key", "value"));
    Problem problem2 = new ProblemImpl("title", 404, "detail", mapOf("key", "value"));

    assertThat(problem1.hashCode()).isEqualTo(problem2.hashCode());
  }

  @Test
  void givenTwoDifferentProblems_shouldHaveDifferentHashCodes() {
    Problem problem1 = new ProblemImpl("title1", 404, "detail1", mapOf("key", "value1"));
    Problem problem2 = new ProblemImpl("title2", 500, "detail2", mapOf("key", "value2"));

    assertThat(problem1.hashCode()).isNotEqualTo(problem2.hashCode());
  }

  @Test
  void givenTwoEqualExtensions_shouldBeEqual() {
    Problem.Extension ext1 = new ProblemImpl.ExtensionImpl("key", "value");
    Problem.Extension ext2 = new ProblemImpl.ExtensionImpl("key", "value");

    assertThat(ext1).isEqualTo(ext2);
  }

  @Test
  void givenSameExtensions_shouldBeEqual() {
    Object ext;
    Object other;

    ext = other = new ProblemImpl.ExtensionImpl("key", "value");

    assertThat(ext).isEqualTo(other);
  }

  @Test
  void givenTwoDifferentExtensions_shouldNotBeEqual() {
    Problem.Extension ext1 = new ProblemImpl.ExtensionImpl("key1", "value1");
    Problem.Extension ext2 = new ProblemImpl.ExtensionImpl("key2", "value2");

    assertThat(ext1).isNotEqualTo(ext2);
  }

  @Test
  void givenExtensionAndDifferentObject_shouldNotBeEqual() {
    Object ext = new ProblemImpl.ExtensionImpl("key", "value");
    Object differentObject = "always wanted to be a problem";

    assertThat(ext).isNotEqualTo(differentObject);
  }

  @Test
  void givenTwoEqualExtensions_shouldHaveSameHashCode() {
    Problem.Extension ext1 = new ProblemImpl.ExtensionImpl("key", "value");
    Problem.Extension ext2 = new ProblemImpl.ExtensionImpl("key", "value");

    assertThat(ext1.hashCode()).isEqualTo(ext2.hashCode());
  }

  @Test
  void givenTwoDifferentExtensions_shouldHaveDifferentHashCodes() {
    Problem.Extension ext1 = new ProblemImpl.ExtensionImpl("key1", "value1");
    Problem.Extension ext2 = new ProblemImpl.ExtensionImpl("key2", "value2");

    assertThat(ext1.hashCode()).isNotEqualTo(ext2.hashCode());
  }
}
