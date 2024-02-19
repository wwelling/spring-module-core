package org.folio.spring.web.utility;

import static org.folio.spring.web.utility.RequestHeaderUtility.APP_RAML;
import static org.folio.spring.web.utility.RequestHeaderUtility.APP_SCHEMA;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    boolean result = RequestHeaderUtility.unsupportedAccept(null);

    assertEquals(false, result);
  }

  @Test
  void unsupportedAcceptHandlesNullListTest() {
    boolean result = RequestHeaderUtility.unsupportedAccept(null);

    assertEquals(false, result);
  }

  @Test
  void unsupportedAcceptHandlesEmptyAcceptTest() {
    boolean result = RequestHeaderUtility.unsupportedAccept(TEXT_PLAIN_TYPE);

    assertEquals(true, result);
  }

  @ParameterizedTest
  @MethodSource("provideMatchContentTypes")
  void unsupportedAcceptWorksTest(String contentType, MediaType mediaType1, MediaType mediaType2, MediaType mediaType3, boolean expect) {
    boolean result = RequestHeaderUtility.unsupportedAccept(contentType, mediaType1, mediaType2, mediaType3);

    assertEquals(expect, result);
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
   *     - boolean expect (the expected boolean result on match or miss).
   */
  private static Stream<Arguments> provideMatchContentTypes() {
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
