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

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
public abstract class AbstractProblem implements Problem, Serializable {

  private static final long serialVersionUID = 1L;

  private final URI type;
  private final String title;
  private final int status;
  private final String detail;
  private final URI instance;
  private final Map<String, Object> extensions;

  /**
   * Constructs a new {@code AbstractProblem} instance with the given details.
   *
   * @param type the URI that identifies the type of the problem; must not be {@code null}
   * @param title a short, human-readable summary of the problem
   * @param status the HTTP status code applicable to this problem
   * @param detail a human-readable explanation specific to this occurrence of the problem
   * @param instance a URI reference that identifies the specific occurrence of the problem
   * @param extensions a map of additional, application-specific properties to include in the
   *     problem; a defensive copy is made, so changes to the original map do not affect this
   *     instance
   */
  public AbstractProblem(
      URI type,
      String title,
      int status,
      String detail,
      URI instance,
      Map<String, Object> extensions) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.extensions = new HashMap<>(extensions);
  }

  /**
   * @return the URI identifying the type of this problem
   */
  @Override
  public URI getType() {
    return this.type;
  }

  /**
   * @return a short, human-readable title describing the problem
   */
  @Override
  public String getTitle() {
    return this.title;
  }

  /**
   * @return the HTTP status code generated for this problem
   */
  @Override
  public int getStatus() {
    return this.status;
  }

  /**
   * @return a detailed, human-readable explanation specific to this occurrence
   */
  @Override
  public String getDetail() {
    return this.detail;
  }

  /**
   * @return a URI identifying the specific occurrence of the problem
   */
  @Override
  public URI getInstance() {
    return this.instance;
  }

  /**
   * @return an unmodifiable set of custom extension keys present in this problem
   */
  @Override
  public Set<String> getExtensions() {
    return Collections.unmodifiableSet(extensions.keySet());
  }

  /**
   * Gets the value associated with a named extension.
   *
   * @param name the extension key
   * @return the value of the extension, or {@code null} if not present
   */
  @Override
  public Object getExtensionValue(String name) {
    return extensions.get(name);
  }

  /**
   * Checks whether a given extension key is present.
   *
   * @param extension the extension key
   * @return {@code true} if the extension exists, {@code false} otherwise
   */
  @Override
  public boolean hasExtension(String extension) {
    return extensions.containsKey(extension);
  }

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
  @Override
  public Map<String, Object> getExtensionMembers() {
    return Collections.unmodifiableMap(extensions);
  }

  /**
   * Converts this problem instance into a {@link Problem} builder, pre-populated with its values.
   * Useful for creating a modified copy.
   *
   * @return a builder with the current problem's values
   */
  @Override
  public ProblemBuilder toBuilder() {
    return new ProblemBuilderImpl(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Problem)) {
      return false;
    }
    Problem problem = (Problem) obj;
    return Objects.equals(getType(), problem.getType())
        && Objects.equals(getTitle(), problem.getTitle())
        && getStatus() == problem.getStatus()
        && Objects.equals(getDetail(), problem.getDetail())
        && Objects.equals(getInstance(), problem.getInstance())
        && Objects.equals(getExtensionMembers(), problem.getExtensionMembers());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getType(), getTitle(), getStatus(), getDetail(), getInstance(), getExtensionMembers());
  }

  @Override
  public String toString() {
    List<String> lines = new ArrayList<>();
    if (getType() != null) {
      lines.add("\"type\" : \"" + quote(getType().toString()) + "\"");
    }
    if (getTitle() != null) {
      lines.add("\"title\" : \"" + quote(getTitle()) + "\"");
    }
    lines.add("\"status\" : " + getStatus());
    if (getDetail() != null) {
      lines.add("\"detail\" : \"" + quote(getDetail()) + "\"");
    }
    if (getInstance() != null) {
      lines.add("\"instance\" : \"" + quote(getInstance().toString()) + "\"");
    }

    getExtensionMembers()
        .forEach(
            (field, value) -> {
              if (value == null) {
                return;
              }

              if (value instanceof String) {
                lines.add("\"" + field + "\" : \"" + quote((String) value) + "\"");
              } else if (value instanceof Number || value instanceof Boolean) {
                lines.add("\"" + field + "\" : " + value);
              } else {
                lines.add(getObjectLine(field, value));
              }
            });

    return lines.stream().collect(Collectors.joining(", ", "{ ", " }"));
  }

  private static String quote(String string) {
    return JsonEscape.escape(string);
  }

  private String getObjectLine(String field, Object value) {
    String className = value.getClass().getSimpleName();
    return "\"" + field + "\" : \"" + className + ":" + quote(value.toString()) + "\"";
  }

  public abstract static class AbstractExtension implements Extension, Serializable {

    private static final long serialVersionUID = 1L;

    private final String key;
    private Object value;

    public AbstractExtension(String key, Object value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public Object setValue(Object value) {
      this.value = value;
      return value;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Extension)) {
        return false;
      }
      Extension extension = (Extension) obj;
      return Objects.equals(getKey(), extension.getKey())
          && Objects.equals(getValue(), extension.getValue());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getKey(), getValue());
    }

    @Override
    public String toString() {
      String valueLine;
      if (getValue() instanceof Number || getValue() instanceof Boolean) {
        valueLine = "\"value\" : " + getValue();
      } else {
        valueLine = "\"value\" : " + "\"" + quote(getValue().toString()) + "\"";
      }
      return "{ \"key\" : \"" + quote(getKey()) + "\", " + valueLine + " }";
    }
  }
}
