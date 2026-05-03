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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class DefaultProblemContextTest {

  @Test
  void givenContext_whenPutAll_thenEntriesAreAdded() {
    ProblemContext context = ProblemContext.create();
    Map<String, String> entries = Map.of("key1", "val1", "key2", "val2");

    ProblemContext putAllResult = context.putAll(entries);

    assertThat(context.get("key1")).isEqualTo("val1");
    assertThat(context.get("key2")).isEqualTo("val2");
    assertThat(putAllResult).isSameAs(context);
  }

  @Test
  void givenContext_whenMerge_thenEntriesAreMerged() {
    ProblemContext context1 = ProblemContext.create().put("key1", "val1");
    ProblemContext context2 = ProblemContext.create().put("key2", "val2");

    ProblemContext mergeResult = context1.merge(context2);

    assertThat(context1.get("key1")).isEqualTo("val1");
    assertThat(context1.get("key2")).isEqualTo("val2");
    assertThat(mergeResult).isSameAs(context1);
  }

  @Test
  void givenContext_whenToMap_thenReturnsImmutableMap() {
    ProblemContext context = ProblemContext.create().put("key1", "val1");

    Map<String, String> map = context.toMap();

    assertThat(map).containsEntry("key1", "val1");
    assertThatThrownBy(() -> map.put("newKey", "newVal"))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void givenContexts_whenEquals_thenEqualIfSameContent() {
    ProblemContext context1 = ProblemContext.create().put("key", "value");
    ProblemContext context2 = ProblemContext.create().put("key", "value");

    assertThat(context1).isEqualTo(context2);
  }

  @Test
  void givenContexts_whenEquals_thenNotEqualIfDifferentContent() {
    ProblemContext context1 = ProblemContext.create().put("key", "value1");
    ProblemContext context2 = ProblemContext.create().put("key", "value2");

    assertThat(context1).isNotEqualTo(context2);
  }

  @Test
  void givenContext_whenEquals_thenNotEqualToNull() {
    ProblemContext context = ProblemContext.create().put("key", "value");

    assertThat(context).isNotEqualTo(null);
  }

  @Test
  void givenContext_whenEquals_thenNotEqualToStringObject() {
    ProblemContext context = ProblemContext.create().put("key", "value");

    assertThat(context).isNotEqualTo("not a context");
  }

  @Test
  void givenContexts_whenHashCode_thenSameIfEqualContent() {
    ProblemContext context1 = ProblemContext.create().put("key", "value");
    ProblemContext context2 = ProblemContext.create().put("key", "value");

    assertThat(context1.hashCode()).isEqualTo(context2.hashCode());
  }

  @Test
  void givenContext_whenToString_thenFormattedCorrectly() {
    ProblemContext context = ProblemContext.create().put("key1", "val1").put("key2", "val2");

    String result = context.toString();

    assertThat(result).contains("key1=val1");
    assertThat(result).contains("key2=val2");
    assertThat(result).startsWith("ProblemContext[");
    assertThat(result).endsWith("]");
  }

  @Test
  void givenContext_whenEquals_thenEqualsToItself() {
    ProblemContext context = ProblemContext.create().put("key", "value");

    assertThat(context).isEqualTo(context);
  }

  @Test
  void givenContextWithMultipleEntries_whenToString_thenSorted() {
    ProblemContext context =
        ProblemContext.create().put("z", "last").put("a", "first").put("m", "middle");

    String result = context.toString();

    assertThat(result).contains("a=first");
    int posA = result.indexOf("a=first");
    int posM = result.indexOf("m=middle");
    int posZ = result.indexOf("z=last");

    assertThat(posA).isLessThan(posM);
    assertThat(posM).isLessThan(posZ);
  }

  @Test
  void givenContextWithNullValue_whenPut_thenRemoves() {
    ProblemContext context = ProblemContext.create().put("key", "value");

    context.put("key", null);

    assertThat(context.containsKey("key")).isFalse();
    assertThat(context.get("key")).isNull();
  }

  @Test
  void givenContextWithEntries_whenSerialized_thenDeserializedEqualsOriginal() throws Exception {
    ProblemContext original = ProblemContext.create().put("key1", "val1").put("key2", "val2");

    ProblemContext deserialized = Serialization.roundTrip(original);

    assertThat(deserialized).isNotSameAs(original);
    assertThat(deserialized).isEqualTo(original);
  }

  @Test
  void givenEmptyContext_whenSerialized_thenDeserializedEqualsOriginal() throws Exception {
    ProblemContext original = ProblemContext.create();

    ProblemContext deserialized = Serialization.roundTrip(original);

    assertThat(deserialized).isNotSameAs(original);
    assertThat(deserialized).isEqualTo(original);
  }

  @Test
  void givenEmptyContext_whenToString_thenFormattedCorrectly() {
    ProblemContext context = ProblemContext.create();

    String result = context.toString();

    assertThat(result).isEqualTo("ProblemContext[EMPTY]");
  }

  @Test
  void givenCustomContextImplementation_whenEquals_thenEqualIfToMapMatches() {
    ProblemContext defaultContext = ProblemContext.create().put("key", "value");
    ProblemContext customContext =
        new ProblemContext() {

          @Override
          public @Nullable String get(String key) {
            return "key".equals(key) ? "value" : null;
          }

          @Override
          public ProblemContext put(String key, @Nullable String value) {
            return this;
          }

          @Override
          public Map<String, String> toMap() {
            return Map.of("key", "value");
          }
        };

    assertThat(defaultContext).isEqualTo(customContext);
  }
}
