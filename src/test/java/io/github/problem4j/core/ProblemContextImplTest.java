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
class ProblemContextImplTest {

  @Test
  void givenEmptyContext_whenCreated_thenContextIsEmpty() {
    ProblemContext context = new ProblemContextImpl();

    assertThat(context.toMap()).isEmpty();
  }

  @Test
  void givenEmptyContext_whenPuttingValue_thenValueIsStored() {
    ProblemContext context = new ProblemContextImpl();

    context.put("key1", "value1");

    assertThat(context.get("key1")).isEqualTo("value1");
  }

  @Test
  void givenContextWithValue_whenCheckingContainsKey_thenReturnsTrue() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    assertThat(context.containsKey("key1")).isTrue();
  }

  @Test
  void givenContextWithoutKey_whenCheckingContainsKey_thenReturnsFalse() {
    ProblemContext context = new ProblemContextImpl();

    assertThat(context.containsKey("nonexistent")).isFalse();
  }

  @Test
  void givenContextWithValue_whenGettingNonexistentKey_thenReturnsNull() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    assertThat(context.get("nonexistent")).isNull();
  }

  @Test
  void givenEmptyContext_whenPuttingNullValue_thenKeyIsNotAdded() {
    ProblemContext context = new ProblemContextImpl();

    context.put("key1", null);

    assertThat(context.containsKey("key1")).isFalse();
    assertThat(context.get("key1")).isNull();
  }

  @Test
  void givenContextWithValue_whenPuttingNullValue_thenKeyIsRemoved() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    context.put("key1", null);

    assertThat(context.containsKey("key1")).isFalse();
    assertThat(context.get("key1")).isNull();
  }

  @Test
  void givenEmptyContext_whenPuttingMultipleValues_thenAllValuesAreStored() {
    ProblemContext context = new ProblemContextImpl();

    context.put("key1", "value1").put("key2", "value2").put("key3", "value3");

    assertThat(context.get("key1")).isEqualTo("value1");
    assertThat(context.get("key2")).isEqualTo("value2");
    assertThat(context.get("key3")).isEqualTo("value3");
  }

  @Test
  void givenEmptyContext_whenPuttingValue_thenReturnsSelfForChaining() {
    ProblemContext context = new ProblemContextImpl();

    ProblemContext result = context.put("key1", "value1");

    assertThat(result).isSameAs(context);
  }

  @Test
  void givenContextWithValue_whenCalling_toMap_thenReturnsImmutableMap() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    Map<String, String> map = context.toMap();

    assertThat(map).containsEntry("key1", "value1");
    assertThat(map).isUnmodifiable();
  }

  @Test
  void givenContextWithValue_whenModifyingReturnedMap_thenContextIsNotAffected() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    Map<String, String> map = context.toMap();
    assertThat(map).isUnmodifiable();
  }

  @Test
  void givenContextWithValues_whenUpdatingExistingValue_thenValueIsReplaced() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    context.put("key1", "newValue");

    assertThat(context.get("key1")).isEqualTo("newValue");
  }

  @Test
  void givenMapWithValues_whenCreatingContextFromMap_thenValuesAreCopied() {
    Map<String, String> sourceMap = new HashMap<>();
    sourceMap.put("key1", "value1");
    sourceMap.put("key2", "value2");

    ProblemContext context = new ProblemContextImpl(sourceMap);

    assertThat(context.get("key1")).isEqualTo("value1");
    assertThat(context.get("key2")).isEqualTo("value2");
  }

  @Test
  void givenMapWithValues_whenCreatingContextFromMap_thenChangesToSourceMapDontAffectContext() {
    Map<String, String> sourceMap = new HashMap<>();
    sourceMap.put("key1", "value1");

    ProblemContext context = new ProblemContextImpl(sourceMap);
    sourceMap.put("key2", "value2");

    assertThat(context.containsKey("key2")).isFalse();
  }

  @Test
  void givenContextWithValues_whenCreatingContextFromContext_thenValuesAreCopied() {
    ProblemContext source = new ProblemContextImpl();
    source.put("key1", "value1");
    source.put("key2", "value2");

    ProblemContext context = new ProblemContextImpl(source);

    assertThat(context.get("key1")).isEqualTo("value1");
    assertThat(context.get("key2")).isEqualTo("value2");
  }

  @Test
  void
      givenContextWithValues_whenCreatingContextFromContext_thenChangesToSourceDontAffectContext() {
    ProblemContext source = new ProblemContextImpl();
    source.put("key1", "value1");

    ProblemContext context = new ProblemContextImpl(source);
    source.put("key2", "value2");

    assertThat(context.containsKey("key2")).isFalse();
  }

  @Test
  void
      givenContextWithValues_whenCreatingContextFromContext_thenChangesToNewContextDontAffectSource() {
    ProblemContext source = new ProblemContextImpl();
    source.put("key1", "value1");

    ProblemContext context = new ProblemContextImpl(source);
    context.put("key2", "value2");

    assertThat(source.containsKey("key2")).isFalse();
  }

  @Test
  void givenEmptyMap_whenCreatingContext_thenContextIsEmpty() {
    ProblemContext context = new ProblemContextImpl(Collections.emptyMap());

    assertThat(context.toMap()).isEmpty();
  }

  @Test
  void givenTwoContextsWithSameValues_whenComparingEquality_thenTheyAreEqual() {
    ProblemContext context1 = new ProblemContextImpl();
    context1.put("key1", "value1");

    ProblemContext context2 = new ProblemContextImpl();
    context2.put("key1", "value1");

    assertThat(context1).isEqualTo(context2);
  }

  @Test
  void givenTwoContextsWithDifferentValues_whenComparingEquality_thenTheyAreNotEqual() {
    ProblemContext context1 = new ProblemContextImpl();
    context1.put("key1", "value1");

    ProblemContext context2 = new ProblemContextImpl();
    context2.put("key1", "value2");

    assertThat(context1).isNotEqualTo(context2);
  }

  @Test
  void givenContextAndItself_whenComparingEquality_thenTheyAreEqual() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    assertThat(context).isEqualTo(context);
  }

  @Test
  void givenContextAndNull_whenComparingEquality_thenTheyAreNotEqual() {
    ProblemContext context = new ProblemContextImpl();

    assertThat(context).isNotEqualTo(null);
  }

  @Test
  void givenContextAndNonContextObject_whenComparingEquality_thenTheyAreNotEqual() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    assertThat(context).isNotEqualTo("not a context");
  }

  @Test
  void givenTwoContextsWithSameValues_whenComparingHashCode_thenHashCodesAreEqual() {
    ProblemContext context1 = new ProblemContextImpl();
    context1.put("key1", "value1");

    ProblemContext context2 = new ProblemContextImpl();
    context2.put("key1", "value1");

    assertThat(context1.hashCode()).isEqualTo(context2.hashCode());
  }

  @Test
  void givenContextWithValues_whenCallingToString_thenReturnsMapRepresentation() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");

    String result = context.toString();

    assertThat(result).contains("key1");
    assertThat(result).contains("value1");
  }

  @Test
  void givenEmptyContext_whenCallingToString_thenReturnsEmptyMapRepresentation() {
    ProblemContext context = new ProblemContextImpl();

    String result = context.toString();

    assertThat(result).isEqualTo("ProblemContext{}");
  }

  @Test
  void givenContextWithMultipleValues_whenCallingToString_thenReturnsAllValues() {
    ProblemContext context = new ProblemContextImpl();
    context.put("key1", "value1");
    context.put("key2", "value2");

    String result = context.toString();

    assertThat(result).contains("key1");
    assertThat(result).contains("value1");
    assertThat(result).contains("key2");
    assertThat(result).contains("value2");
  }

  @Test
  void givenContextWithEmptyStringValue_whenPuttingValue_thenValueIsStored() {
    ProblemContext context = new ProblemContextImpl();

    context.put("key1", "");

    assertThat(context.get("key1")).isEqualTo("");
  }

  @Test
  void givenContextWithWhitespaceValue_whenPuttingValue_thenValueIsStored() {
    ProblemContext context = new ProblemContextImpl();

    context.put("key1", "   ");

    assertThat(context.get("key1")).isEqualTo("   ");
  }
}
