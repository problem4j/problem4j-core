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

import static io.github.problem4j.core.DummyStatusTitleResolver.STATUS_NETWORK_READ_TIMEOUT;
import static io.github.problem4j.core.DummyStatusTitleResolver.STATUS_SITE_IS_FROZEN;
import static io.github.problem4j.core.DummyStatusTitleResolver.STATUS_THIS_IS_FINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class StatusTitleSupportTest {

  @Test
  void givenStatusCode_whenResolvingTitle_thenReturnsTitle() {
    assertThat(StatusTitleSupport.getResolver().resolve(200)).contains("OK");
  }

  @Test
  void givenUnknownStatusCode_whenResolvingTitle_thenReturnsEmpty() {
    assertThat(StatusTitleSupport.getResolver().resolve(999)).isEmpty();
  }

  @Test
  void givenStatusTitleResolver_whenCallGetPriority_thenReturnsDefault() {
    StatusTitleResolver resolver = status -> Optional.empty();

    assertThat(resolver.getPriority()).isZero();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  void givenNoRegisteredResolvers_whenLoadResolver_thenReturnsFallbackResolver() {
    ServiceLoader<StatusTitleResolver> loader = mock(ServiceLoader.class);
    when(loader.spliterator()).thenReturn(List.<StatusTitleResolver>of().spliterator());

    try (MockedStatic<ServiceLoader> mocked = mockStatic(ServiceLoader.class)) {
      mocked.when(() -> ServiceLoader.load(StatusTitleResolver.class)).thenReturn(loader);

      StatusTitleResolver resolver = StatusTitleSupport.loadResolver();

      assertThat(resolver).isInstanceOf(DefaultStatusTitleResolver.class);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  void givenSingleRegisteredResolver_whenLoadResolver_thenReturnsThatResolver() {
    ServiceLoader<StatusTitleResolver> loader = mock(ServiceLoader.class);
    StatusTitleResolver custom = mock(StatusTitleResolver.class);
    when(loader.spliterator()).thenReturn(List.of(custom).spliterator());

    try (MockedStatic<ServiceLoader> mocked = mockStatic(ServiceLoader.class)) {
      mocked.when(() -> ServiceLoader.load(StatusTitleResolver.class)).thenReturn(loader);

      StatusTitleResolver resolver = StatusTitleSupport.loadResolver();

      assertThat(resolver).isSameAs(custom);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  void givenMultipleRegisteredResolvers_whenLoadResolver_thenReturnsResolverWithLowestPriority() {
    ServiceLoader<StatusTitleResolver> loader = mock(ServiceLoader.class);

    StatusTitleResolver highPriority = mock(StatusTitleResolver.class);
    when(highPriority.getPriority()).thenReturn(-10);

    StatusTitleResolver lowPriority = mock(StatusTitleResolver.class);
    when(lowPriority.getPriority()).thenReturn(5);

    when(loader.spliterator()).thenReturn(List.of(lowPriority, highPriority).spliterator());

    try (MockedStatic<ServiceLoader> mocked = mockStatic(ServiceLoader.class)) {
      mocked.when(() -> ServiceLoader.load(StatusTitleResolver.class)).thenReturn(loader);

      StatusTitleResolver resolver = StatusTitleSupport.loadResolver();

      assertThat(resolver).isSameAs(highPriority);
    }
  }

  @Test
  void givenCustomStatusCode_whenResolvingTitle_thenReturnsCustomTitle() {
    assertThat(StatusTitleSupport.getResolver().resolve(STATUS_THIS_IS_FINE))
        .contains("This is Fine");
    assertThat(StatusTitleSupport.getResolver().resolve(STATUS_SITE_IS_FROZEN))
        .contains("Site is Frozen");
    assertThat(StatusTitleSupport.getResolver().resolve(STATUS_NETWORK_READ_TIMEOUT))
        .contains("Network Read Timeout Error");
  }

  @Test
  void givenStandardStatusCode_whenCustomResolverRegistered_thenStandardTitleStillReturned() {
    assertThat(StatusTitleSupport.getResolver().resolve(200)).contains("OK");
    assertThat(StatusTitleSupport.getResolver().resolve(404)).contains("Not Found");
    assertThat(StatusTitleSupport.getResolver().resolve(500)).contains("Internal Server Error");
  }
}
