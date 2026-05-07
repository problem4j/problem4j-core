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

import java.util.Optional;

/**
 * Resolves a human-readable title for an HTTP status code.
 *
 * <p>Implementations are discovered via {@link java.util.ServiceLoader}. When multiple
 * implementations are present, the one with the lowest {@link #getPriority()} value takes
 * precedence. If none are registered, a built-in resolver provides titles for HTTP status codes
 * well-known at the time of library release.
 *
 * @since 2.0.0
 */
public interface StatusTitleResolver {

  /**
   * Returns the title for the given HTTP status code, or an empty {@code Optional} if this resolver
   * does not recognize it.
   *
   * @param status the HTTP status code
   * @return the resolved title, or empty if not recognized
   * @since 2.0.0
   */
  Optional<String> resolve(int status);

  /**
   * Priority of this resolver - lower values take precedence over higher ones. Defaults to {@code
   * 0}.
   *
   * @return the priority of this resolver
   * @since 2.0.0
   */
  default int getPriority() {
    return 0;
  }
}
