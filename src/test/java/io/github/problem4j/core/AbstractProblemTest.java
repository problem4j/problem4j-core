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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools.
 */
class AbstractProblemTest {

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

    Problem problem = new AbstractProblem(type, title, status, detail, instance, extensions) {};

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
    Problem problem =
        new AbstractProblem(
            Problem.BLANK_TYPE, ProblemStatus.OK_TITLE, 200, null, null, extensions) {};

    String result = problem.toString();

    assertThat(result).isEqualTo("Problem{title=OK, status=200}");
  }

  @Test
  void givenStatusOnly_whenCreatingProblem_thenTitleIsDerivedFromStatus() {
    Problem problem = new AbstractProblem(404) {};

    assertThat(problem.getStatus()).isEqualTo(404);
    assertThat(problem.getTitle()).isEqualTo("Not Found");
    assertThat(problem.getDetail()).isNull();
    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
  }

  @Test
  void givenStatusAndDetail_whenCreatingProblem_thenTitleDerivedAndDetailSet() {
    Problem problem = new AbstractProblem(500, "server exploded") {};

    assertThat(problem.getStatus()).isEqualTo(500);
    assertThat(problem.getTitle()).isEqualTo("Internal Server Error");
    assertThat(problem.getDetail()).isEqualTo("server exploded");
  }

  @Test
  void givenTitleAndStatus_whenCreatingProblem_thenFieldsAreSet() {
    Problem problem = new AbstractProblem("Custom Title", 400) {};

    assertThat(problem.getTitle()).isEqualTo("Custom Title");
    assertThat(problem.getStatus()).isEqualTo(400);
    assertThat(problem.getDetail()).isNull();
  }

  @Test
  void givenTitleStatusAndDetail_whenCreatingProblem_thenFieldsAreSet() {
    Problem problem = new AbstractProblem("Custom Title", 422, "invalid input") {};

    assertThat(problem.getTitle()).isEqualTo("Custom Title");
    assertThat(problem.getStatus()).isEqualTo(422);
    assertThat(problem.getDetail()).isEqualTo("invalid input");
    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenTypeTitleAndStatus_whenCreatingProblem_thenFieldsAreSet() {
    URI type = URI.create("urn:test");
    Problem problem = new AbstractProblem(type, "Typed", 409) {};

    assertThat(problem.getType()).isEqualTo(type);
    assertThat(problem.getTitle()).isEqualTo("Typed");
    assertThat(problem.getStatus()).isEqualTo(409);
    assertThat(problem.getDetail()).isNull();
  }

  @Test
  void givenTypeTitleStatusAndDetail_whenCreatingProblem_thenFieldsAreSet() {
    URI type = URI.create("urn:test");
    Problem problem = new AbstractProblem(type, "Typed", 409, "conflict detail") {};

    assertThat(problem.getType()).isEqualTo(type);
    assertThat(problem.getTitle()).isEqualTo("Typed");
    assertThat(problem.getStatus()).isEqualTo(409);
    assertThat(problem.getDetail()).isEqualTo("conflict detail");
  }

  @Test
  void givenNullExtensionsMap_whenCreatingProblem_thenExtensionsAreEmpty() {
    Problem problem = new AbstractProblem(Problem.BLANK_TYPE, "T", 200, null, null, null) {};

    assertThat(problem.getExtensionMembers()).isEmpty();
  }

  @Test
  void givenUnknownStatus_whenFindTitle_thenReturnsUnknownTitle() {
    Problem problem = new AbstractProblem(999) {};

    assertThat(problem.getTitle()).isEqualTo(Problem.UNKNOWN_TITLE);
  }

  @SuppressWarnings("deprecation")
  @Test
  void givenExtension_whenSetValue_thenValueIsUpdated() {
    Problem.Extension ext = new AbstractProblem.AbstractExtension("key", "original") {};

    Object result = ext.setValue("updated");

    assertThat(result).isEqualTo("updated");
    assertThat(ext.getValue()).isEqualTo("updated");
  }

  @Test
  void givenExtension_whenToString_thenFormattedCorrectly() {
    Problem.Extension ext = new AbstractProblem.AbstractExtension("myKey", 42) {};

    assertThat(ext.toString()).isEqualTo("Extension{myKey=42}");
  }

  @Test
  void givenExtensionWithNullValue_whenToString_thenFormattedCorrectly() {
    Problem.Extension ext = new AbstractProblem.AbstractExtension("myKey", null) {};

    assertThat(ext.toString()).isEqualTo("Extension{myKey=null}");
  }

  @Test
  void givenTwoProblemsWithDifferentTypes_whenEquals_thenNotEqual() {
    Problem p1 = new AbstractProblem(URI.create("urn:type1"), "T", 400) {};
    Problem p2 = new AbstractProblem(URI.create("urn:type2"), "T", 400) {};

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentTitles_whenEquals_thenNotEqual() {
    Problem p1 = new AbstractProblem("T1", 400) {};
    Problem p2 = new AbstractProblem("T2", 400) {};

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentStatuses_whenEquals_thenNotEqual() {
    Problem p1 = new AbstractProblem("T", 400) {};
    Problem p2 = new AbstractProblem("T", 500) {};

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentDetails_whenEquals_thenNotEqual() {
    Problem p1 = new AbstractProblem("T", 400, "d1") {};
    Problem p2 = new AbstractProblem("T", 400, "d2") {};

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentInstances_whenEquals_thenNotEqual() {
    Problem p1 =
        new AbstractProblem(Problem.BLANK_TYPE, "T", 400, null, URI.create("urn:i1"), null) {};
    Problem p2 =
        new AbstractProblem(Problem.BLANK_TYPE, "T", 400, null, URI.create("urn:i2"), null) {};

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentExtensions_whenEquals_thenNotEqual() {
    Problem p1 =
        new AbstractProblem(Problem.BLANK_TYPE, "T", 400, null, null, Map.of("k", "v1")) {};
    Problem p2 =
        new AbstractProblem(Problem.BLANK_TYPE, "T", 400, null, null, Map.of("k", "v2")) {};

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoExtensionsWithSameKeyDifferentValue_whenEquals_thenNotEqual() {
    Problem.Extension e1 = new AbstractProblem.AbstractExtension("k", "v1") {};
    Problem.Extension e2 = new AbstractProblem.AbstractExtension("k", "v2") {};

    assertThat(e1).isNotEqualTo(e2);
  }
}
