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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Built-in {@link StatusTitleResolver} backed by a static map of well-known HTTP status codes and
 * their titles.
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
final class DefaultStatusTitleResolver implements StatusTitleResolver, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Map<Integer, String> TITLES = new HashMap<>();

  static {
    // 1xx Informational
    TITLES.put(100, "Continue"); // RFC 9110 §15.2.1
    TITLES.put(101, "Switching Protocols"); // RFC 9110 §15.2.2
    TITLES.put(102, "Processing"); // RFC 2518 §10.1 (WebDAV)
    TITLES.put(103, "Early Hints"); // RFC 8297 §2

    // Deprecated status - kept commented-out for references.
    // map.put(103, "Checkpoint");

    // 2xx Success
    TITLES.put(200, "OK"); // RFC 9110 §15.3.1
    TITLES.put(201, "Created"); // RFC 9110 §15.3.2
    TITLES.put(202, "Accepted"); // RFC 9110 §15.3.3
    TITLES.put(203, "Non-Authoritative Information"); // RFC 9110 §15.3.4
    TITLES.put(204, "No Content"); // RFC 9110 §15.3.5
    TITLES.put(205, "Reset Content"); // RFC 9110 §15.3.6
    TITLES.put(206, "Partial Content"); // RFC 9110 §15.3.7
    TITLES.put(207, "Multi-Status"); // RFC 4918 §11.1 (WebDAV)
    TITLES.put(208, "Already Reported"); // RFC 5842 §7.1 (WebDAV)
    TITLES.put(226, "IM Used"); // RFC 3229 §10.4.1

    // 3xx Redirection
    TITLES.put(300, "Multiple Choices"); // RFC 9110 §15.4.1
    TITLES.put(301, "Moved Permanently"); // RFC 9110 §15.4.2
    TITLES.put(302, "Found"); // RFC 9110 §15.4.3
    TITLES.put(303, "See Other"); // RFC 9110 §15.4.4
    TITLES.put(304, "Not Modified"); // RFC 9110 §15.4.5
    TITLES.put(305, "Use Proxy"); // RFC 7231 §6.4.5
    TITLES.put(307, "Temporary Redirect"); // RFC 9110 §15.4.8
    TITLES.put(308, "Permanent Redirect"); // RFC 7538 §3

    // 4xx Client Error
    TITLES.put(400, "Bad Request"); // RFC 9110 §15.5.1
    TITLES.put(401, "Unauthorized"); // RFC 9110 §15.5.2
    TITLES.put(402, "Payment Required"); // RFC 9110 §15.5.3
    TITLES.put(403, "Forbidden"); // RFC 9110 §15.5.4
    TITLES.put(404, "Not Found"); // RFC 9110 §15.5.5
    TITLES.put(405, "Method Not Allowed"); // RFC 9110 §15.5.6
    TITLES.put(406, "Not Acceptable"); // RFC 9110 §15.5.7
    TITLES.put(407, "Proxy Authentication Required"); // RFC 9110 §15.5.8
    TITLES.put(408, "Request Timeout"); // RFC 9110 §15.5.9
    TITLES.put(409, "Conflict"); // RFC 9110 §15.5.10
    TITLES.put(410, "Gone"); // RFC 9110 §15.5.11
    TITLES.put(411, "Length Required"); // RFC 9110 §15.5.12
    TITLES.put(412, "Precondition Failed"); // RFC 9110 §15.5.13
    TITLES.put(413, "Content Too Large"); // RFC 9110 §15.5.14

    // Deprecated statuses - kept commented-out for references.
    // map.put(413, "Payload Too Large"); // renamed to "Content Too Large" in RFC 9110
    // map.put(413, "Request Entity Too Large"); // renamed to "Payload Too Large" in RFC 7231, then
    // to "Content Too Large" in RFC 9110

    TITLES.put(414, "URI Too Long"); // RFC 9110 §15.5.15

    // Deprecated status - kept commented-out for references.
    // map.put(414, "Request-URI Too Long"); // renamed to "URI Too Long" by RFC 7231

    TITLES.put(415, "Unsupported Media Type"); // RFC 9110 §15.5.16
    TITLES.put(416, "Range Not Satisfiable"); // RFC 9110 §15.5.17

    // Deprecated status - kept commented-out for references.
    // map.put(416, "Requested Range Not Satisfiable"); // renamed to "Range Not Satisfiable" by RFC
    // 9110

    TITLES.put(417, "Expectation Failed"); // RFC 9110 §15.5.18
    TITLES.put(418, "I'm a teapot"); // RFC 2324 §2.3.2
    TITLES.put(421, "Misdirected Request"); // RFC 7540 §9.1.2
    TITLES.put(422, "Unprocessable Content"); // RFC 9110 §15.5.21

    // Deprecated status - kept commented-out for references.
    // map.put(422, "Unprocessable Entity"); // renamed to "Unprocessable Content" by RFC 9110

    TITLES.put(423, "Locked"); // RFC 4918 §11.3 (WebDAV)
    TITLES.put(424, "Failed Dependency"); // RFC 4918 §11.4 (WebDAV)
    TITLES.put(425, "Too Early"); // RFC 8470 §5.2
    TITLES.put(426, "Upgrade Required"); // RFC 9110 §15.5.19
    TITLES.put(428, "Precondition Required"); // RFC 6585 §3
    TITLES.put(429, "Too Many Requests"); // RFC 6585 §4
    TITLES.put(431, "Request Header Fields Too Large"); // RFC 6585 §5
    TITLES.put(451, "Unavailable For Legal Reasons"); // RFC 7725 §3

    // 5xx Server Error
    TITLES.put(500, "Internal Server Error"); // RFC 9110 §15.6.1
    TITLES.put(501, "Not Implemented"); // RFC 9110 §15.6.2
    TITLES.put(502, "Bad Gateway"); // RFC 9110 §15.6.3
    TITLES.put(503, "Service Unavailable"); // RFC 9110 §15.6.4
    TITLES.put(504, "Gateway Timeout"); // RFC 9110 §15.6.5
    TITLES.put(505, "HTTP Version Not Supported"); // RFC 9110 §15.6.6
    TITLES.put(506, "Variant Also Negotiates"); // RFC 2295 §8.1
    TITLES.put(507, "Insufficient Storage"); // RFC 4918 §11.5 (WebDAV)
    TITLES.put(508, "Loop Detected"); // RFC 5842 §7.2 (WebDAV)
    TITLES.put(509, "Bandwidth Limit Exceeded"); // unofficial
    TITLES.put(510, "Not Extended"); // RFC 2774 §7
    TITLES.put(511, "Network Authentication Required"); // RFC 6585 §6
  }

  DefaultStatusTitleResolver() {}

  @Override
  public Optional<String> resolve(int status) {
    return Optional.ofNullable(TITLES.get(status));
  }

  @Override
  public int getPriority() {
    return Integer.MAX_VALUE;
  }
}
