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

import static java.util.Comparator.comparingInt;
import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

final class StatusTitleSupport {

  private static final StatusTitleResolver RESOLVER = loadResolver();

  static StatusTitleResolver getResolver() {
    return RESOLVER;
  }

  static StatusTitleResolver loadResolver() {
    return stream(load(StatusTitleResolver.class).spliterator(), false)
        .min(comparingInt(StatusTitleResolver::getPriority))
        .orElseGet(DefaultStatusTitleResolver::new);
  }

  private StatusTitleSupport() {}
}
