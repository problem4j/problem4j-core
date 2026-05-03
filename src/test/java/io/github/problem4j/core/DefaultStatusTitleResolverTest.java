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

import java.util.Optional;
import org.junit.jupiter.api.Test;

class DefaultStatusTitleResolverTest {

  @Test
  void givenDefaultStatusTitleResolver_whenGetPriority_thenReturnsMaxValue() {
    StatusTitleResolver resolver = new DefaultStatusTitleResolver();

    assertThat(resolver.getPriority()).isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  void givenDefaultStatusTitleResolver_whenResolve_thenReturnsOptional() {
    StatusTitleResolver resolver = new DefaultStatusTitleResolver();

    Optional<String> title = resolver.resolve(200);

    assertThat(title).isPresent().contains("OK");
  }

  @Test
  void givenDefaultStatusTitleResolver_whenResolveUnknownStatus_thenReturnsEmpty() {
    StatusTitleResolver resolver = new DefaultStatusTitleResolver();

    Optional<String> title = resolver.resolve(999);

    assertThat(title).isEmpty();
  }

  @Test
  void givenResolver_whenSerialized_thenDeserializedResolvesTheSame() throws Exception {
    StatusTitleResolver original = new DefaultStatusTitleResolver();

    StatusTitleResolver deserialized = Serialization.roundTrip(original);

    assertThat(deserialized).isNotSameAs(original);
    assertThat(deserialized.resolve(404)).isEqualTo(original.resolve(404));
    assertThat(deserialized.resolve(999)).isEqualTo(original.resolve(999));
  }
}
