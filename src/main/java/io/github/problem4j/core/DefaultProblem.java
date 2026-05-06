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

import static java.util.Collections.unmodifiableMap;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class DefaultProblem implements Problem, Serializable {

  private static final long serialVersionUID = 1L;

  private final URI type;
  private final String title;
  private final int status;
  private final @Nullable String detail;
  private final @Nullable URI instance;
  private final Map<String, Object> extensions;

  DefaultProblem(
      URI type,
      String title,
      int status,
      @Nullable String detail,
      @Nullable URI instance,
      @Nullable Map<String, ? extends Object> extensions) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.extensions = extensions != null ? new HashMap<>(extensions) : new HashMap<>();
  }

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
  public @Nullable String getDetail() {
    return detail;
  }

  @Override
  public @Nullable URI getInstance() {
    return instance;
  }

  @Override
  public Map<String, Object> getExtensions() {
    return unmodifiableMap(extensions);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Problem)) {
      return false;
    }
    return ProblemSupport.equals(this, (Problem) obj);
  }

  @Override
  public int hashCode() {
    return ProblemSupport.hashCode(this);
  }

  @Override
  public String toString() {
    return ProblemSupport.toString("Problem", this);
  }

  static final class DefaultExtension implements Extension {

    private final String name;
    private final @Nullable Object value;

    DefaultExtension(String name, @Nullable Object value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public @Nullable Object getValue() {
      return value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Problem.Extension)) {
        return false;
      }
      return ProblemSupport.equals(this, (Problem.Extension) obj);
    }

    @Override
    public int hashCode() {
      return ProblemSupport.hashCode(this);
    }

    @Override
    public String toString() {
      return ProblemSupport.toString("Extension", this);
    }
  }
}
