/*
 * Copyright (c) 2025-2026 The Problem4J Authors
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

import java.net.URI;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class ProblemImpl extends AbstractProblem {

  private static final long serialVersionUID = 1L;

  ProblemImpl(String title, int status, @Nullable Map<String, ? extends Object> extensions) {
    super(BLANK_TYPE, title, status, null, null, extensions);
  }

  ProblemImpl(
      String title,
      int status,
      @Nullable String detail,
      @Nullable Map<String, ? extends Object> extensions) {
    super(BLANK_TYPE, title, status, detail, null, extensions);
  }

  ProblemImpl(
      URI type,
      String title,
      int status,
      @Nullable String detail,
      @Nullable URI instance,
      @Nullable Map<String, ? extends Object> extensions) {
    super(type, title, status, detail, instance, extensions);
  }

  static final class ExtensionImpl extends AbstractExtension {

    private static final long serialVersionUID = 1L;

    ExtensionImpl(String key, @Nullable Object value) {
      super(key, value);
    }
  }
}
