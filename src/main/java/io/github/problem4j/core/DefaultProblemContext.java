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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class DefaultProblemContext implements ProblemContext, Serializable {

  private static final long serialVersionUID = 1L;

  private final Map<String, String> context;

  DefaultProblemContext() {
    this.context = new HashMap<>();
  }

  @Override
  public @Nullable String get(String key) {
    return context.get(key);
  }

  @Override
  public ProblemContext put(String key, @Nullable String value) {
    if (value == null) {
      context.remove(key);
    } else {
      context.put(key, value);
    }
    return this;
  }

  @Override
  public Map<String, String> toMap() {
    return Collections.unmodifiableMap(new HashMap<>(context));
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ProblemContext)) {
      return false;
    }
    ProblemContext other = (ProblemContext) obj;
    return ProblemSupport.equals(this, other);
  }

  @Override
  public int hashCode() {
    return ProblemSupport.hashCode(this);
  }

  @Override
  public String toString() {
    return ProblemSupport.toString("ProblemContext", this);
  }
}
