package org.folio.spring.web.utility;

import static org.folio.spring.web.utility.RequestHeaderUtility.APP_JSON_PLUS;
import static org.folio.spring.web.utility.RequestHeaderUtility.APP_RAML;
import static org.folio.spring.web.utility.RequestHeaderUtility.APP_SCHEMA;
import static org.folio.spring.web.utility.RequestHeaderUtility.compatibleWith;
import static org.folio.spring.web.utility.RequestHeaderUtility.unsupportedAccept;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.IMAGE_GIF;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

class RequestHandlerUtilityTest {

  private static final String TEXT_PLAIN_TYPE = "text/plain";

  private static final String APP_RAML_TYPE = "application/raml+yaml";

  private static final String APP_RAML_FAKE_TYPE = "application/raml";

  private static final String APP_STAR_TYPE = "application/*";

  private static final String TEXT_STAR_TYPE = "text/*";

  private static final String STAR_TYPE = "*";

  public static final MediaType APP_FAKE = new MediaType("application", "fake");

  public static final MediaType TEST_FAKE = new MediaType("test", "fake");

  @Test
  void unsupportedAcceptHandlesNullAcceptTest() {
    assertEquals(false, unsupportedAccept(null));
  }

  @Test
  void unsupportedAcceptHandlesNullListTest() {
    assertEquals(false, unsupportedAccept(null));
  }

  @Test
  void unsupportedAcceptHandlesEmptyAcceptTest() {
    assertEquals(true, unsupportedAccept(TEXT_PLAIN_TYPE));
  }

  @ParameterizedTest
  @MethodSource("provideIsCompatibleContentTypes")
  void compatibleWithWorksTest(MediaType contentType, MediaType mediaType1, MediaType mediaType2, MediaType mediaType3, boolean expect) {
    assertEquals(expect, compatibleWith(contentType, mediaType1, mediaType2, mediaType3));
  }

  @ParameterizedTest
  @MethodSource("provideUnsupportedAcceptContentTypes")
  void unsupportedAcceptWorksTest(String contentType, MediaType mediaType1, MediaType mediaType2, MediaType mediaType3, boolean expect) {
    assertEquals(expect, unsupportedAccept(contentType, mediaType1, mediaType2, mediaType3));
  }

  /**
   * Helper function for parameterized test providing tests for a given content type for the set of three.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - String contentType (Content-Type request).
   *     - MediaType mediaType1 (the first MediaType to match).
   *     - MediaType mediaType2 (the second MediaType to match).
   *     - MediaType mediaType3 (the third MediaType to match).
   *     - boolean expect (the expected result on unsupported (true) or supported (false)).
   */
  private static Stream<Arguments> provideIsCompatibleContentTypes() {
    return Stream.of(
      Arguments.of(APPLICATION_JSON,         APPLICATION_JSON, APP_JSON_PLUS, TEXT_PLAIN, true),
      Arguments.of(APPLICATION_OCTET_STREAM, APPLICATION_JSON, APP_JSON_PLUS, TEXT_PLAIN, false),
      Arguments.of(IMAGE_GIF,                APPLICATION_JSON, APP_JSON_PLUS, TEXT_PLAIN, false),
      Arguments.of(TEXT_PLAIN,               APPLICATION_JSON, APP_JSON_PLUS, TEXT_PLAIN, true),
      Arguments.of(TEXT_HTML,                APPLICATION_JSON, APP_JSON_PLUS, TEXT_PLAIN, false),
      Arguments.of(APP_SCHEMA,               APPLICATION_JSON, APP_JSON_PLUS, TEXT_PLAIN, true)
    );
  }

  /**
   * Helper function for parameterized test providing tests for a given content type for the set of three.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - String contentType (Content-Type request).
   *     - MediaType mediaType1 (the first MediaType to match).
   *     - MediaType mediaType2 (the second MediaType to match).
   *     - MediaType mediaType3 (the third MediaType to match).
   *     - boolean expect (the expected result on unsupported (true) or supported (false)).
   */
  private static Stream<Arguments> provideUnsupportedAcceptContentTypes() {
    return Stream.of(
      Arguments.of(TEXT_PLAIN_TYPE,    APP_RAML,  APP_SCHEMA, TEXT_PLAIN, false),
      Arguments.of(APP_RAML_TYPE,      APP_RAML,  APP_SCHEMA, TEXT_PLAIN, false),
      Arguments.of(APP_STAR_TYPE,      APP_RAML,  APP_SCHEMA, TEXT_PLAIN, false),
      Arguments.of(STAR_TYPE,          APP_RAML,  APP_SCHEMA, TEXT_PLAIN, false),
      Arguments.of(APP_RAML_FAKE_TYPE, APP_RAML,  APP_SCHEMA, TEXT_PLAIN, true),
      Arguments.of(APP_RAML_TYPE,      APP_FAKE,  APP_FAKE,   TEXT_PLAIN, true),
      Arguments.of(APP_STAR_TYPE,      APP_FAKE,  APP_FAKE,   TEXT_PLAIN, false),
      Arguments.of(STAR_TYPE,          APP_FAKE,  APP_FAKE,   TEXT_PLAIN, false),
      Arguments.of(APP_RAML_TYPE,      APP_FAKE,  APP_FAKE,   TEXT_PLAIN, true),
      Arguments.of(APP_STAR_TYPE,      TEST_FAKE, TEST_FAKE,  TEXT_PLAIN, true),
      Arguments.of(STAR_TYPE,          APP_FAKE,  APP_FAKE,   TEXT_PLAIN, false),
      Arguments.of(TEXT_STAR_TYPE,     APP_FAKE,  APP_FAKE,   TEXT_PLAIN, false),
      Arguments.of(TEXT_STAR_TYPE,     APP_RAML,  APP_SCHEMA, APP_FAKE,   true)
    );
  }

}
