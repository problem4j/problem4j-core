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

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility enum for generic HTTP status codes, to be used with {@link ProblemBuilder} without having
 * to pass both {@link ProblemBuilder#title(String)} and {@link ProblemBuilder#status(int)}
 * separately, if creating {@link Problem} object representing standard HTTP error codes.
 *
 * <pre>{@code
 * // instead of calling two methods
 * Problem problem1 = Problem.builder()
 *     .title("Not Found")
 *     .status(404)
 *     .build();
 *
 * Problem problem2 = Problem.builder()
 *     .status(ProblemStatus.NOT_FOUND)
 *     .build();
 * }</pre>
 *
 * <p>This enum can be used to represent response status codes in a library-agnostic way, without
 * introducing dependencies on specific frameworks like Spring.
 *
 * <p><b>Referenced RFC sources (chronological)</b>:
 *
 * <ul>
 *   <li><b>1998-03</b> - RFC 2295: Transparent Content Negotiation in HTTP (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc2295">link</a>) - introduces 506 Variant
 *       Also Negotiates.
 *   <li><b>1998-04</b> - RFC 2324: Hyper Text Coffee Pot Control Protocol (HTCPCP/1.0) (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc2324">link</a>) - adds novelty code 418 I'm
 *       a teapot.
 *   <li><b>1999-02</b> - RFC 2518: HTTP Extensions for Distributed Authoring (WebDAV) (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc2518">link</a>) - defines 102 Processing,
 *       207 Multi-Status, 422 Unprocessable Entity, 423 Locked, 424 Failed Dependency.
 *   <li><b>1999-06</b> - RFC 2616: Hypertext Transfer Protocol -- HTTP/1.1 (Obsoleted) (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc2616">link</a>) - core set:
 *       100,101,200-206,300-305,400-417,500-505.
 *   <li><b>2000-02</b> - RFC 2774: An HTTP Extension Framework (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc2774">link</a>) - adds 510 Not Extended.
 *   <li><b>2002-01</b> - RFC 3229: Delta encoding in HTTP (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc3229">link</a>) - adds 226 IM Used.
 *   <li><b>2007-06</b> - RFC 4918: WebDAV (replacing RFC 2518) (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc4918">link</a>) - re-specifies
 *       102,207,422,423,424; adds 507 Insufficient Storage.
 *   <li><b>2010-05</b> - RFC 5842: Binding Extensions to WebDAV (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc5842">link</a>) - adds 208 Already Reported,
 *       508 Loop Detected.
 *   <li><b>2012-04</b> - RFC 6585: Additional HTTP Status Codes (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc6585">link</a>) - adds 428 Precondition
 *       Required, 429 Too Many Requests, 431 Request Header Fields Too Large, 511 Network
 *       Authentication Required.
 *   <li><b>2014-06</b> - RFC 7231: HTTP/1.1 Semantics and Content (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc7231">link</a>) - deprecates 305 Use Proxy;
 *       renames 413 Request Entity Too Large to Payload Too Large; 414 Request-URI Too Long to URI
 *       Too Long.
 *   <li><b>2015-04</b> - RFC 7538: Status Code 308 (Permanent Redirect) (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc7538">link</a>) - adds 308 Permanent
 *       Redirect.
 *   <li><b>2015-05</b> - RFC 7540: HTTP/2 (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc7540">link</a>) - adds 421 Misdirected
 *       Request.
 *   <li><b>2016-02</b> - RFC 7725: An HTTP Status Code to Report Legal Obstacles (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc7725">link</a>) - adds 451 Unavailable For
 *       Legal Reasons.
 *   <li><b>2018-01</b> - RFC 8297: Indicating Hints (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc8297">link</a>) - adds 103 Early Hints;
 *       obsoletes informal 103 Checkpoint name.
 *   <li><b>2018-09</b> - RFC 8470: Using Early Data in HTTP (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc8470">link</a>) - adds 425 Too Early.
 *   <li><b>2022-06</b> - RFC 9110: HTTP Semantics (<a
 *       href="https://datatracker.ietf.org/doc/html/rfc9110">link</a>) - renames 413 Payload Too
 *       Large to Content Too Large; 416 Requested Range Not Satisfiable to Range Not Satisfiable;
 *       422 Unprocessable Entity to Unprocessable Content; consolidates prior semantics.
 * </ul>
 *
 * @see <a href="https://http.cat/">HTTP Cats</a>
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status">HTTP response
 *     status codes - HTTP | MDN</a>
 */
public enum ProblemStatus {

  /**
   * 100 Continue.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.2.1">RFC 9110
   *     §15.2.1</a>
   */
  CONTINUE(ProblemStatus.CONTINUE_STATUS, ProblemStatus.CONTINUE_TITLE),

  /**
   * 101 Switching Protocols.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.2.2">RFC 9110
   *     §15.2.2</a>
   */
  SWITCHING_PROTOCOLS(
      ProblemStatus.SWITCHING_PROTOCOLS_STATUS, ProblemStatus.SWITCHING_PROTOCOLS_TITLE),

  /**
   * 102 Processing (WebDAV).
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2518#section-10.1">RFC 2518 §10.1</a>
   */
  PROCESSING(ProblemStatus.PROCESSING_STATUS, ProblemStatus.PROCESSING_TITLE),

  /**
   * 103 Early Hints.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc8297#section-2">RFC 8297 §2</a>
   */
  EARLY_HINTS(ProblemStatus.EARLY_HINTS_STATUS, ProblemStatus.EARLY_HINTS_TITLE),

  /**
   * 103 Checkpoint.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc8297#section-2">RFC 8297 §2</a>
   * @deprecated Renamed to {@link #EARLY_HINTS} by RFC 8297.
   */
  @Deprecated
  CHECKPOINT(ProblemStatus.CHECKPOINT_STATUS, ProblemStatus.CHECKPOINT_TITLE),

  /**
   * 200 OK.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.1">RFC 9110
   *     §15.3.1</a>
   */
  OK(ProblemStatus.OK_STATUS, ProblemStatus.OK_TITLE),

  /**
   * 201 Created.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.2">RFC 9110
   *     §15.3.2</a>
   */
  CREATED(ProblemStatus.CREATED_STATUS, ProblemStatus.CREATED_TITLE),

  /**
   * 202 Accepted.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.3">RFC 9110
   *     §15.3.3</a>
   */
  ACCEPTED(ProblemStatus.ACCEPTED_STATUS, ProblemStatus.ACCEPTED_TITLE),

  /**
   * 203 Non-Authoritative Information.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.4">RFC 9110
   *     §15.3.4</a>
   */
  NON_AUTHORITATIVE_INFORMATION(
      ProblemStatus.NON_AUTHORITATIVE_INFORMATION_STATUS,
      ProblemStatus.NON_AUTHORITATIVE_INFORMATION_TITLE),

  /**
   * 204 No Content.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.5">RFC 9110
   *     §15.3.5</a>
   */
  NO_CONTENT(ProblemStatus.NO_CONTENT_STATUS, ProblemStatus.NO_CONTENT_TITLE),

  /**
   * 205 Reset Content.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.6">RFC 9110
   *     §15.3.6</a>
   */
  RESET_CONTENT(ProblemStatus.RESET_CONTENT_STATUS, ProblemStatus.RESET_CONTENT_TITLE),

  /**
   * 206 Partial Content.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.3.7">RFC 9110
   *     §15.3.7</a>
   */
  PARTIAL_CONTENT(ProblemStatus.PARTIAL_CONTENT_STATUS, ProblemStatus.PARTIAL_CONTENT_TITLE),

  /**
   * 207 Multi-Status (WebDAV).
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc4918#section-11.1">RFC 4918 §11.1</a>
   */
  MULTI_STATUS(ProblemStatus.MULTI_STATUS_STATUS, ProblemStatus.MULTI_STATUS_TITLE),

  /**
   * 208 Already Reported (WebDAV).
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc5842#section-7.1">RFC 5842 §7.1</a>
   */
  ALREADY_REPORTED(ProblemStatus.ALREADY_REPORTED_STATUS, ProblemStatus.ALREADY_REPORTED_TITLE),

  /**
   * 226 IM Used.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc3229#section-10.4.1">RFC 3229
   *     §10.4.1</a>
   */
  IM_USED(ProblemStatus.IM_USED_STATUS, ProblemStatus.IM_USED_TITLE),

  /**
   * 300 Multiple Choices.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.4.1">RFC 9110
   *     §15.4.1</a>
   */
  MULTIPLE_CHOICES(ProblemStatus.MULTIPLE_CHOICES_STATUS, ProblemStatus.MULTIPLE_CHOICES_TITLE),

  /**
   * 301 Moved Permanently.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.4.2">RFC 9110
   *     §15.4.2</a>
   */
  MOVED_PERMANENTLY(ProblemStatus.MOVED_PERMANENTLY_STATUS, ProblemStatus.MOVED_PERMANENTLY_TITLE),

  /**
   * 302 Found.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.4.3">RFC 9110
   *     §15.4.3</a>
   */
  FOUND(ProblemStatus.FOUND_STATUS, ProblemStatus.FOUND_TITLE),

  /**
   * 303 See Other.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.4.4">RFC 9110
   *     §15.4.4</a>
   */
  SEE_OTHER(ProblemStatus.SEE_OTHER_STATUS, ProblemStatus.SEE_OTHER_TITLE),

  /**
   * 304 Not Modified.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.4.5">RFC 9110
   *     §15.4.5</a>
   */
  NOT_MODIFIED(ProblemStatus.NOT_MODIFIED_STATUS, ProblemStatus.NOT_MODIFIED_TITLE),

  /**
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-6.4.5">RFC 7231 §6.4.5</a>
   * @deprecated Obsoleted by RFC 7231. "Use Proxy" is no longer recommended.
   */
  @Deprecated
  USE_PROXY(ProblemStatus.USE_PROXY_STATUS, ProblemStatus.USE_PROXY_TITLE),

  /**
   * 307 Temporary Redirect.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.4.8">RFC 9110
   *     §15.4.8</a>
   */
  TEMPORARY_REDIRECT(
      ProblemStatus.TEMPORARY_REDIRECT_STATUS, ProblemStatus.TEMPORARY_REDIRECT_TITLE),

  /**
   * 308 Permanent Redirect.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc7538#section-3">RFC 7538 §3</a>
   */
  PERMANENT_REDIRECT(
      ProblemStatus.PERMANENT_REDIRECT_STATUS, ProblemStatus.PERMANENT_REDIRECT_TITLE),

  /**
   * 400 Bad Request.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.1">RFC 9110
   *     §15.5.1</a>
   */
  BAD_REQUEST(ProblemStatus.BAD_REQUEST_STATUS, ProblemStatus.BAD_REQUEST_TITLE),

  /**
   * 401 Unauthorized.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.2">RFC 9110
   *     §15.5.2</a>
   */
  UNAUTHORIZED(ProblemStatus.UNAUTHORIZED_STATUS, ProblemStatus.UNAUTHORIZED_TITLE),

  /**
   * 402 Payment Required.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.3">RFC 9110
   *     §15.5.3</a>
   */
  PAYMENT_REQUIRED(ProblemStatus.PAYMENT_REQUIRED_STATUS, ProblemStatus.PAYMENT_REQUIRED_TITLE),

  /**
   * 403 Forbidden.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.4">RFC 9110
   *     §15.5.4</a>
   */
  FORBIDDEN(ProblemStatus.FORBIDDEN_STATUS, ProblemStatus.FORBIDDEN_TITLE),

  /**
   * 404 Not Found.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.5">RFC 9110
   *     §15.5.5</a>
   */
  NOT_FOUND(ProblemStatus.NOT_FOUND_STATUS, ProblemStatus.NOT_FOUND_TITLE),

  /**
   * 405 Method Not Allowed.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.6">RFC 9110
   *     §15.5.6</a>
   */
  METHOD_NOT_ALLOWED(
      ProblemStatus.METHOD_NOT_ALLOWED_STATUS, ProblemStatus.METHOD_NOT_ALLOWED_TITLE),

  /**
   * 406 Not Acceptable.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.7">RFC 9110
   *     §15.5.7</a>
   */
  NOT_ACCEPTABLE(ProblemStatus.NOT_ACCEPTABLE_STATUS, ProblemStatus.NOT_ACCEPTABLE_TITLE),

  /**
   * 407 Proxy Authentication Required.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.8">RFC 9110
   *     §15.5.8</a>
   */
  PROXY_AUTHENTICATION_REQUIRED(
      ProblemStatus.PROXY_AUTHENTICATION_REQUIRED_STATUS,
      ProblemStatus.PROXY_AUTHENTICATION_REQUIRED_TITLE),

  /**
   * 408 Request Timeout.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.9">RFC 9110
   *     §15.5.9</a>
   */
  REQUEST_TIMEOUT(ProblemStatus.REQUEST_TIMEOUT_STATUS, ProblemStatus.REQUEST_TIMEOUT_TITLE),

  /**
   * 409 Conflict.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.10">RFC 9110
   *     §15.5.10</a>
   */
  CONFLICT(ProblemStatus.CONFLICT_STATUS, ProblemStatus.CONFLICT_TITLE),

  /**
   * 410 Gone.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.11">RFC 9110
   *     §15.5.11</a>
   */
  GONE(ProblemStatus.GONE_STATUS, ProblemStatus.GONE_TITLE),

  /**
   * 411 Length Required.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.12">RFC 9110
   *     §15.5.12</a>
   */
  LENGTH_REQUIRED(ProblemStatus.LENGTH_REQUIRED_STATUS, ProblemStatus.LENGTH_REQUIRED_TITLE),

  /**
   * 412 Precondition Failed.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.13">RFC 9110
   *     §15.5.13</a>
   */
  PRECONDITION_FAILED(
      ProblemStatus.PRECONDITION_FAILED_STATUS, ProblemStatus.PRECONDITION_FAILED_TITLE),

  /**
   * 413 Content Too Large.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.14">RFC 9110
   *     §15.5.14</a>
   */
  CONTENT_TOO_LARGE(ProblemStatus.CONTENT_TOO_LARGE_STATUS, ProblemStatus.CONTENT_TOO_LARGE_TITLE),

  /**
   * 413 Payload Too Large.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.11">RFC 7231
   *     §6.5.11</a>
   * @deprecated Renamed to {@link #CONTENT_TOO_LARGE} in RFC 9110.
   */
  @Deprecated
  PAYLOAD_TOO_LARGE(ProblemStatus.PAYLOAD_TOO_LARGE_STATUS, ProblemStatus.PAYLOAD_TOO_LARGE_TITLE),

  /**
   * 413 Request Entity Too Large.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.14">RFC 2616
   *     §10.4.14</a>
   * @deprecated Renamed to {@link #PAYLOAD_TOO_LARGE} in RFC 7231, then to {@link
   *     #CONTENT_TOO_LARGE} in RFC 9110.
   */
  @Deprecated
  REQUEST_ENTITY_TOO_LARGE(
      ProblemStatus.REQUEST_ENTITY_TOO_LARGE_STATUS, ProblemStatus.REQUEST_ENTITY_TOO_LARGE_TITLE),

  /**
   * 414 URI Too Long.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.15">RFC 9110
   *     §15.5.15</a>
   */
  URI_TOO_LONG(ProblemStatus.URI_TOO_LONG_STATUS, ProblemStatus.URI_TOO_LONG_TITLE),

  /**
   * 414 Request URI Too Long.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.15">RFC 2616
   *     §10.4.15</a>
   * @deprecated Renamed to {@link #URI_TOO_LONG} by RFC 8297.
   */
  @Deprecated
  REQUEST_URI_TOO_LONG(
      ProblemStatus.REQUEST_URI_TOO_LONG_STATUS, ProblemStatus.REQUEST_URI_TOO_LONG_TITLE),

  /**
   * 415 Unsupported Media Type.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.16">RFC 9110
   *     §15.5.16</a>
   */
  UNSUPPORTED_MEDIA_TYPE(
      ProblemStatus.UNSUPPORTED_MEDIA_TYPE_STATUS, ProblemStatus.UNSUPPORTED_MEDIA_TYPE_TITLE),

  /**
   * 416 Range Not Satisfiable.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.17">RFC 9110
   *     §15.5.17</a>
   */
  RANGE_NOT_SATISFIABLE(
      ProblemStatus.RANGE_NOT_SATISFIABLE_STATUS, ProblemStatus.RANGE_NOT_SATISFIABLE_TITLE),

  /**
   * 416 Requested Range Not Satisfiable.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.17">RFC 2616
   *     §10.5.17</a>
   * @deprecated renamed to {@link #RANGE_NOT_SATISFIABLE} by RFC 9110.
   */
  @Deprecated
  REQUESTED_RANGE_NOT_SATISFIABLE(
      ProblemStatus.REQUESTED_RANGE_NOT_SATISFIABLE_STATUS,
      ProblemStatus.REQUESTED_RANGE_NOT_SATISFIABLE_TITLE),

  /**
   * 417 Expectation Failed.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.18">RFC 9110
   *     §15.5.18</a>
   */
  EXPECTATION_FAILED(
      ProblemStatus.EXPECTATION_FAILED_STATUS, ProblemStatus.EXPECTATION_FAILED_TITLE),

  /**
   * 418 I'm a teapot.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2324#section-2.3.2">RFC 2324 §2.3.2</a>
   */
  I_AM_A_TEAPOT(ProblemStatus.I_AM_A_TEAPOT_STATUS, ProblemStatus.I_AM_A_TEAPOT_TITLE),

  /**
   * 421 Misdirected Request.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc7540#section-9.1.2">RFC 7540 §9.1.2</a>
   */
  MISDIRECTED_REQUEST(
      ProblemStatus.MISDIRECTED_REQUEST_STATUS, ProblemStatus.MISDIRECTED_REQUEST_TITLE),

  /**
   * 422 Unprocessable Content.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.21">RFC 9110
   *     §15.5.21</a>
   */
  UNPROCESSABLE_CONTENT(
      ProblemStatus.UNPROCESSABLE_CONTENT_STATUS, ProblemStatus.UNPROCESSABLE_CONTENT_TITLE),

  /**
   * 422 Unprocessable Entity.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc4918#section-11.2">RFC 4918 §11.2</a>
   * @deprecated renamed to {@link #UNPROCESSABLE_CONTENT} by RFC 9110.
   */
  @Deprecated
  UNPROCESSABLE_ENTITY(
      ProblemStatus.UNPROCESSABLE_ENTITY_STATUS, ProblemStatus.UNPROCESSABLE_ENTITY_TITLE),

  /**
   * 423 Locked.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc4918#section-11.3">RFC 4918 §11.3</a>
   */
  LOCKED(ProblemStatus.LOCKED_STATUS, ProblemStatus.LOCKED_TITLE),

  /**
   * 424 Failed Dependency.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc4918#section-11.4">RFC 4918 §11.4</a>
   */
  FAILED_DEPENDENCY(ProblemStatus.FAILED_DEPENDENCY_STATUS, ProblemStatus.FAILED_DEPENDENCY_TITLE),

  /**
   * 425 Too Early.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc8470#section-5.2">RFC 8470 §5.2</a>
   */
  TOO_EARLY(ProblemStatus.TOO_EARLY_STATUS, ProblemStatus.TOO_EARLY_TITLE),

  /**
   * 426 Upgrade Required.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.5.19">RFC 9110
   *     §15.5.19</a>
   */
  UPGRADE_REQUIRED(ProblemStatus.UPGRADE_REQUIRED_STATUS, ProblemStatus.UPGRADE_REQUIRED_TITLE),

  /**
   * 428 Precondition Required.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc6585#section-3">RFC 6585 §3</a>
   */
  PRECONDITION_REQUIRED(
      ProblemStatus.PRECONDITION_REQUIRED_STATUS, ProblemStatus.PRECONDITION_REQUIRED_TITLE),

  /**
   * 429 Too Many Requests.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc6585#section-4">RFC 6585 §4</a>
   */
  TOO_MANY_REQUESTS(ProblemStatus.TOO_MANY_REQUESTS_STATUS, ProblemStatus.TOO_MANY_REQUESTS_TITLE),

  /**
   * 431 Request Header Fields Too Large.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc6585#section-5">RFC 6585 §5</a>
   */
  REQUEST_HEADER_FIELDS_TOO_LARGE(
      ProblemStatus.REQUEST_HEADER_FIELDS_TOO_LARGE_STATUS,
      ProblemStatus.REQUEST_HEADER_FIELDS_TOO_LARGE_TITLE),

  /**
   * 451 Unavailable For Legal Reasons.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc7725#section-3">RFC 7725 §3</a>
   */
  UNAVAILABLE_FOR_LEGAL_REASONS(
      ProblemStatus.UNAVAILABLE_FOR_LEGAL_REASONS_STATUS,
      ProblemStatus.UNAVAILABLE_FOR_LEGAL_REASONS_TITLE),

  /**
   * 500 Internal Server Error.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.1">RFC 9110
   *     §15.6.1</a>
   */
  INTERNAL_SERVER_ERROR(
      ProblemStatus.INTERNAL_SERVER_ERROR_STATUS, ProblemStatus.INTERNAL_SERVER_ERROR_TITLE),

  /**
   * 501 Not Implemented.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.2">RFC 9110
   *     §15.6.2</a>
   */
  NOT_IMPLEMENTED(ProblemStatus.NOT_IMPLEMENTED_STATUS, ProblemStatus.NOT_IMPLEMENTED_TITLE),

  /**
   * 502 Bad Gateway.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.3">RFC 9110
   *     §15.6.3</a>
   */
  BAD_GATEWAY(ProblemStatus.BAD_GATEWAY_STATUS, ProblemStatus.BAD_GATEWAY_TITLE),

  /**
   * 503 Service Unavailable.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.4">RFC 9110
   *     §15.6.4</a>
   */
  SERVICE_UNAVAILABLE(
      ProblemStatus.SERVICE_UNAVAILABLE_STATUS, ProblemStatus.SERVICE_UNAVAILABLE_TITLE),

  /**
   * 504 Gateway Timeout.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.5">RFC 9110
   *     §15.6.5</a>
   */
  GATEWAY_TIMEOUT(ProblemStatus.GATEWAY_TIMEOUT_STATUS, ProblemStatus.GATEWAY_TIMEOUT_TITLE),

  /**
   * 505 HTTP Version Not Supported.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15.6.6">RFC 9110
   *     §15.6.6</a>
   */
  HTTP_VERSION_NOT_SUPPORTED(
      ProblemStatus.HTTP_VERSION_NOT_SUPPORTED_STATUS,
      ProblemStatus.HTTP_VERSION_NOT_SUPPORTED_TITLE),

  /**
   * 506 Variant Also Negotiates.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2295#section-8.1">RFC 2295 §8.1</a>
   */
  VARIANT_ALSO_NEGOTIATES(
      ProblemStatus.VARIANT_ALSO_NEGOTIATES_STATUS, ProblemStatus.VARIANT_ALSO_NEGOTIATES_TITLE),

  /**
   * 507 Insufficient Storage (WebDAV).
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc4918#section-11.5">RFC 4918 §11.5</a>
   */
  INSUFFICIENT_STORAGE(
      ProblemStatus.INSUFFICIENT_STORAGE_STATUS, ProblemStatus.INSUFFICIENT_STORAGE_TITLE),

  /**
   * 508 Loop Detected (WebDAV).
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc5842#section-7.2">RFC 5842 §7.2</a>
   */
  LOOP_DETECTED(ProblemStatus.LOOP_DETECTED_STATUS, ProblemStatus.LOOP_DETECTED_TITLE),

  /** 509 Bandwidth Limit Exceeded (unofficial). */
  BANDWIDTH_LIMIT_EXCEEDED(
      ProblemStatus.BANDWIDTH_LIMIT_EXCEEDED_STATUS, ProblemStatus.BANDWIDTH_LIMIT_EXCEEDED_TITLE),

  /**
   * 510 Not Extended.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc2774#section-7">RFC 2774 §7</a>
   */
  NOT_EXTENDED(ProblemStatus.NOT_EXTENDED_STATUS, ProblemStatus.NOT_EXTENDED_TITLE),

  /**
   * 511 Network Authentication Required.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc6585#section-6">RFC 6585 §6</a>
   */
  NETWORK_AUTHENTICATION_REQUIRED(
      ProblemStatus.NETWORK_AUTHENTICATION_REQUIRED_STATUS,
      ProblemStatus.NETWORK_AUTHENTICATION_REQUIRED_TITLE);

  /** Status of {@link #CONTINUE}. */
  public static final int CONTINUE_STATUS = 100;

  /** Title of {@link #CONTINUE}. */
  public static final String CONTINUE_TITLE = "Continue";

  /** Status of {@link #SWITCHING_PROTOCOLS}. */
  public static final int SWITCHING_PROTOCOLS_STATUS = 101;

  /** Title of {@link #SWITCHING_PROTOCOLS}. */
  public static final String SWITCHING_PROTOCOLS_TITLE = "Switching Protocols";

  /** Status of {@link #PROCESSING}. */
  public static final int PROCESSING_STATUS = 102;

  /** Title of {@link #PROCESSING}. */
  public static final String PROCESSING_TITLE = "Processing";

  /** Status of {@link #EARLY_HINTS}. */
  public static final int EARLY_HINTS_STATUS = 103;

  /** Title of {@link #EARLY_HINTS}. */
  public static final String EARLY_HINTS_TITLE = "Early Hints";

  /**
   * Status of {@link #CHECKPOINT}.
   *
   * @deprecated Renamed to {@link #EARLY_HINTS} by RFC 8297.
   */
  @Deprecated public static final int CHECKPOINT_STATUS = 103;

  /**
   * Title of {@link #CHECKPOINT}.
   *
   * @deprecated Renamed to {@link #EARLY_HINTS} by RFC 8297.
   */
  @Deprecated public static final String CHECKPOINT_TITLE = "Checkpoint";

  /** Status of {@link #OK}. */
  public static final int OK_STATUS = 200;

  /** Title of {@link #OK}. */
  public static final String OK_TITLE = "OK";

  /** Status of {@link #CREATED}. */
  public static final int CREATED_STATUS = 201;

  /** Title of {@link #CREATED}. */
  public static final String CREATED_TITLE = "Created";

  /** Status of {@link #ACCEPTED}. */
  public static final int ACCEPTED_STATUS = 202;

  /** Title of {@link #ACCEPTED}. */
  public static final String ACCEPTED_TITLE = "Accepted";

  /** Status of {@link #NON_AUTHORITATIVE_INFORMATION}. */
  public static final int NON_AUTHORITATIVE_INFORMATION_STATUS = 203;

  /** Title of {@link #NON_AUTHORITATIVE_INFORMATION}. */
  public static final String NON_AUTHORITATIVE_INFORMATION_TITLE = "Non-Authoritative Information";

  /** Status of {@link #NO_CONTENT}. */
  public static final int NO_CONTENT_STATUS = 204;

  /** Title of {@link #NO_CONTENT}. */
  public static final String NO_CONTENT_TITLE = "No Content";

  /** Status of {@link #RESET_CONTENT}. */
  public static final int RESET_CONTENT_STATUS = 205;

  /** Title of {@link #RESET_CONTENT}. */
  public static final String RESET_CONTENT_TITLE = "Reset Content";

  /** Status of {@link #PARTIAL_CONTENT}. */
  public static final int PARTIAL_CONTENT_STATUS = 206;

  /** Title of {@link #PARTIAL_CONTENT}. */
  public static final String PARTIAL_CONTENT_TITLE = "Partial Content";

  /** Status of {@link #MULTI_STATUS}. */
  public static final int MULTI_STATUS_STATUS = 207;

  /** Title of {@link #MULTI_STATUS}. */
  public static final String MULTI_STATUS_TITLE = "Multi-Status";

  /** Status of {@link #ALREADY_REPORTED}. */
  public static final int ALREADY_REPORTED_STATUS = 208;

  /** Title of {@link #ALREADY_REPORTED}. */
  public static final String ALREADY_REPORTED_TITLE = "Already Reported";

  /** Status of {@link #IM_USED}. */
  public static final int IM_USED_STATUS = 226;

  /** Title of {@link #IM_USED}. */
  public static final String IM_USED_TITLE = "IM Used";

  /** Status of {@link #MULTIPLE_CHOICES}. */
  public static final int MULTIPLE_CHOICES_STATUS = 300;

  /** Title of {@link #MULTIPLE_CHOICES}. */
  public static final String MULTIPLE_CHOICES_TITLE = "Multiple Choices";

  /** Status of {@link #MOVED_PERMANENTLY}. */
  public static final int MOVED_PERMANENTLY_STATUS = 301;

  /** Title of {@link #MOVED_PERMANENTLY}. */
  public static final String MOVED_PERMANENTLY_TITLE = "Moved Permanently";

  /** Status of {@link #FOUND}. */
  public static final int FOUND_STATUS = 302;

  /** Title of {@link #FOUND}. */
  public static final String FOUND_TITLE = "Found";

  /** Status of {@link #SEE_OTHER}. */
  public static final int SEE_OTHER_STATUS = 303;

  /** Title of {@link #SEE_OTHER}. */
  public static final String SEE_OTHER_TITLE = "See Other";

  /** Status of {@link #NOT_MODIFIED}. */
  public static final int NOT_MODIFIED_STATUS = 304;

  /** Title of {@link #NOT_MODIFIED}. */
  public static final String NOT_MODIFIED_TITLE = "Not Modified";

  /**
   * Status of {@link #USE_PROXY}.
   *
   * @deprecated Obsoleted by RFC 7231. "Use Proxy" is no longer recommended.
   */
  @Deprecated public static final int USE_PROXY_STATUS = 305;

  /**
   * Title of {@link #USE_PROXY}.
   *
   * @deprecated Obsoleted by RFC 7231. "Use Proxy" is no longer recommended.
   */
  @Deprecated public static final String USE_PROXY_TITLE = "Use Proxy";

  /** Status of {@link #TEMPORARY_REDIRECT}. */
  public static final int TEMPORARY_REDIRECT_STATUS = 307;

  /** Title of {@link #TEMPORARY_REDIRECT}. */
  public static final String TEMPORARY_REDIRECT_TITLE = "Temporary Redirect";

  /** Status of {@link #PERMANENT_REDIRECT}. */
  public static final int PERMANENT_REDIRECT_STATUS = 308;

  /** Title of {@link #PERMANENT_REDIRECT}. */
  public static final String PERMANENT_REDIRECT_TITLE = "Permanent Redirect";

  /** Status of {@link #BAD_REQUEST}. */
  public static final int BAD_REQUEST_STATUS = 400;

  /** Title of {@link #BAD_REQUEST}. */
  public static final String BAD_REQUEST_TITLE = "Bad Request";

  /** Status of {@link #UNAUTHORIZED}. */
  public static final int UNAUTHORIZED_STATUS = 401;

  /** Title of {@link #UNAUTHORIZED}. */
  public static final String UNAUTHORIZED_TITLE = "Unauthorized";

  /** Status of {@link #PAYMENT_REQUIRED}. */
  public static final int PAYMENT_REQUIRED_STATUS = 402;

  /** Title of {@link #PAYMENT_REQUIRED}. */
  public static final String PAYMENT_REQUIRED_TITLE = "Payment Required";

  /** Status of {@link #FORBIDDEN}. */
  public static final int FORBIDDEN_STATUS = 403;

  /** Title of {@link #FORBIDDEN}. */
  public static final String FORBIDDEN_TITLE = "Forbidden";

  /** Status of {@link #NOT_FOUND}. */
  public static final int NOT_FOUND_STATUS = 404;

  /** Title of {@link #NOT_FOUND}. */
  public static final String NOT_FOUND_TITLE = "Not Found";

  /** Status of {@link #METHOD_NOT_ALLOWED}. */
  public static final int METHOD_NOT_ALLOWED_STATUS = 405;

  /** Title of {@link #METHOD_NOT_ALLOWED}. */
  public static final String METHOD_NOT_ALLOWED_TITLE = "Method Not Allowed";

  /** Status of {@link #NOT_ACCEPTABLE}. */
  public static final int NOT_ACCEPTABLE_STATUS = 406;

  /** Title of {@link #NOT_ACCEPTABLE}. */
  public static final String NOT_ACCEPTABLE_TITLE = "Not Acceptable";

  /** Status of {@link #PROXY_AUTHENTICATION_REQUIRED}. */
  public static final int PROXY_AUTHENTICATION_REQUIRED_STATUS = 407;

  /** Title of {@link #PROXY_AUTHENTICATION_REQUIRED}. */
  public static final String PROXY_AUTHENTICATION_REQUIRED_TITLE = "Proxy Authentication Required";

  /** Status of {@link #REQUEST_TIMEOUT}. */
  public static final int REQUEST_TIMEOUT_STATUS = 408;

  /** Title of {@link #REQUEST_TIMEOUT}. */
  public static final String REQUEST_TIMEOUT_TITLE = "Request Timeout";

  /** Status of {@link #CONFLICT}. */
  public static final int CONFLICT_STATUS = 409;

  /** Title of {@link #CONFLICT}. */
  public static final String CONFLICT_TITLE = "Conflict";

  /** Status of {@link #GONE}. */
  public static final int GONE_STATUS = 410;

  /** Title of {@link #GONE}. */
  public static final String GONE_TITLE = "Gone";

  /** Status of {@link #LENGTH_REQUIRED}. */
  public static final int LENGTH_REQUIRED_STATUS = 411;

  /** Title of {@link #LENGTH_REQUIRED}. */
  public static final String LENGTH_REQUIRED_TITLE = "Length Required";

  /** Status of {@link #PRECONDITION_FAILED}. */
  public static final int PRECONDITION_FAILED_STATUS = 412;

  /** Title of {@link #PRECONDITION_FAILED}. */
  public static final String PRECONDITION_FAILED_TITLE = "Precondition Failed";

  /** Status of {@link #CONTENT_TOO_LARGE}. */
  public static final int CONTENT_TOO_LARGE_STATUS = 413;

  /** Title of {@link #CONTENT_TOO_LARGE}. */
  public static final String CONTENT_TOO_LARGE_TITLE = "Content Too Large";

  /**
   * Status of {@link #PAYLOAD_TOO_LARGE}.
   *
   * @deprecated Renamed to {@link #CONTENT_TOO_LARGE} in RFC 9110.
   */
  @Deprecated public static final int PAYLOAD_TOO_LARGE_STATUS = 413;

  /**
   * Title of {@link #PAYLOAD_TOO_LARGE}.
   *
   * @deprecated Renamed to {@link #CONTENT_TOO_LARGE} in RFC 9110.
   */
  @Deprecated public static final String PAYLOAD_TOO_LARGE_TITLE = "Payload Too Large";

  /**
   * Status of {@link #REQUEST_ENTITY_TOO_LARGE}.
   *
   * @deprecated Renamed to {@link #PAYLOAD_TOO_LARGE} in RFC 7231, then to {@link
   *     #CONTENT_TOO_LARGE} in RFC 9110.
   */
  @Deprecated public static final int REQUEST_ENTITY_TOO_LARGE_STATUS = 413;

  /**
   * Title of {@link #REQUEST_ENTITY_TOO_LARGE}.
   *
   * @deprecated Renamed to {@link #PAYLOAD_TOO_LARGE} in RFC 7231, then to {@link
   *     #CONTENT_TOO_LARGE} in RFC 9110.
   */
  @Deprecated
  public static final String REQUEST_ENTITY_TOO_LARGE_TITLE = "Request Entity Too Large";

  /** Status of {@link #URI_TOO_LONG}. */
  public static final int URI_TOO_LONG_STATUS = 414;

  /** Title of {@link #URI_TOO_LONG}. */
  public static final String URI_TOO_LONG_TITLE = "URI Too Long";

  /**
   * Status of {@link #REQUEST_URI_TOO_LONG}.
   *
   * @deprecated Renamed to {@link #URI_TOO_LONG} by RFC 8297.
   */
  @Deprecated public static final int REQUEST_URI_TOO_LONG_STATUS = 414;

  /**
   * Title of {@link #REQUEST_URI_TOO_LONG}.
   *
   * @deprecated Renamed to {@link #URI_TOO_LONG} by RFC 8297.
   */
  @Deprecated public static final String REQUEST_URI_TOO_LONG_TITLE = "Request-URI Too Long";

  /** Status of {@link #UNSUPPORTED_MEDIA_TYPE}. */
  public static final int UNSUPPORTED_MEDIA_TYPE_STATUS = 415;

  /** Title of {@link #UNSUPPORTED_MEDIA_TYPE}. */
  public static final String UNSUPPORTED_MEDIA_TYPE_TITLE = "Unsupported Media Type";

  /** Status of {@link #RANGE_NOT_SATISFIABLE}. */
  public static final int RANGE_NOT_SATISFIABLE_STATUS = 416;

  /** Title of {@link #RANGE_NOT_SATISFIABLE}. */
  public static final String RANGE_NOT_SATISFIABLE_TITLE = "Range Not Satisfiable";

  /**
   * Status of {@link #REQUESTED_RANGE_NOT_SATISFIABLE}.
   *
   * @deprecated renamed to {@link #RANGE_NOT_SATISFIABLE} by RFC 9110.
   */
  @Deprecated public static final int REQUESTED_RANGE_NOT_SATISFIABLE_STATUS = 416;

  /**
   * Title of {@link #REQUESTED_RANGE_NOT_SATISFIABLE}.
   *
   * @deprecated renamed to {@link #RANGE_NOT_SATISFIABLE} by RFC 9110.
   */
  @Deprecated
  public static final String REQUESTED_RANGE_NOT_SATISFIABLE_TITLE =
      "Requested Range Not Satisfiable";

  /** Status of {@link #EXPECTATION_FAILED}. */
  public static final int EXPECTATION_FAILED_STATUS = 417;

  /** Title of {@link #EXPECTATION_FAILED}. */
  public static final String EXPECTATION_FAILED_TITLE = "Expectation Failed";

  /** Status of {@link #I_AM_A_TEAPOT}. */
  public static final int I_AM_A_TEAPOT_STATUS = 418;

  /** Title of {@link #I_AM_A_TEAPOT}. */
  public static final String I_AM_A_TEAPOT_TITLE = "I'm a teapot";

  /** Status of {@link #MISDIRECTED_REQUEST}. */
  public static final int MISDIRECTED_REQUEST_STATUS = 421;

  /** Title of {@link #MISDIRECTED_REQUEST}. */
  public static final String MISDIRECTED_REQUEST_TITLE = "Misdirected Request";

  /** Status of {@link #UNPROCESSABLE_CONTENT}. */
  public static final int UNPROCESSABLE_CONTENT_STATUS = 422;

  /** Title of {@link #UNPROCESSABLE_CONTENT}. */
  public static final String UNPROCESSABLE_CONTENT_TITLE = "Unprocessable Content";

  /**
   * Status of {@link #UNPROCESSABLE_ENTITY}.
   *
   * @deprecated renamed to {@link #UNPROCESSABLE_CONTENT} by RFC 9110.
   */
  @Deprecated public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

  /**
   * Title of {@link #UNPROCESSABLE_ENTITY}.
   *
   * @deprecated renamed to {@link #UNPROCESSABLE_CONTENT} by RFC 9110.
   */
  @Deprecated public static final String UNPROCESSABLE_ENTITY_TITLE = "Unprocessable Entity";

  /** Status of {@link #LOCKED}. */
  public static final int LOCKED_STATUS = 423;

  /** Title of {@link #LOCKED}. */
  public static final String LOCKED_TITLE = "Locked";

  /** Status of {@link #FAILED_DEPENDENCY}. */
  public static final int FAILED_DEPENDENCY_STATUS = 424;

  /** Title of {@link #FAILED_DEPENDENCY}. */
  public static final String FAILED_DEPENDENCY_TITLE = "Failed Dependency";

  /** Status of {@link #TOO_EARLY}. */
  public static final int TOO_EARLY_STATUS = 425;

  /** Title of {@link #TOO_EARLY}. */
  public static final String TOO_EARLY_TITLE = "Too Early";

  /** Status of {@link #UPGRADE_REQUIRED}. */
  public static final int UPGRADE_REQUIRED_STATUS = 426;

  /** Title of {@link #UPGRADE_REQUIRED}. */
  public static final String UPGRADE_REQUIRED_TITLE = "Upgrade Required";

  /** Status of {@link #PRECONDITION_REQUIRED}. */
  public static final int PRECONDITION_REQUIRED_STATUS = 428;

  /** Title of {@link #PRECONDITION_REQUIRED}. */
  public static final String PRECONDITION_REQUIRED_TITLE = "Precondition Required";

  /** Status of {@link #TOO_MANY_REQUESTS}. */
  public static final int TOO_MANY_REQUESTS_STATUS = 429;

  /** Title of {@link #TOO_MANY_REQUESTS}. */
  public static final String TOO_MANY_REQUESTS_TITLE = "Too Many Requests";

  /** Status of {@link #REQUEST_HEADER_FIELDS_TOO_LARGE}. */
  public static final int REQUEST_HEADER_FIELDS_TOO_LARGE_STATUS = 431;

  /** Title of {@link #REQUEST_HEADER_FIELDS_TOO_LARGE}. */
  public static final String REQUEST_HEADER_FIELDS_TOO_LARGE_TITLE =
      "Request Header Fields Too Large";

  /** Status of {@link #UNAVAILABLE_FOR_LEGAL_REASONS}. */
  public static final int UNAVAILABLE_FOR_LEGAL_REASONS_STATUS = 451;

  /** Title of {@link #UNAVAILABLE_FOR_LEGAL_REASONS}. */
  public static final String UNAVAILABLE_FOR_LEGAL_REASONS_TITLE = "Unavailable For Legal Reasons";

  /** Status of {@link #INTERNAL_SERVER_ERROR}. */
  public static final int INTERNAL_SERVER_ERROR_STATUS = 500;

  /** Title of {@link #INTERNAL_SERVER_ERROR}. */
  public static final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error";

  /** Status of {@link #NOT_IMPLEMENTED}. */
  public static final int NOT_IMPLEMENTED_STATUS = 501;

  /** Title of {@link #NOT_IMPLEMENTED}. */
  public static final String NOT_IMPLEMENTED_TITLE = "Not Implemented";

  /** Status of {@link #BAD_GATEWAY}. */
  public static final int BAD_GATEWAY_STATUS = 502;

  /** Title of {@link #BAD_GATEWAY}. */
  public static final String BAD_GATEWAY_TITLE = "Bad Gateway";

  /** Status of {@link #SERVICE_UNAVAILABLE}. */
  public static final int SERVICE_UNAVAILABLE_STATUS = 503;

  /** Title of {@link #SERVICE_UNAVAILABLE}. */
  public static final String SERVICE_UNAVAILABLE_TITLE = "Service Unavailable";

  /** Status of {@link #GATEWAY_TIMEOUT}. */
  public static final int GATEWAY_TIMEOUT_STATUS = 504;

  /** Title of {@link #GATEWAY_TIMEOUT}. */
  public static final String GATEWAY_TIMEOUT_TITLE = "Gateway Timeout";

  /** Status of {@link #HTTP_VERSION_NOT_SUPPORTED}. */
  public static final int HTTP_VERSION_NOT_SUPPORTED_STATUS = 505;

  /** Title of {@link #HTTP_VERSION_NOT_SUPPORTED}. */
  public static final String HTTP_VERSION_NOT_SUPPORTED_TITLE = "HTTP Version Not Supported";

  /** Status of {@link #VARIANT_ALSO_NEGOTIATES}. */
  public static final int VARIANT_ALSO_NEGOTIATES_STATUS = 506;

  /** Title of {@link #VARIANT_ALSO_NEGOTIATES}. */
  public static final String VARIANT_ALSO_NEGOTIATES_TITLE = "Variant Also Negotiates";

  /** Status of {@link #INSUFFICIENT_STORAGE}. */
  public static final int INSUFFICIENT_STORAGE_STATUS = 507;

  /** Title of {@link #INSUFFICIENT_STORAGE}. */
  public static final String INSUFFICIENT_STORAGE_TITLE = "Insufficient Storage";

  /** Status of {@link #LOOP_DETECTED}. */
  public static final int LOOP_DETECTED_STATUS = 508;

  /** Title of {@link #LOOP_DETECTED}. */
  public static final String LOOP_DETECTED_TITLE = "Loop Detected";

  /** Status of {@link #BANDWIDTH_LIMIT_EXCEEDED}. */
  public static final int BANDWIDTH_LIMIT_EXCEEDED_STATUS = 509;

  /** Title of {@link #BANDWIDTH_LIMIT_EXCEEDED}. */
  public static final String BANDWIDTH_LIMIT_EXCEEDED_TITLE = "Bandwidth Limit Exceeded";

  /** Status of {@link #NOT_EXTENDED}. */
  public static final int NOT_EXTENDED_STATUS = 510;

  /** Title of {@link #NOT_EXTENDED}. */
  public static final String NOT_EXTENDED_TITLE = "Not Extended";

  /** Status of {@link #NETWORK_AUTHENTICATION_REQUIRED}. */
  public static final int NETWORK_AUTHENTICATION_REQUIRED_STATUS = 511;

  /** Title of {@link #NETWORK_AUTHENTICATION_REQUIRED}. */
  public static final String NETWORK_AUTHENTICATION_REQUIRED_TITLE =
      "Network Authentication Required";

  /**
   * Return the {@link ProblemStatus} matching the given integer HTTP status code.
   *
   * @param status the HTTP status code to look up (for example {@code 404})
   * @return an {@link Optional} containing the matching {@link ProblemStatus} if present, or empty
   *     if there is no enum constant for the provided code
   */
  public static Optional<ProblemStatus> findValue(int status) {
    return StatusMapHolder.findValue(status);
  }

  /**
   * Return the {@link ProblemStatus} matching the given integer HTTP status code. Overloaded only
   * for nullability convenience.
   *
   * @param status the HTTP status code to look up (for example {@code 404})
   * @return an {@link Optional} containing the matching {@link ProblemStatus} if present, or empty
   *     if there is no enum constant for the provided code
   */
  public static Optional<ProblemStatus> findValue(Integer status) {
    if (status == null) {
      return Optional.empty();
    }
    return StatusMapHolder.findValue(status);
  }

  /**
   * Human-readable title of the HTTP status as commonly used in responses (for example {@code "Not
   * Found"} for 404).
   */
  private final String title;

  /** Integer HTTP status code (for example {@code 404}). */
  private final int status;

  /**
   * Construct a {@link ProblemStatus} enum constant.
   *
   * @param status integer HTTP status code
   * @param title human-readable title commonly associated with the status code
   */
  ProblemStatus(int status, String title) {
    this.title = title;
    this.status = status;
  }

  /**
   * Get the human-readable title associated with this status code.
   *
   * @return the title string (never {@code null})
   */
  public String getTitle() {
    return title;
  }

  /**
   * Get the integer HTTP status code for this enum constant.
   *
   * @return the numeric status code (for example {@code 200}, {@code 404})
   */
  public int getStatus() {
    return status;
  }

  /**
   * Nested static class to hold the lazily initialized lookup map from integer HTTP status codes to
   * {@link ProblemStatus} enum constants.
   *
   * <p>This classis loaded and its static fields are initialized only when {@link
   * #findValue(Integer)} is called for the first time, ensuring that the initialization of the
   * lookup map is deferred until it is actually needed.
   */
  private static final class StatusMapHolder {

    private static final Map<Integer, ProblemStatus> STATUSES_BY_CODE =
        Arrays.stream(values())
            .collect(
                Collectors.toMap(
                    ProblemStatus::getStatus,
                    Function.identity(),
                    (existing, replacement) -> existing));

    private static Optional<ProblemStatus> findValue(Integer status) {
      return Optional.ofNullable(STATUSES_BY_CODE.get(status));
    }
  }
}
