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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Some of the tests in this class may appear trivial or unnecessary. They are intentionally
 * included to explore and validate the behavior of various code coverage analysis tools. These
 * tests help ensure that the coverage reports correctly reflect different execution paths, edge
 * cases, and instrumentation scenarios.
 */
class ProblemExceptionTest {

  @Test
  void
      givenProblemWithAllFields_whenCreatingException_thenMessageWithAllFieldsWithProperFormatting() {
    Problem problem =
        Problem.builder()
            .type("https://example.com/error")
            .title("Validation Error")
            .detail("The request body is invalid")
            .status(400)
            .build();

    ProblemException exception = new ProblemException(problem);

    assertEquals(
        "Validation Error: The request body is invalid (code: 400)", exception.getMessage());
  }

  @Test
  void
      givenProblemWithTitleAndDetail_whenCreatingException_thenMessageWithTitleAndDetailWithColon() {
    Problem problem =
        Problem.builder()
            .title("Not Found")
            .detail("The requested resource does not exist")
            .build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Not Found: The requested resource does not exist", exception.getMessage());
  }

  @Test
  void givenProblemWithTitleAndStatus_whenCreatingException_thenMessageWithTitleAndStatus() {
    Problem problem = Problem.builder().title("Unauthorized").status(401).build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Unauthorized (code: 401)", exception.getMessage());
  }

  @Test
  void givenProblemWithDetailAndStatus_whenCreatingException_thenMessageWithDetailAndStatus() {
    Problem problem = Problem.builder().detail("Token has expired").status(401).build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Unauthorized: Token has expired (code: 401)", exception.getMessage());
  }

  @Test
  void givenProblemWithTitle_whenCreatingException_thenMessageWithTitle() {
    Problem problem = Problem.builder().title("Internal Server Error").build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Internal Server Error", exception.getMessage());
  }

  @Test
  void givenProblemWithDetail_whenCreatingException_thenMessageWithDetail() {
    Problem problem = Problem.builder().detail("Something went wrong").build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Something went wrong", exception.getMessage());
  }

  @Test
  void givenProblemWithStatus_whenCreatingException_thenMessageWithStatus() {
    Problem problem = Problem.builder().status(500).build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Internal Server Error (code: 500)", exception.getMessage());
  }

  @Test
  void givenProblemWithEmptyStringFields_whenCreatingException_thenMessageIsNull() {
    Problem problem = Problem.builder().title("").detail("").build();

    ProblemException exception = new ProblemException(problem);

    assertNull(exception.getMessage());
  }

  @Test
  void givenProblemWithZeroStatus_whenCreatingException_thenStatusNotIncludedInMessage() {
    Problem problem =
        Problem.builder().title("Error").detail("Something happened").status(0).build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Error: Something happened", exception.getMessage());
  }

  @Test
  void givenProblemWithNegativeStatus_whenCreatingException_thenStatusIncludedInMessage() {
    Problem problem = Problem.builder().title("Custom Error").status(-1).build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Custom Error (code: -1)", exception.getMessage());
  }

  @Test
  void givenProblemWithLargeStatus_whenCreatingException_thenStatusIncludedInMessage() {
    Problem problem = Problem.builder().detail("Custom protocol error").status(999).build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("Custom protocol error (code: 999)", exception.getMessage());
  }

  @Test
  void givenProblemWithWhitespaceTitle_whenCreatingException_thenTitleIncludedInMessage() {
    Problem problem = Problem.builder().title("   ").detail("Error occurred").build();

    ProblemException exception = new ProblemException(problem);

    assertEquals("   : Error occurred", exception.getMessage());
  }

  @Test
  void givenProblemWithSpecialCharacters_whenCreatingException_thenSpecialCharactersPreserved() {
    Problem problem =
        Problem.builder()
            .title("Validation Error: Required Fields Missing")
            .detail("Fields 'name' & 'email' are required (check docs)")
            .status(422)
            .build();

    ProblemException exception = new ProblemException(problem);

    assertEquals(
        "Validation Error: Required Fields Missing: Fields 'name' & 'email' are required (check docs) (code: 422)",
        exception.getMessage());
  }

  @Test
  void givenCtor_whenCreatingProblemException_problemIsNotRecreated() {
    Problem problem = Problem.builder().title("Bad Request").status(400).build();

    ProblemException exception = new ProblemException(problem);

    assertSame(problem, exception.getProblem());
  }

  @Test
  void givenCtorWithMessage_whenCreatingProblemException_problemIsNotRecreated() {
    Problem problem = Problem.builder().title("Bad Request").status(400).build();

    ProblemException exception = new ProblemException("this is a message", problem);

    assertSame(problem, exception.getProblem());
  }

  @Test
  void givenCtorWithCause_whenCreatingProblemException_problemIsNotRecreated() {
    Problem problem = Problem.builder().title("Bad Request").status(400).build();
    Throwable cause = new RuntimeException("root cause");

    ProblemException exception = new ProblemException(problem, cause);

    assertSame(problem, exception.getProblem());
  }

  @Test
  void givenCtorWithMessageAndCause_whenCreatingProblemException_problemIsNotRecreated() {
    Problem problem = Problem.builder().title("Bad Request").status(400).build();
    Throwable cause = new RuntimeException("root cause");

    ProblemException exception = new ProblemException("this is a message", problem, cause);

    assertSame(problem, exception.getProblem());
  }

  @Test
  void givenCtorWithParameters_whenCreatingProblemException_problemIsNotRecreated() {
    Problem problem = Problem.builder().title("Bad Request").status(400).build();
    Throwable cause = new RuntimeException("root cause");

    ProblemException exception =
        new ProblemException("this is a message", problem, cause, false, true);

    assertSame(problem, exception.getProblem());
  }
}
