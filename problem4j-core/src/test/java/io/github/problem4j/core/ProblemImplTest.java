/*
 * Copyright (c) 2025 Damian Malczewski
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
 * SPDX-License-Identifier: MIT
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
  void givenAllFieldsPopulated_whenToString_thenContainsAllFieldsProperly() {
    URI type = URI.create("https://example.com/problem");
    String title = "Test Problem";
    int status = 400;
    String detail = "Something went wrong";
    URI instance = URI.create("https://example.com/instance");
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("stringExt", "value");
    extensions.put("numberExt", 42);
    extensions.put("booleanExt", true);
    extensions.put("objectExt", new DummyObject("foo"));

    Problem problem = new ProblemImpl(type, title, status, detail, instance, extensions);

    String result = problem.toString();

    assertThat(result).contains("\"type\" : \"" + JsonEscape.escape(type.toString()) + "\"");
    assertThat(result).contains("\"title\" : \"" + JsonEscape.escape(title) + "\"");
    assertThat(result).contains("\"status\" : " + status);
    assertThat(result).contains("\"detail\" : \"" + JsonEscape.escape(detail) + "\"");
    assertThat(result)
        .contains("\"instance\" : \"" + JsonEscape.escape(instance.toString()) + "\"");
    assertThat(result).contains("\"stringExt\" : \"" + JsonEscape.escape("value") + "\"");
    assertThat(result).contains("\"numberExt\" : 42");
    assertThat(result).contains("\"booleanExt\" : true");
    assertThat(result).contains("\"objectExt\" : \"DummyObject:" + JsonEscape.escape("foo") + "\"");
  }

  @Test
  void givenNullExtensionsAndNullableFields_whenToString_thenOmitsNulls() {
    Map<String, Object> extensions = new HashMap<>();
    Problem problem = new ProblemImpl(null, null, 200, null, null, extensions);

    String result = problem.toString();

    assertThat(result).isEqualTo("{ \"status\" : 200 }");
  }

  @Test
  void givenOnlyStringExtensions_whenToString_thenContainsQuotedStrings() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("ext1", "value1");
    extensions.put("ext2", "value2");

    Problem problem = new ProblemImpl(null, null, 0, null, null, extensions);

    String result = problem.toString();

    assertThat(result).contains("\"ext1\" : \"" + JsonEscape.escape("value1") + "\"");
    assertThat(result).contains("\"ext2\" : \"" + JsonEscape.escape("value2") + "\"");
  }

  @Test
  void givenOnlyNumberExtensions_whenToString_thenContainsNumbers() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("ext1", 123);
    extensions.put("ext2", 456.78);

    Problem problem = new ProblemImpl(null, null, 0, null, null, extensions);

    String result = problem.toString();

    assertThat(result).contains("\"ext1\" : 123");
    assertThat(result).contains("\"ext2\" : 456.78");
  }

  @Test
  void givenOnlyBooleanExtensions_whenToString_thenContainsBooleans() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("flag1", true);
    extensions.put("flag2", false);

    Problem problem = new ProblemImpl(null, null, 0, null, null, extensions);

    String result = problem.toString();

    assertThat(result).contains("\"flag1\" : true");
    assertThat(result).contains("\"flag2\" : false");
  }

  @Test
  void givenNonPrimitiveExtension_whenToString_thenUsesClassNamePrefix() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("obj", new DummyObject("bar"));

    Problem problem = new ProblemImpl(null, null, 0, null, null, extensions);

    String result = problem.toString();

    assertThat(result).contains("\"obj\" : \"DummyObject:" + JsonEscape.escape("bar") + "\"");
  }

  @Test
  void givenStringWithSpecialCharacters_whenToString_thenProperlyEscapes() {
    Map<String, Object> extensions = new HashMap<>();
    extensions.put("ext", "a\"b\\c\nd");

    Problem problem = new ProblemImpl(null, null, 0, null, null, extensions);

    String result = problem.toString();

    assertThat(result).contains("\"ext\" : \"" + JsonEscape.escape("a\"b\\c\nd") + "\"");
  }

  @Test
  void givenTwoEqualProblems_shouldBeEqual() {
    Problem problem1 = new ProblemImpl(null, "title", 404, "detail", null, mapOf("key", "value"));
    Problem problem2 = new ProblemImpl(null, "title", 404, "detail", null, mapOf("key", "value"));

    assertThat(problem1).isEqualTo(problem2);
  }

  @Test
  void givenSameProblems_shouldBeEqual() {
    Object problem;
    Object other;

    problem = other = new ProblemImpl(null, "title", 404, "detail", null, mapOf("key", "value"));

    assertThat(problem).isEqualTo(other);
  }

  @Test
  void givenTwoDifferentProblems_shouldNotBeEqual() {
    Problem problem1 =
        new ProblemImpl(null, "title1", 404, "detail1", null, mapOf("key", "value1"));
    Problem problem2 =
        new ProblemImpl(null, "title2", 500, "detail2", null, mapOf("key", "value2"));

    assertThat(problem1).isNotEqualTo(problem2);
  }

  @Test
  void givenProblemAndDifferentObject_shouldNotBeEqual() {
    Object problem = new ProblemImpl(null, "title1", 404, "detail1", null, mapOf("key", "value"));
    Object differentObject = "always wanted to be a problem";

    assertThat(problem).isNotEqualTo(differentObject);
  }

  @Test
  void givenTwoEqualProblems_shouldHaveSameHashCode() {
    Problem problem1 = new ProblemImpl(null, "title", 404, "detail", null, mapOf("key", "value"));
    Problem problem2 = new ProblemImpl(null, "title", 404, "detail", null, mapOf("key", "value"));

    assertThat(problem1.hashCode()).isEqualTo(problem2.hashCode());
  }

  @Test
  void givenTwoDifferentProblems_shouldHaveDifferentHashCodes() {
    Problem problem1 =
        new ProblemImpl(null, "title1", 404, "detail1", null, mapOf("key", "value1"));
    Problem problem2 =
        new ProblemImpl(null, "title2", 500, "detail2", null, mapOf("key", "value2"));

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

  private static class DummyObject {

    private final String value;

    DummyObject(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }
}
