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
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class DefaultProblemBuilder implements ProblemBuilder, Serializable {

  private static final long serialVersionUID = 1L;

  private final StatusTitleResolver titleResolver;

  private @Nullable URI type = null;
  private @Nullable String title = null;
  private int status = 0;
  private @Nullable String detail = null;
  private @Nullable URI instance = null;
  private final Map<String, Object> extensions = new HashMap<>();

  DefaultProblemBuilder() {
    this(StatusTitleSupport.getResolver());
  }

  DefaultProblemBuilder(Problem problem) {
    this(StatusTitleSupport.getResolver());
    this.type = problem.getType();
    this.title = problem.getTitle();
    this.status = problem.getStatus();
    this.detail = problem.getDetail();
    this.instance = problem.getInstance();
    this.extensions.putAll(problem.getExtensions());
  }

  DefaultProblemBuilder(StatusTitleResolver titleResolver) {
    this.titleResolver = titleResolver;
  }

  @Override
  public ProblemBuilder type(@Nullable URI type) {
    this.type = type;
    return this;
  }

  @Override
  public ProblemBuilder title(@Nullable String title) {
    this.title = title;
    return this;
  }

  @Override
  public ProblemBuilder status(int status) {
    this.status = status;
    return this;
  }

  @Override
  public ProblemBuilder detail(@Nullable String detail) {
    this.detail = detail;
    return this;
  }

  @Override
  public ProblemBuilder instance(@Nullable URI instance) {
    this.instance = instance;
    return this;
  }

  @Override
  public ProblemBuilder extension(String name, @Nullable Object value) {
    if (value != null) {
      extensions.put(name, value);
    } else {
      extensions.remove(name);
    }
    return this;
  }

  @Override
  public Problem build() {
    URI type = this.type;
    if (type == null || isTypeBlank(type)) {
      type = Problem.BLANK_TYPE;
    }
    String title = this.title;
    if (title == null) {
      title = titleResolver.resolve(status).orElse(Problem.UNKNOWN_TITLE);
    }
    return new DefaultProblem(type, title, status, detail, instance, extensions);
  }

  @Override
  public String toString() {
    List<String> entries = new ArrayList<>();
    if (type != null && !isTypeBlank(type)) {
      entries.add("type=" + type);
    }
    if (title != null) {
      entries.add("title=" + title);
    }
    entries.add("status=" + status);
    if (detail != null) {
      entries.add("detail=" + detail);
    }
    if (instance != null) {
      entries.add("instance=" + instance);
    }
    extensions.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> entries.add(entry.getKey() + "=" + entry.getValue()));
    return "ProblemBuilder[" + String.join(", ", entries) + "]";
  }

  private boolean isTypeBlank(URI type) {
    return type.equals(Problem.BLANK_TYPE) || type.toString().isEmpty();
  }
}
