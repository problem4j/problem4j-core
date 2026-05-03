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
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools.
 */
class DefaultProblemTest {

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

    Problem problem = new DefaultProblem(type, title, status, detail, instance, extensions);

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
    Problem problem = new DefaultProblem(Problem.BLANK_TYPE, "OK", 200, null, null, extensions);

    String result = problem.toString();

    assertThat(result).isEqualTo("Problem[title=OK, status=200]");
  }

  @Test
  void givenNullExtensionsMap_whenCreatingProblem_thenExtensionsAreEmpty() {
    Problem problem = new DefaultProblem(Problem.BLANK_TYPE, "T", 200, null, null, null);

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenExtension_whenToString_thenFormattedCorrectly() {
    Problem.Extension ext = new DefaultProblem.DefaultExtension("myKey", 42);

    assertThat(ext.toString()).isEqualTo("Extension[myKey=42]");
  }

  @Test
  void givenExtensionWithNullValue_whenToString_thenFormattedCorrectly() {
    Problem.Extension ext = new DefaultProblem.DefaultExtension("myKey", null);

    assertThat(ext.toString()).isEqualTo("Extension[myKey=null]");
  }

  @Test
  void givenTwoProblemsWithDifferentInstances_whenEquals_thenNotEqual() {
    Problem p1 = new DefaultProblem(Problem.BLANK_TYPE, "T", 400, null, URI.create("urn:i1"), null);
    Problem p2 = new DefaultProblem(Problem.BLANK_TYPE, "T", 400, null, URI.create("urn:i2"), null);

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentExtensions_whenEquals_thenNotEqual() {
    Problem p1 = new DefaultProblem(Problem.BLANK_TYPE, "T", 400, null, null, Map.of("k", "v1"));
    Problem p2 = new DefaultProblem(Problem.BLANK_TYPE, "T", 400, null, null, Map.of("k", "v2"));

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoExtensionsWithSameKeyDifferentValue_whenEquals_thenNotEqual() {
    Problem.Extension e1 = new DefaultProblem.DefaultExtension("k", "v1");
    Problem.Extension e2 = new DefaultProblem.DefaultExtension("k", "v2");

    assertThat(e1).isNotEqualTo(e2);
  }

  @Test
  void givenTwoProblemsWithAllFieldsSame_whenHashCode_thenCanBeUsedInCollections() {
    Problem p1 =
        new DefaultProblem(
            URI.create("http://example.org/type"),
            "Title",
            400,
            "Detail",
            URI.create("http://example.org/instance"),
            Map.of("key", "value"));
    Problem p2 =
        new DefaultProblem(
            URI.create("http://example.org/type"),
            "Title",
            400,
            "Detail",
            URI.create("http://example.org/instance"),
            Map.of("key", "value"));

    assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
  }

  @Test
  void givenProblem_whenEqualToItself_thenReturnsTrue() {
    Problem p1 =
        new DefaultProblem(
            URI.create("http://example.org/type"),
            "Title",
            400,
            "Detail",
            URI.create("http://example.org/instance"),
            null);

    assertThat(p1).isEqualTo(p1);
  }

  @Test
  void givenProblem_whenComparedToNull_thenReturnsNotEqual() {
    Problem p1 = new DefaultProblem(Problem.BLANK_TYPE, "Title", 400, null, null, null);

    assertThat(p1).isNotEqualTo(null);
  }

  @Test
  void givenProblem_whenComparedToStringObject_thenReturnsNotEqual() {
    Problem p1 = new DefaultProblem(Problem.BLANK_TYPE, "Title", 400, null, null, null);

    assertThat(p1).isNotEqualTo("not a problem");
  }

  @Test
  void givenTwoProblemsWithDifferentTypes_whenEquals_thenNotEqual() {
    Problem p1 =
        new DefaultProblem(URI.create("http://example.org/type1"), "Title", 400, null, null, null);
    Problem p2 =
        new DefaultProblem(URI.create("http://example.org/type2"), "Title", 400, null, null, null);

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentTitles_whenEquals_thenNotEqual() {
    Problem p1 = new DefaultProblem(Problem.BLANK_TYPE, "Title1", 400, null, null, null);
    Problem p2 = new DefaultProblem(Problem.BLANK_TYPE, "Title2", 400, null, null, null);

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenTwoProblemsWithDifferentDetails_whenEquals_thenNotEqual() {
    Problem p1 = new DefaultProblem(Problem.BLANK_TYPE, "Title", 400, "Detail1", null, null);
    Problem p2 = new DefaultProblem(Problem.BLANK_TYPE, "Title", 400, "Detail2", null, null);

    assertThat(p1).isNotEqualTo(p2);
  }

  @Test
  void givenExtension_whenHashCode_thenCanBeUsedInCollections() {
    Problem.Extension e1 = new DefaultProblem.DefaultExtension("k", "v");
    Problem.Extension e2 = new DefaultProblem.DefaultExtension("k", "v");

    assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
  }

  @Test
  void givenExtension_whenEqualToItself_thenReturnsTrue() {
    Problem.Extension ext = new DefaultProblem.DefaultExtension("key", "value");

    assertThat(ext).isEqualTo(ext);
  }

  @Test
  void givenExtension_whenComparedToString_thenReturnsNotEqual() {
    Problem.Extension ext = new DefaultProblem.DefaultExtension("key", "value");

    assertThat(ext).isNotEqualTo("key=value");
  }

  @Test
  void givenTwoExtensionsWithDifferentKeys_whenEquals_thenNotEqual() {
    Problem.Extension e1 = new DefaultProblem.DefaultExtension("k1", "v");
    Problem.Extension e2 = new DefaultProblem.DefaultExtension("k2", "v");

    assertThat(e1).isNotEqualTo(e2);
  }

  @Test
  void givenCustomProblemImplementation_whenEquals_thenEqualIfFieldsMatch() {
    URI type = URI.create("http://example.org/type");
    String title = "Title";
    int status = 400;
    String detail = "Detail";
    URI instance = URI.create("http://example.org/instance");
    Map<String, Object> ext = Map.of("k", "v");

    Problem defaultProblem = new DefaultProblem(type, title, status, detail, instance, ext);
    Problem customProblem =
        new Problem() {
          @Override
          public URI getType() {
            return type;
          }

          @Override
          public String getTitle() {
            return title;
          }

          @Override
          public int getStatus() {
            return status;
          }

          @Override
          public String getDetail() {
            return detail;
          }

          @Override
          public URI getInstance() {
            return instance;
          }

          @Override
          public Map<String, Object> getExtensions() {
            return ext;
          }
        };

    assertThat(defaultProblem).isEqualTo(customProblem);
  }

  @Test
  void givenCustomExtensionImplementation_whenEquals_thenEqualIfFieldsMatch() {
    Problem.Extension defaultExt = new DefaultProblem.DefaultExtension("key", "value");
    Problem.Extension customExt =
        new Problem.Extension() {
          @Override
          public String getName() {
            return "key";
          }

          @Override
          public Object getValue() {
            return "value";
          }
        };

    assertThat(defaultExt).isEqualTo(customExt);
  }

  @Test
  void givenProblemWithAllFields_whenSerialized_thenDeserializedEqualsOriginal() throws Exception {
    Problem original =
        new DefaultProblem(
            URI.create("https://example.com/problem"),
            "Test Problem",
            400,
            "Something went wrong",
            URI.create("https://example.com/instance"),
            Map.of("key", "value"));

    Problem deserialized = Serialization.roundTrip(original);

    assertThat(deserialized).isNotSameAs(original);
    assertThat(deserialized).isEqualTo(original);
  }

  @Test
  void givenMinimalProblem_whenSerialized_thenDeserializedEqualsOriginal() throws Exception {
    Problem original = new DefaultProblem(Problem.BLANK_TYPE, "OK", 200, null, null, null);

    Problem deserialized = Serialization.roundTrip(original);

    assertThat(deserialized).isNotSameAs(original);
    assertThat(deserialized).isEqualTo(original);
    assertThat(deserialized.getDetail()).isNull();
    assertThat(deserialized.getInstance()).isNull();
  }
}
