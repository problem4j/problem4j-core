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

import static org.assertj.core.api.Assertions.assertThat;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProblemMapperTest {

  private ProblemMapper mapper;

  @BeforeEach
  void beforeEach() {
    mapper = ProblemMapper.create();
  }

  @Test
  void givenDetailWithMessageInterpolation_shouldCreateProblemObject() {
    @ProblemMapping(
        type = "https://example.org/probs/tests",
        title = "Test problem",
        status = 400,
        detail = "failed: {message}")
    class MessageException extends RuntimeException {
      MessageException(String message) {
        super(message);
      }
    }
    MessageException ex = new MessageException("boom");

    Problem problem = mapper.toProblemBuilder(ex).build();

    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type("https://example.org/probs/tests")
                .title("Test problem")
                .status(400)
                .detail("failed: boom")
                .build());
  }

  @Test
  void givenDetailWithPrivateFieldInterpolation_shouldCreateProblemObject() {
    @ProblemMapping(
        type = "https://example.org/probs/private",
        title = "Private field problem",
        status = 422,
        detail = "value: {secret}")
    class PrivateFieldException extends RuntimeException {

      private final String secret;

      PrivateFieldException(String message, String secret) {
        super(message);
        this.secret = secret;
      }
    }
    PrivateFieldException ex = new PrivateFieldException("ignored", "s3cr3t");

    Problem problem = mapper.toProblemBuilder(ex).build();

    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type("https://example.org/probs/private")
                .title("Private field problem")
                .status(422)
                .detail("value: s3cr3t")
                .build());
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "TRACE-123, https://example.org/probs/ctx/TRACE-123, Ctx TRACE-123, ctx:TRACE-123 field:v, https://example.org/instance/TRACE-123",
        "null,      https://example.org/probs/ctx/,          'Ctx ',        ctx: field:v,          https://example.org/instance/"
      },
      nullValues = "null")
  void givenTraceIdInterpolation_shouldCreateProblemObject(
      String traceId,
      String expectedType,
      String expectedTitle,
      String expectedDetail,
      String expectedInstance) {
    @ProblemMapping(
        type = "https://example.org/probs/ctx/{context.traceId}",
        title = "Ctx {context.traceId}",
        status = 418,
        detail = "ctx:{context.traceId} field:{value}",
        instance = "https://example.org/instance/{context.traceId}")
    class ContextException extends RuntimeException {

      private final String value;

      ContextException(String value) {
        super("ctx");
        this.value = value;
      }
    }
    ContextException ex = new ContextException("v");

    Problem problem =
        mapper.toProblemBuilder(ex, ProblemContext.create().put("traceId", traceId)).build();

    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type(expectedType)
                .title(expectedTitle)
                .status(418)
                .detail(expectedDetail)
                .instance(expectedInstance)
                .build());
  }

  @Test
  void givenNullPrivateFields_shouldOmitExtensions() {
    @ProblemMapping(
        type = "https://example.org/probs/ext",
        title = "Extensions problem",
        status = 422,
        detail = "some detail",
        extensions = {"secret", "other"})
    class ExtensionsException extends RuntimeException {

      private final String secret;
      private final @Nullable Integer other;

      ExtensionsException(String secret, @Nullable Integer other) {
        super("ignored");
        this.secret = secret;
        this.other = other;
      }
    }

    ExtensionsException ex = new ExtensionsException("s", null);

    Problem problem = mapper.toProblemBuilder(ex).build();

    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type("https://example.org/probs/ext")
                .title("Extensions problem")
                .status(422)
                .detail("some detail")
                .extension("secret", "s")
                .build());
  }

  @Test
  void givenMissingPrivateFieldPlaceholder_shouldOmitInterpolation() {
    @ProblemMapping(
        type = "https://example.org/probs/multi",
        title = "Repeat",
        status = 400,
        detail = "{message} - {message} - {missing}")
    class RepeatException extends RuntimeException {
      RepeatException(String m) {
        super(m);
      }
    }
    RepeatException ex = new RepeatException("X");

    Problem problem = mapper.toProblemBuilder(ex).build();

    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type("https://example.org/probs/multi")
                .title("Repeat")
                .status(400)
                .detail("X - X - ")
                .build());
  }

  @Test
  void givenExceptionInheritance_shouldInterpolate() {
    class BaseAnnotatedException extends RuntimeException {
      BaseAnnotatedException(String m) {
        super(m);
      }
    }

    @ProblemMapping(type = "https://example.org/probs/base", title = "Base {message}", status = 400)
    class BaseWithAnnotation extends BaseAnnotatedException {
      BaseWithAnnotation(String m) {
        super(m);
      }
    }

    class ChildOfAnnotated extends BaseWithAnnotation {
      ChildOfAnnotated(String m) {
        super(m);
      }
    }
    ChildOfAnnotated ex = new ChildOfAnnotated("hello");

    Problem problem = mapper.toProblemBuilder(ex).build();

    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type("https://example.org/probs/base")
                .title("Base hello")
                .status(400)
                .build());
  }

  @Test
  void givenEmptyAnnotation_shouldCreateProblemObject() {
    @ProblemMapping
    class MessageException extends RuntimeException {
      MessageException(String message) {
        super(message);
      }
    }
    MessageException ex = new MessageException("boom");

    Problem problem = mapper.toProblemBuilder(ex).build();

    assertThat(problem).isNotNull();
    assertThat(problem).isEqualTo(Problem.builder().status(0).build());
  }

  @Test
  void malformedPlaceholder_shouldNotCrash() {
    @ProblemMapping(
        type = "https://example.org",
        title = "Bad {notClosed",
        status = 400,
        detail = "d")
    class MalformedPlaceholderException extends RuntimeException {
      MalformedPlaceholderException() {
        super("i have malformed title");
      }
    }

    MalformedPlaceholderException ex = new MalformedPlaceholderException();

    Problem problem = mapper.toProblemBuilder(ex).build();
    assertThat(problem).isNotNull();
    assertThat(problem)
        .isEqualTo(
            Problem.builder()
                .type("https://example.org")
                .title("Bad {notClosed")
                .status(400)
                .detail("d")
                .build());
  }

  @Test
  void isMappingCandidate_returnsTrue_forDirectAnnotation() {
    @ProblemMapping(type = "type", title = "title")
    class DirectAnnotatedException extends RuntimeException {}

    Throwable ex = new DirectAnnotatedException();

    assertThat(mapper.isMappingCandidate(ex)).isTrue();
  }

  @Test
  void isMappingCandidate_returnsFalse_forUnannotatedException() {
    class PlainException extends RuntimeException {}

    Throwable ex = new PlainException();

    assertThat(mapper.isMappingCandidate(ex)).isFalse();
  }

  @Test
  void isMappingCandidate_returnsTrue_forInheritedAnnotation() {
    @ProblemMapping(type = "type", title = "title")
    class BaseException extends RuntimeException {}

    class SubException extends BaseException {}

    Throwable ex = new SubException();

    assertThat(mapper.isMappingCandidate(ex)).isTrue();
  }

  @Test
  void isMappingCandidate_returnsFalse_forNull() {
    assertThat(mapper.isMappingCandidate(null)).isFalse();
  }

  @Test
  void givenNullThrowable_whenToProblemBuilder_thenReturnsEmptyBuilder() {
    Problem problem = mapper.toProblemBuilder(null).build();

    assertThat(problem.getStatus()).isZero();
    assertThat(problem.getTitle()).isEqualTo(Problem.UNKNOWN_TITLE);
  }

  @Test
  void givenUnannotatedThrowable_whenToProblemBuilder_thenReturnsEmptyBuilder() {
    Problem problem = mapper.toProblemBuilder(new RuntimeException("plain")).build();

    assertThat(problem.getStatus()).isZero();
    assertThat(problem.getTitle()).isEqualTo(Problem.UNKNOWN_TITLE);
  }

  @Test
  void givenNullThrowable_whenToProblemBuilderWithContext_thenReturnsEmptyBuilder() {
    Problem problem = mapper.toProblemBuilder(null, ProblemContext.create()).build();

    assertThat(problem.getStatus()).isZero();
  }

  @Test
  void givenInvalidTypeUri_whenMapping_thenTypeIsIgnored() {

    @ProblemMapping(type = "ht tp://invalid uri", title = "Title", status = 400)
    class InvalidTypeException extends RuntimeException {
      InvalidTypeException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new InvalidTypeException()).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
    assertThat(problem.getTitle()).isEqualTo("Title");
  }

  @Test
  void givenInvalidInstanceUri_whenMapping_thenInstanceIsIgnored() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        instance = "ht tp://invalid uri")
    class InvalidInstanceException extends RuntimeException {
      InvalidInstanceException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new InvalidInstanceException()).build();

    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenTypeResolvingToEmpty_whenMapping_thenTypeIsNotSet() {

    @ProblemMapping(type = "{missing}", title = "Title", status = 400)
    class EmptyTypeException extends RuntimeException {
      EmptyTypeException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new EmptyTypeException()).build();

    assertThat(problem.getType()).isEqualTo(Problem.BLANK_TYPE);
  }

  @Test
  void givenTitleResolvingToEmpty_whenMapping_thenTitleFallsBackToDefault() {

    @ProblemMapping(type = "https://example.org/err", title = "{missing}", status = 400)
    class EmptyTitleException extends RuntimeException {
      EmptyTitleException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new EmptyTitleException()).build();

    assertThat(problem.getTitle()).isEqualTo(ProblemStatus.BAD_REQUEST.getTitle());
  }

  @Test
  void givenDetailResolvingToEmpty_whenMapping_thenDetailIsNotSet() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        detail = "{missing}")
    class EmptyDetailException extends RuntimeException {
      EmptyDetailException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new EmptyDetailException()).build();

    assertThat(problem.getDetail()).isNull();
  }

  @Test
  void givenInstanceResolvingToEmpty_whenMapping_thenInstanceIsNotSet() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        instance = "{missing}")
    class EmptyInstanceException extends RuntimeException {
      EmptyInstanceException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new EmptyInstanceException()).build();

    assertThat(problem.getInstance()).isNull();
  }

  @Test
  void givenExtensionWithBlankName_whenMapping_thenBlankNameIsSkipped() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        extensions = {"  ", "  \t  "})
    class BlankExtException extends RuntimeException {
      BlankExtException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new BlankExtException()).build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenExtensionWithEmptyStringValue_whenMapping_thenExtensionIsOmitted() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        extensions = {"emptyField"})
    class EmptyExtException extends RuntimeException {

      private final String emptyField;

      EmptyExtException() {
        super("err");
        this.emptyField = "";
      }
    }

    Problem problem = mapper.toProblemBuilder(new EmptyExtException()).build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenContextWithMissingKey_whenInterpolating_thenResolvesToEmptyString() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        detail = "trace:{context.missingKey}")
    class CtxException extends RuntimeException {
      CtxException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new CtxException(), ProblemContext.create()).build();

    assertThat(problem.getDetail()).isEqualTo("trace:");
  }

  @Test
  void givenContextKeyWithNullContext_whenInterpolating_thenResolvesToEmptyString() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        detail = "trace:{context.traceId}")
    class CtxException extends RuntimeException {
      CtxException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new CtxException()).build();

    assertThat(problem.getDetail()).isEqualTo("trace:");
  }

  @Test
  void givenNullMessage_whenInterpolating_thenMessageResolvesToEmpty() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        detail = "msg:{message}")
    class NullMsgException extends RuntimeException {
      NullMsgException() {
        super((String) null);
      }
    }

    Problem problem = mapper.toProblemBuilder(new NullMsgException()).build();

    assertThat(problem.getDetail()).isEqualTo("msg:");
  }

  @Test
  void givenZeroStatus_whenMapping_thenStatusIsNotSet() {

    @ProblemMapping(type = "https://example.org/err", title = "Title", status = 0)
    class ZeroStatusException extends RuntimeException {
      ZeroStatusException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new ZeroStatusException()).build();

    assertThat(problem.getStatus()).isZero();
  }

  @Test
  void givenExtensionFieldInSuperclass_whenMapping_thenFieldIsResolved() {

    class BaseException extends RuntimeException {

      protected final String code;

      BaseException(String code, String message) {
        super(message);
        this.code = code;
      }
    }

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Inherited",
        status = 400,
        extensions = {"code"})
    class ChildException extends BaseException {
      ChildException(String code, String message) {
        super(code, message);
      }
    }

    Problem problem = mapper.toProblemBuilder(new ChildException("ERR-1", "fail")).build();

    assertThat(problem.getExtensionValue("code")).isEqualTo("ERR-1");
  }

  @Test
  void givenExtensionFieldThatDoesNotExist_whenMapping_thenExtensionIsOmitted() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "No field",
        status = 400,
        extensions = {"nonexistent"})
    class NoFieldException extends RuntimeException {
      NoFieldException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new NoFieldException()).build();

    assertThat(problem.getExtensions()).isEmpty();
  }

  @Test
  void givenEmptyPlaceholder_whenInterpolating_thenResolvesToEmpty() {

    @ProblemMapping(
        type = "https://example.org/err",
        title = "Title",
        status = 400,
        detail = "before{}after")
    class EmptyPlaceholderException extends RuntimeException {
      EmptyPlaceholderException() {
        super("err");
      }
    }

    Problem problem = mapper.toProblemBuilder(new EmptyPlaceholderException()).build();

    assertThat(problem.getDetail()).isEqualTo("before{}after");
  }
}
