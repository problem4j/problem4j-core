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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.problem4j.core;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This processor supports dynamic interpolation of placeholders in the annotation values. The
 * algorithm is as follows:
 *
 * <ol>
 *   <li>Check if the exception class has {@link ProblemMapping}; if not, return null.
 *   <li>Create a {@link ProblemBuilder} to accumulate the problem details.
 *   <li>For each standard field ({@code type}, {@code title}, {@code status}, {@code detail},
 *       {@code instance}):
 *       <ol>
 *         <li>Read the raw annotation value.
 *         <li>Interpolate placeholders of the form {@code "{placeholderValue}"}:
 *             <ul>
 *               <li>{@code {message}} -&gt; {@link Throwable#getMessage()}
 *               <li>{@code {context.*}} -&gt; {@link ProblemContext#get(String)}
 *               <li>{@code {fieldName}} -&gt; any field in the exception class hierarchy
 *             </ul>
 *         <li>Ignore placeholders that resolve to null or empty string.
 *         <li>Assign the interpolated value to the {@link ProblemBuilder}, ignoring invalid URIs
 *             for {@code type} and {@code instance}.
 *       </ol>
 *   <li>For extensions:
 *       <ol>
 *         <li>For each field name listed in {@link ProblemMapping#extensions()}:
 *             <ul>
 *               <li>Resolve the value using the same rules as above.
 *               <li>Ignore null or empty values.
 *             </ul>
 *       </ol>
 *   <li>Build and return the {@link ProblemBuilder}.
 * </ol>
 *
 * <p>This design allows dynamic, context-aware {@link Problem} generation, supports subclass
 * inheritance, and ensures that null or empty values do not appear in the output, making Problems
 * concise and meaningful.
 */
public abstract class AbstractProblemMapper implements ProblemMapper {

  protected static final Pattern PLACEHOLDER = Pattern.compile("\\{([^}]+)}");

  protected static final String MESSAGE_LABEL = "message";
  protected static final String CONTEXT_LABEL_PREFIX = "context.";

  /** Creates a new instance of problem mapper. */
  public AbstractProblemMapper() {}

  /**
   * Convert {@link Throwable} -&gt; {@link ProblemBuilder} according to its {@link ProblemMapping}
   * annotation. Such builder can be further extended or executed to create {@link Problem}
   * response.
   *
   * @param t {@link Throwable} to convert (may be {@code null})
   * @return a {@link ProblemBuilder} instance
   * @throws ProblemMappingException when something goes wrong while building the Problem
   */
  @Override
  public final ProblemBuilder toProblemBuilder(Throwable t) {
    return toProblemBuilder(t, null);
  }

  /**
   * Convert {@link Throwable} -&gt; {@link ProblemBuilder} according to its {@link ProblemMapping}
   * annotation.
   *
   * @param t {@link Throwable} to convert (may be {@code null})
   * @param context optional {@link ProblemContext} (may be {@code null})
   * @return a {@link ProblemBuilder} instance
   * @throws ProblemMappingException when something goes wrong while building the Problem
   */
  @Override
  public ProblemBuilder toProblemBuilder(Throwable t, ProblemContext context) {
    if (t == null) {
      return Problem.builder();
    }
    ProblemMapping mapping = findAnnotation(t.getClass());
    if (mapping == null) {
      return Problem.builder();
    }

    ProblemBuilder builder = Problem.builder();

    try {
      applyTypeOnBuilder(builder, mapping, t, context);
      applyTitleOnBuilder(builder, mapping, t, context);
      applyStatusOnBuilder(mapping, builder);
      applyDetailOnBuilder(builder, mapping, t, context);
      applyInstanceOnBuilder(builder, mapping, t, context);
      applyExtensionsOnBuilder(builder, mapping, t);
      return builder;
    } catch (ProblemMappingException e) {
      // explicit rethrow so next clause doesn't have ProblemProcessingException as a cause
      throw e;
    } catch (Exception e) {
      throw new ProblemMappingException(
          "Unexpected failure while processing @ProblemMapping of " + t.getClass().getName(), e);
    }
  }

  /**
   * Checks whether the given exception class is annotated with {@link ProblemMapping}.
   *
   * @param t {@link Throwable} to check (may be {@code null})
   * @return {@code true} if the exception class has a {@link ProblemMapping} annotation, {@code
   *     false} otherwise
   */
  @Override
  public boolean isMappingCandidate(Throwable t) {
    return t != null && t.getClass().isAnnotationPresent(ProblemMapping.class);
  }

  /**
   * Returns the {@link ProblemMapping} annotation from the class if present, otherwise null.
   *
   * @param clazz the class to inspect
   * @return the {@link ProblemMapping} annotation if present, otherwise null
   */
  protected ProblemMapping findAnnotation(Class<?> clazz) {
    return clazz.getAnnotation(ProblemMapping.class);
  }

  /**
   * Applies the "type" value from {@link ProblemMapping#type()} after placeholder interpolation;
   * ignores invalid URIs.
   *
   * @param builder the {@link ProblemBuilder} to add the type to
   * @param mapping the {@link ProblemMapping} annotation containing the type value
   * @param t the {@link Throwable} to extract values from
   * @param context the problem context for additional data
   */
  protected void applyTypeOnBuilder(
      ProblemBuilder builder, ProblemMapping mapping, Throwable t, ProblemContext context) {
    String rawType =
        mapping.type() != null && !mapping.type().isEmpty() ? mapping.type().trim() : "";
    if (!rawType.isEmpty()) {
      String typeInterpolated = interpolate(rawType, t, context);
      if (!typeInterpolated.isEmpty()) {
        try {
          builder.type(typeInterpolated);
        } catch (IllegalArgumentException e) {
          // ignored - if type is invalid let not fail
        }
      }
    }
  }

  /**
   * Applies the interpolated title if present and non-empty.
   *
   * @param builder the {@link ProblemBuilder} to add the title to
   * @param mapping the {@link ProblemMapping} annotation containing the title value
   * @param t the {@link Throwable} to extract values from
   * @param context the problem context for additional data
   */
  protected void applyTitleOnBuilder(
      ProblemBuilder builder, ProblemMapping mapping, Throwable t, ProblemContext context) {
    String titleRaw =
        mapping.title() != null && !mapping.title().isEmpty() ? mapping.title().trim() : "";
    if (!titleRaw.isEmpty()) {
      String titleInterpolated = interpolate(titleRaw, t, context);
      if (!titleInterpolated.isEmpty()) {
        builder.title(titleInterpolated);
      }
    }
  }

  /**
   * Sets the status when greater than zero.
   *
   * @param mapping the {@link ProblemMapping} annotation containing the status value
   * @param builder the {@link ProblemBuilder} to set the status on
   */
  protected void applyStatusOnBuilder(ProblemMapping mapping, ProblemBuilder builder) {
    if (mapping.status() > 0) {
      builder.status(mapping.status());
    }
  }

  /**
   * Applies the interpolated detail text if non-empty.
   *
   * @param builder the {@link ProblemBuilder} to add the detail to
   * @param mapping the {@link ProblemMapping} annotation containing the detail value
   * @param t the {@link Throwable} to extract values from
   * @param context the problem context for additional data
   */
  protected void applyDetailOnBuilder(
      ProblemBuilder builder, ProblemMapping mapping, Throwable t, ProblemContext context) {
    String detailRaw =
        mapping.detail() != null && !mapping.detail().isEmpty() ? mapping.detail().trim() : "";
    if (!detailRaw.isEmpty()) {
      String detailInterpolated = interpolate(detailRaw, t, context);
      if (!detailInterpolated.isEmpty()) {
        builder.detail(detailInterpolated);
      }
    }
  }

  /**
   * Applies the interpolated instance value; ignores invalid URIs.
   *
   * @param builder the {@link ProblemBuilder} to add the instance to
   * @param mapping the {@link ProblemMapping} annotation containing the instance value
   * @param t the {@link Throwable} to extract values from
   * @param context the problem context for additional data
   */
  protected void applyInstanceOnBuilder(
      ProblemBuilder builder, ProblemMapping mapping, Throwable t, ProblemContext context) {
    String rawInstance =
        mapping.instance() != null && !mapping.instance().isEmpty()
            ? mapping.instance().trim()
            : "";
    if (!rawInstance.isEmpty()) {
      String instanceInterpolated = interpolate(rawInstance, t, context);
      if (!instanceInterpolated.isEmpty()) {
        try {
          builder.instance(instanceInterpolated);
        } catch (IllegalArgumentException e) {
          // ignored - if type is invalid let not fail
        }
      }
    }
  }

  /**
   * Adds extension fields resolved from {@link ProblemMapping#extensions()}.
   *
   * @param builder the {@link ProblemBuilder} to add extensions to
   * @param mapping the {@link ProblemMapping} annotation containing extension field names
   * @param t the {@link Throwable} to extract extension values from
   */
  protected void applyExtensionsOnBuilder(
      ProblemBuilder builder, ProblemMapping mapping, Throwable t) {
    String[] extensions = mapping.extensions();
    for (String name : extensions) {
      if (name == null || name.trim().isEmpty()) {
        continue;
      }
      name = name.trim();
      Object value = resolvePlaceholderSource(t, name);
      if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
        builder.extension(name, value);
      }
    }
  }

  /**
   * Interpolates placeholders of the form {@code "{placeholderValue}"}. Supported keys:
   *
   * <ul>
   *   <li>{@code message} - throwable message
   *   <li>{@code {context.*}} -&gt; value from {@link ProblemContext} included in evaluation via
   *       {@link ProblemMapper}
   *   <li>Any other token - value of a matching field in the throwable class hierarchy
   * </ul>
   *
   * <p>Missing values resolve to an empty string.
   *
   * @param template the template string containing placeholders
   * @param t the throwable to extract values from
   * @param context the problem context for additional data
   * @return the interpolated string with placeholders replaced by actual values
   */
  protected String interpolate(String template, Throwable t, ProblemContext context) {
    Matcher m = PLACEHOLDER.matcher(template);
    StringBuilder sb = new StringBuilder();

    int lastEnd = 0;
    while (m.find()) {
      sb.append(template, lastEnd, m.start());

      String key = m.group(1);
      String replacement;

      if (MESSAGE_LABEL.equals(key)) {
        replacement = t.getMessage() == null ? "" : String.valueOf(t.getMessage());
      } else if (key.startsWith(CONTEXT_LABEL_PREFIX)) {
        String contextKey = key.substring(CONTEXT_LABEL_PREFIX.length());
        replacement =
            (context == null || !context.containsKey(contextKey)) ? "" : context.get(contextKey);
      } else {
        Object v = resolvePlaceholderSource(t, key);
        replacement = v == null ? "" : String.valueOf(v);
      }

      sb.append(replacement);
      lastEnd = m.end();
    }

    sb.append(template, lastEnd, template.length()); // append the tail
    return sb.toString();
  }

  /**
   * Resolves a placeholder by reflective field lookup up the throwable class hierarchy.
   *
   * @param t the throwable to inspect
   * @param name the field name to look for
   * @return the field value if found, otherwise null
   */
  protected Object resolvePlaceholderSource(Throwable t, String name) {
    if (name == null || name.isEmpty()) {
      return null;
    }
    Class<?> search = t.getClass();
    while (search != null && search != Object.class) {
      try {
        Field f = search.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(t);
      } catch (NoSuchFieldException ignored) {
        // ignored, loop will go to parent class to see if that field exists there
      } catch (Exception ignored) {
        return null;
      }
      search = search.getSuperclass();
    }
    return null;
  }
}
