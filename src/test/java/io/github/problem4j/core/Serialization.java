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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

final class Serialization {

  /**
   * Serializes and deserializes an object, returning the result. This is useful for testing that an
   * object is serializable and that it works correctly after serialization.
   *
   * @param obj the object to serialize and deserialize
   * @return the deserialized object
   * @throws Exception if an error occurs during serialization or deserialization
   */
  @SuppressWarnings("unchecked")
  static <T> T roundTrip(T obj) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(obj);
    }
    try (ObjectInputStream ois =
        new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
      return (T) ois.readObject();
    }
  }

  private Serialization() {}
}
