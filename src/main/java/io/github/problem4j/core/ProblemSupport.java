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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class providing canonical implementations of various operations for {@link Problem},
 * {@link Problem.Extension}, and {@link ProblemContext}.
 *
 * <p>Custom implementations of these interfaces should delegate to the corresponding methods in
 * this class to satisfy their well-known contracts.
 *
 * @since 2.0.0
 */
public final class ProblemSupport {

  /**
   * Returns {@code true} if {@code problem} equals {@code other} by comparing all standard fields:
   * type, title, status, detail, instance, and extensions.
   *
   * @param problem the problem to compare
   * @param other the other problem to compare against
   * @return {@code true} if all fields are equal
   * @since 2.0.0
   */
  public static boolean equals(Problem problem, Problem other) {
    return Objects.equals(problem.getType(), other.getType())
        && Objects.equals(problem.getTitle(), other.getTitle())
        && problem.getStatus() == other.getStatus()
        && Objects.equals(problem.getDetail(), other.getDetail())
        && Objects.equals(problem.getInstance(), other.getInstance())
        && Objects.equals(problem.getExtensions(), other.getExtensions());
  }

  /**
   * Computes a hash code for {@code problem} from all its standard fields.
   *
   * @param problem the problem to hash
   * @return the hash code
   * @since 2.0.0
   */
  public static int hashCode(Problem problem) {
    return Objects.hash(
        problem.getType(),
        problem.getTitle(),
        problem.getStatus(),
        problem.getDetail(),
        problem.getInstance(),
        problem.getExtensions());
  }

  /**
   * Returns a string representation of {@code problem}, including all non-null fields. Extensions
   * are appended in sorted key order.
   *
   * @param problem the problem to represent
   * @return a string in the form {@code Problem[field=value, ...]}
   * @since 2.0.0
   */
  public static String toString(Problem problem) {
    return toString(problem.getClass().getSimpleName(), problem);
  }

  /**
   * Returns a string representation of {@code problem} using the given class name label, including
   * all non-null fields. Extensions are appended in sorted key order.
   *
   * @param label the class name label to use as prefix
   * @param problem the problem to represent
   * @return a string in the form {@code label[field=value, ...]}
   * @since 2.0.0
   */
  public static String toString(String label, Problem problem) {
    List<String> entries = new ArrayList<>();
    if (problem.isTypeNonBlank()) {
      entries.add("type=" + problem.getType());
    }
    entries.add("title=" + problem.getTitle());
    entries.add("status=" + problem.getStatus());
    if (problem.getDetail() != null) {
      entries.add("detail=" + problem.getDetail());
    }
    if (problem.getInstance() != null) {
      entries.add("instance=" + problem.getInstance());
    }
    problem.getExtensions().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> entries.add(entry.getKey() + "=" + entry.getValue()));
    return label + "[" + String.join(", ", entries) + "]";
  }

  /**
   * Returns {@code true} if {@code extension} equals {@code other} by comparing name and value.
   *
   * @param extension the extension to compare
   * @param other the other extension to compare against
   * @return {@code true} if name and value are equal
   * @since 2.0.0
   */
  public static boolean equals(Problem.Extension extension, Problem.Extension other) {
    return Objects.equals(extension.getName(), other.getName())
        && Objects.equals(extension.getValue(), other.getValue());
  }

  /**
   * Computes a hash code for {@code extension} from its name and value.
   *
   * @param extension the extension to hash
   * @return the hash code
   * @since 2.0.0
   */
  public static int hashCode(Problem.Extension extension) {
    return Objects.hash(extension.getName(), extension.getValue());
  }

  /**
   * Returns a string representation of {@code extension}.
   *
   * @param extension the extension to represent
   * @return a string in the form {@code Extension[name=value]}
   * @since 2.0.0
   */
  public static String toString(Problem.Extension extension) {
    return toString(extension.getClass().getSimpleName(), extension);
  }

  /**
   * Returns a string representation of {@code extension} using the given class name label.
   *
   * @param label the class name label to use as prefix
   * @param extension the extension to represent
   * @return a string in the form {@code label[name=value]}
   * @since 2.0.0
   */
  public static String toString(String label, Problem.Extension extension) {
    return label + "[" + extension.getName() + "=" + extension.getValue() + "]";
  }

  /**
   * Returns {@code true} if {@code context} equals {@code other} by comparing their {@link
   * ProblemContext#toMap() toMap()} snapshots.
   *
   * @param context the context to compare
   * @param other the other context to compare against
   * @return {@code true} if the map snapshots are equal
   * @since 2.0.0
   */
  public static boolean equals(ProblemContext context, ProblemContext other) {
    return Objects.equals(context.toMap(), other.toMap());
  }

  /**
   * Computes a hash code for {@code context} from its {@link ProblemContext#toMap() toMap()}
   * snapshot.
   *
   * @param context the context to hash
   * @return the hash code
   * @since 2.0.0
   */
  public static int hashCode(ProblemContext context) {
    return Objects.hash(context.toMap());
  }

  /**
   * Returns a string representation of {@code context}. Entries are sorted by key.
   *
   * @param context the context to represent
   * @return {@code "ProblemContext[EMPTY]"} if empty, otherwise {@code "ProblemContext[key=value,
   *     ...]"}
   * @since 2.0.0
   */
  public static String toString(ProblemContext context) {
    return toString(context.getClass().getSimpleName(), context);
  }

  /**
   * Returns a string representation of {@code context} using the given class name label. Entries
   * are sorted by key.
   *
   * @param label the class name label to use as prefix
   * @param context the context to represent
   * @return {@code "label[EMPTY]"} if empty, otherwise {@code "label[key=value, ...]"}
   * @since 2.0.0
   */
  public static String toString(String label, ProblemContext context) {
    if (context.toMap().isEmpty()) {
      return label + "[EMPTY]";
    }
    List<String> entries = new ArrayList<>();
    context.toMap().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> entries.add(entry.getKey() + "=" + entry.getValue()));
    return label + "[" + String.join(", ", entries) + "]";
  }

  /**
   * Returns {@code true} if {@code type} is considered blank, meaning it equals {@link
   * Problem#BLANK_TYPE} or its string representation is empty.
   *
   * @param type the URI to check
   * @return {@code true} if the type is blank, {@code false} otherwise
   * @since 2.0.0
   */
  public static boolean isTypeBlank(URI type) {
    return type.equals(Problem.BLANK_TYPE) || type.toString().isEmpty();
  }

  /**
   * Returns {@code true} if {@code type} is considered non-blank, meaning it does not equal {@link
   * Problem#BLANK_TYPE} and its string representation is not empty.
   *
   * @param type the URI to check
   * @return {@code true} if the type is non-blank, {@code false} otherwise
   * @since 2.0.0
   */
  public static boolean isTypeNonBlank(URI type) {
    return !type.equals(Problem.BLANK_TYPE) && !type.toString().isEmpty();
  }

  private ProblemSupport() {}
}
