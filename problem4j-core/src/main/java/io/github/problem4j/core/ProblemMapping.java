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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to map a {@link Throwable} to an <a href="https://tools.ietf.org/html/rfc7807">RFC
 * 7807</a> {@link Problem} (aka. <a href="https://tools.ietf.org/html/rfc9457">RFC 9457</a>)
 * specification.
 *
 * <p>This annotation allows you to declaratively specify how a specific exception should be
 * converted into a {@link Problem} response. All string values support dynamic interpolation of
 * placeholders based on the exception's fields and context.
 *
 * <h3>Interpolation</h3>
 *
 * <ul>
 *   <li>{@code {message}} -&gt; the exception's {@link Throwable#getMessage()}
 *   <li>{@code {context.*}} -&gt; value from {@link ProblemContext} included in evaluation via
 *       {@link ProblemMapper}
 *   <li>{@code {fieldName}} -&gt; value of any field (private or public) in the exception class
 *       hierarchy
 *   <li>Any placeholder that resolves to null or an empty string is ignored in the final output
 * </ul>
 *
 * <h3>Inheritance</h3>
 *
 * <p>This annotation is {@link Inherited}, so subclasses of an annotated exception automatically
 * inherit the mapping unless explicitly overridden with a new annotation.
 *
 * <h3>Extensions</h3>
 *
 * <p>Use {@link #extensions()} to expose additional fields as {@link Problem} extensions. Each name
 * is resolved using the same rules as placeholders (fields in the class hierarchy). Null or empty
 * values are automatically omitted from the final {@link Problem}.
 *
 * <h3>Defaulting behavior</h3>
 *
 * <ul>
 *   <li>If {@link #type()} is empty, the processor may apply a default type (e.g., {@code
 *       about:blank}).
 *   <li>If {@link #title()} is empty, the processor may assign the standard HTTP reason phrase
 *       corresponding to the status code.
 *   <li>Status code {@code 0} is interpreted as "unspecified".
 * </ul>
 *
 * <h3>Example usage</h3>
 *
 * <pre>{@code @ProblemMapping(
 *     type = "https://example.org/errors/validation",
 *     title = "Validation Failed",
 *     status = 400,
 *     detail = "Invalid input for user {userId}, trace {context.traceId}",
 *     extensions = {"userId", "fieldName"}
 * )
 * public class ValidationException extends RuntimeException {
 *     private final String userId;
 *     private final String fieldName;
 *
 *     public ValidationException(String userId, String fieldName) {
 *         super("Validation failed for user " + userId);
 *         this.userId = userId;
 *         this.fieldName = fieldName;
 *     }
 * }}</pre>
 *
 * <p>This annotation provides a simple and consistent way to map exceptions to RFC 7807 Problems,
 * with support for dynamic data inclusion, null/empty-safe interpolation, and subclass inheritance.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProblemMapping {

  /**
   * Interpolated type URI for the problem.
   *
   * <p>Supports placeholders of the form {@code "{placeholderValue}"}:
   *
   * <ul>
   *   <li>{@code {message}} -&gt; exception message
   *   <li>{@code {context.*}} -&gt; value from {@link ProblemContext} included in problem
   *       evaluation via {@link ProblemMapper}
   *   <li>{@code {fieldName}} -&gt; value of any field in the exception class hierarchy
   * </ul>
   *
   * <p>If empty, a default type (e.g., {@code about:blank}) may be applied by the processor. Null
   * or empty placeholders are ignored.
   *
   * @return interpolated type URI for the problem
   */
  String type() default "";

  /**
   * Interpolated title of the problem.
   *
   * <p>Supports placeholders the same way as {@link #type()}.
   *
   * <p>If empty, the processor may assign the standard HTTP reason phrase corresponding to the
   * {@link #status()}. Null or empty placeholders are ignored.
   *
   * @return interpolated title of the problem
   */
  String title() default "";

  /**
   * Status for the problem.
   *
   * <p>{@code 0} means "unspecified"; in that case, the processor may apply a default. Used to
   * determine the response status and may influence default title assignment.
   *
   * @return status for the problem
   */
  int status() default 0;

  /**
   * Interpolated detailed description of the problem.
   *
   * <p>Supports placeholders the same way as {@link #type()}.
   *
   * <p>Null or empty placeholder values are ignored in the resulting string.
   *
   * @return interpolated detailed description of the problem
   */
  String detail() default "";

  /**
   * Interpolated instance URI identifying this occurrence of the problem.
   *
   * <p>Supports placeholders the same way as {@link #type()}.
   *
   * <p>If invalid URI or placeholder resolves to null/empty, the processor ignores it. Useful for
   * linking to logs or trace-specific URLs.
   *
   * @return interpolated instance URI identifying this occurrence of the problem
   */
  String instance() default "";

  /**
   * Names of fields in the exception class to expose as problem extensions.
   *
   * <p>Each name is resolved using the same rules as placeholders. Null or empty values are
   * omitted.
   *
   * <p>This allows exposing additional context-specific data for clients, beyond the standard RFC
   * 7807 fields.
   *
   * @return names of fields in the exception class to expose as problem extensions
   */
  String[] extensions() default {};
}
