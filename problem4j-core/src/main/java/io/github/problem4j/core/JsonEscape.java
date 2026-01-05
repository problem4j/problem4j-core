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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for escaping strings for safe inclusion in JSON.
 *
 * <p>Replaces control characters, special characters, and Unicode ranges with appropriate escape
 * sequences to produce valid JSON string literals.
 */
final class JsonEscape {

  /** Mapping of characters to their JSON escape sequences. */
  private static final Map<Character, String> REPLACEMENTS = new HashMap<>();

  static {
    REPLACEMENTS.put('"', "\\\"");
    REPLACEMENTS.put('\\', "\\\\");
    REPLACEMENTS.put('\b', "\\b");
    REPLACEMENTS.put('\f', "\\f");
    REPLACEMENTS.put('\n', "\\n");
    REPLACEMENTS.put('\r', "\\r");
    REPLACEMENTS.put('\t', "\\t");
    REPLACEMENTS.put('/', "\\/");
  }

  /** Private constructor to prevent instantiation. */
  private JsonEscape() {}

  /**
   * Escapes the given string for inclusion in JSON.
   *
   * <p>Replaces control characters, special characters, and certain Unicode ranges with appropriate
   * escape sequences.
   *
   * @param value the string to escape
   * @return the escaped string
   */
  static String escape(String value) {
    StringBuilder result = new StringBuilder();

    for (char character : value.toCharArray()) {
      if (shouldBeReplaced(character)) {
        replace(result, character);
      } else if (shouldBeHexed(character)) {
        hex(result, character);
      } else {
        result.append(character);
      }
    }

    return result.toString();
  }

  /**
   * Determines if the character should be replaced using a predefined escape sequence.
   *
   * @param character the character to check
   * @return true if the character has a predefined replacement
   */
  private static boolean shouldBeReplaced(char character) {
    return REPLACEMENTS.containsKey(character);
  }

  /**
   * Appends the JSON escape sequence for a character that has a predefined replacement.
   *
   * @param result the StringBuilder to append to
   * @param character the character to escape
   */
  private static void replace(StringBuilder result, char character) {
    result.append(REPLACEMENTS.get(character));
  }

  /**
   * Determines if the character should be escaped using a Unicode hexadecimal escape.
   *
   * <p>Characters that are control characters or in certain Unicode ranges are escaped.
   *
   * @param character the character to check
   * @return true if the character should be represented as a Unicode escape
   */
  private static boolean shouldBeHexed(char character) {
    return character <= '\u001F'
        || character >= '\u007F' && character <= '\u009F'
        || character >= '\u2000' && character <= '\u20FF';
  }

  /**
   * Appends the Unicode hexadecimal escape sequence for the given character to the result.
   *
   * <p>The escape sequence is in the format {@code "\\uXXXX"}, where {@code "XXXX"} is the
   * uppercase hexadecimal representation of the character code, padded with leading zeros to four
   * digits.
   *
   * @param result the StringBuilder to append the escape sequence to
   * @param character the character to be escaped
   */
  private static void hex(StringBuilder result, char character) {
    String hexedCharacter = Integer.toHexString(character).toUpperCase();
    result.append("\\u");
    for (int i = 0; i < 4 - hexedCharacter.length(); i++) {
      result.append('0');
    }
    result.append(hexedCharacter);
  }
}
