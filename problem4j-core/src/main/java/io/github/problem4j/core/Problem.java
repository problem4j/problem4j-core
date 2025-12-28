/*
 * Copyright (c) 2025 Damian Malczewski
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
 * SPDX-License-Identifier: MIT
 */
package io.github.problem4j.core;

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * Represents a problem detail according to the <a href="https://tools.ietf.org/html/rfc7807">RFC
 * 7807</a> (aka. <a href="https://tools.ietf.org/html/rfc9457">RFC 9457</a>) specification.
 *
 * <p>Instances of {@link Problem} are intended to be immutable. They provide standard fields such
 * as:
 *
 * <ul>
 *   <li>{@code type} – a URI identifying the type of the problem
 *   <li>{@code title} – a short, human-readable summary of the problem
 *   <li>{@code status} – the HTTP status code generated for this problem
 *   <li>{@code detail} – a human-readable explanation specific to this occurrence
 *   <li>{@code instance} – a URI identifying the specific occurrence of the problem
 * </ul>
 *
 * In addition, custom extensions can be added to provide extra context.
 */
public interface Problem {

  /** Default type URI for generic problems. */
  URI BLANK_TYPE = URI.create("about:blank");

  /** MIME content type for this problem. */
  String CONTENT_TYPE = "application/problem+json";

  /**
   * Creates a new builder for constructing {@link Problem} instances.
   *
   * @return a new {@link ProblemBuilder}
   */
  static ProblemBuilder builder() {
    return new ProblemBuilderImpl();
  }

  /**
   * Creates a named extension for use in a {@link Problem}.
   *
   * @param key the extension key, must not be {@code null}
   * @param value the extension value
   * @return a new {@link Problem.Extension} instance
   * @throws IllegalArgumentException if the {@code key} is {@code null}
   */
  static Extension extension(String key, Object value) {
    if (key == null) {
      throw new IllegalArgumentException("key cannot be null");
    }
    return new ProblemImpl.ExtensionImpl(key, value);
  }

  /**
   * @return the URI identifying the type of this problem
   */
  URI getType();

  /**
   * @return a short, human-readable title describing the problem
   */
  String getTitle();

  /**
   * @return the HTTP status code generated for this problem
   */
  int getStatus();

  /**
   * @return a detailed, human-readable explanation specific to this occurrence
   */
  String getDetail();

  /**
   * @return a URI identifying the specific occurrence of the problem
   */
  URI getInstance();

  /**
   * @return an unmodifiable set of custom extension keys present in this problem
   */
  Set<String> getExtensions();

  /**
   * Gets the value associated with a named extension.
   *
   * @param name the extension key
   * @return the value of the extension, or {@code null} if not present
   */
  Object getExtensionValue(String name);

  /**
   * Checks whether a given extension key is present.
   *
   * @param extension the extension key
   * @return {@code true} if the extension exists, {@code false} otherwise
   */
  boolean hasExtension(String extension);

  /**
   * Returns a map of all extension members.
   *
   * <p>This method provides a complete view of all extension key–value pairs, which is useful when
   * callers need to handle extensions in bulk rather than querying them individually. It
   * complements {@link #getExtensionValue(String)} and {@link #hasExtension(String)} by exposing
   * the full extension payload at once.
   *
   * @return an unmodifiable map of extension members
   */
  Map<String, Object> getExtensionMembers();

  /**
   * Converts this problem instance into a {@link Problem} builder, pre-populated with its values.
   * Useful for creating a modified copy.
   *
   * @return a builder with the current problem's values
   */
  ProblemBuilder toBuilder();

  /**
   * A convenience method to verify if {@code type} field was assigned, as {@link #BLANK_TYPE} also
   * mean that type is unassigned.
   *
   * @return {@code true} if {@code type} is assigned to a non-blank value, {@code false} otherwise
   */
  default boolean isTypeNonBlank() {
    return getType() != null && !getType().equals(BLANK_TYPE) && !getType().toString().isEmpty();
  }

  /** Represents a single key-value extension in a {@link Problem}. */
  interface Extension extends Map.Entry<String, Object> {

    /**
     * @return the extension key
     */
    @Override
    String getKey();

    /**
     * @return the extension value
     */
    @Override
    Object getValue();

    /**
     * Sets the extension value.
     *
     * @param value new value
     * @return the new value
     */
    @Override
    Object setValue(Object value);
  }
}
