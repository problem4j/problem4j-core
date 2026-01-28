/*
 * Copyright (c) 2026 Damian Malczewski
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

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Helper class to create {@link ProblemMapper} instances by delegating it to {@link ServiceLoader}.
 */
final class ProblemMapperInstance {

  /**
   * Creates a default {@link ProblemMapper} instance. The returned mapper provides the standard
   * mapping behavior defined by this library.
   *
   * <p>This method attempts to load a {@link ProblemMapper} implementation using the {@link
   * ServiceLoader} mechanism. If no implementation is found or if an error occurs during loading,
   * it falls back to the default {@link ProblemMapperImpl}.
   *
   * <p>To configure a custom implementation, create a file named {@code
   * META-INF/services/io.github.problem4j.core.ProblemMapper} in your classpath, and list the fully
   * qualified class name of your implementation in that file.
   *
   * @return a new {@link ProblemMapper} instance
   */
  static ProblemMapper createInstance() {
    try {
      ServiceLoader<ProblemMapper> serviceLoader = ServiceLoader.load(ProblemMapper.class);

      Iterator<ProblemMapper> iterator = serviceLoader.iterator();

      while (iterator.hasNext()) {
        try {
          return iterator.next();
        } catch (Throwable e) {
          // ignore - try next
        }
      }
    } catch (Throwable t) {
      // ignore - fall back to default
    }
    return new ProblemMapperImpl();
  }
}
