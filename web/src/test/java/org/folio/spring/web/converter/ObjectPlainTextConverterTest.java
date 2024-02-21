package org.folio.spring.web.converter;

import static org.folio.spring.test.mock.MockMvcConstant.MT_APP_SCHEMA;
import static org.folio.spring.test.mock.MockMvcConstant.MT_APP_STAR;
import static org.folio.spring.test.mock.MockMvcConstant.MT_NULL;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.folio.spring.web.converter.ObjectPlainTextConverter.DEFAULT_CHARSET;
import static org.folio.spring.web.utility.RequestHeaderUtility.compatibleWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.invokeSetterMethod;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ObjectPlainTextConverterTest {

  private static final List<Charset> AVAILABLE_CHARSETS = new ArrayList<>(Charset.availableCharsets().values());

  private static final List<Charset> NULL_CHARSETS = null;

  @Mock
  private MethodParameter parameter;

  @Mock
  private NativeWebRequest nativeWebRequest;

  private ObjectPlainTextConverter objectPlainTextConverter;

  @BeforeEach
  void beforeEach() {
    objectPlainTextConverter = new ObjectPlainTextConverter();
  }

  @Test
  void initializedToDefaultCharsetTest() {
    assertEquals(DEFAULT_CHARSET, objectPlainTextConverter.getDefaultCharset());
  }

  @Test
  void initializedToCustomDefaultCharsetTest() {
    Charset charset = StandardCharsets.UTF_8;

    objectPlainTextConverter = new ObjectPlainTextConverter(charset);

    assertEquals(charset, objectPlainTextConverter.getDefaultCharset());

    // In case the default gets changed and this test is not updated.
    assertNotEquals(DEFAULT_CHARSET, charset);
  }

  @Test
  void setWriteAcceptCharsetWorksTest() {
    setField(objectPlainTextConverter, "writeAcceptCharset", false);

    invokeSetterMethod(objectPlainTextConverter, "setWriteAcceptCharset", true);

    assertEquals(true, getField(objectPlainTextConverter, "writeAcceptCharset"));
  }

  @ParameterizedTest
  @ValueSource(classes = { Object.class })
  void supportsRetunsTrueTest(Class clazz) {
    assertTrue(objectPlainTextConverter.supports(clazz));
  }

  @ParameterizedTest
  @ValueSource(classes = { String.class, Integer.class, List.class })
  void supportsRetunsFalseTest(Class clazz) {
    assertFalse(objectPlainTextConverter.supports(clazz));
  }

  @Test
  void readInternalWorksTest() throws IOException {
    MockClientHttpResponse inputMessage = new MockClientHttpResponse(VALUE.getBytes(), 200);

    String result = (String) objectPlainTextConverter.readInternal(getClass(), inputMessage);
    assertEquals(VALUE, result);
  }

  @Test
  void readInternalThrowsIllegalStateExceptionTest() throws IOException {
    MockClientHttpResponse inputMessage = new MockClientHttpResponse(VALUE.getBytes(), 200);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(TEXT_PLAIN);
    assertEquals(1, headers.size());

    setField(inputMessage, "headers", headers);
    setField(objectPlainTextConverter, "defaultCharset", null);

    Assertions.assertThrows(IllegalStateException.class, () -> {
      objectPlainTextConverter.readInternal(getClass(), inputMessage);
    });
  }

  @Test
  void getContentLengthWorksTest() throws IOException {
    long result = objectPlainTextConverter.getContentLength(VALUE, TEXT_PLAIN);
    assertEquals(VALUE.length(), (int) result);
  }

  @ParameterizedTest
  @MethodSource("provideAddDefaultHeader")
  void addDefaultHeadersWorks(HttpHeaders headers, MediaType type, MediaType expect) throws IOException {
    objectPlainTextConverter.addDefaultHeaders(headers, VALUE, type);

    assertEquals(2, headers.size());
    assertEquals(VALUE.length(), headers.getContentLength());
    assertTrue(compatibleWith(headers.getContentType(), expect));
  }

  @ParameterizedTest
  @MethodSource("provideWriteInternal")
  void writeInternalWorks(List<Charset> charset, MediaType contentType, boolean writeAcceptCharset) throws IOException {
    HttpHeaders headers = new HttpHeaders();
    MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();

    if (charset != null) {
      headers.setAcceptCharset(charset);
    }

    if (contentType != null) {
      headers.setContentType(contentType);
    }

    setField(outputMessage, "headers", headers);
    setField(objectPlainTextConverter, "writeAcceptCharset", writeAcceptCharset);

    objectPlainTextConverter.writeInternal(VALUE, outputMessage);
    assertEquals(VALUE, outputMessage.getBody().toString());
    assertEquals(contentType, outputMessage.getHeaders().getContentType());
  }

  @Test
  void getAcceptedCharsetsWorksTest() throws IOException {
    setField(objectPlainTextConverter, "availableCharsets", new ArrayList<>());

    List<Charset> response = objectPlainTextConverter.getAcceptedCharsets();

    assertNotNull(response);
    assertEquals(0, response.size());
  }

  @Test
  void getAcceptedCharsetsWorksWhenNullTest() throws IOException {
    setField(objectPlainTextConverter, "availableCharsets", null);

    assertNotNull(objectPlainTextConverter.getAcceptedCharsets());
  }

  /**
   * Helper function for parameterized test providing tests for the add default header tests.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - HttpHeaders headers (Must be a new instance due to modifications by test).
   *     - MediaType type (the type to pass to the addDefaultHeaders()).
   *     - MediaType expect (the MediaType to match or be compatible with).
   */
  private static Stream<Arguments> provideAddDefaultHeader() {
    HttpHeaders headers1 = new HttpHeaders();
    HttpHeaders headers2 = new HttpHeaders();
    HttpHeaders headers3 = new HttpHeaders();
    HttpHeaders headers4 = new HttpHeaders();
    HttpHeaders headers5 = new HttpHeaders();
    HttpHeaders headers6 = new HttpHeaders();
    headers1.setContentType(TEXT_PLAIN);
    headers2.setContentType(APPLICATION_JSON);
    headers3.setContentType(MT_APP_SCHEMA);
    headers4.setContentType(TEXT_PLAIN);
    headers5.setContentType(APPLICATION_JSON);
    headers6.setContentType(MT_APP_SCHEMA);

    return Stream.of(
      Arguments.of(headers1,          MT_NULL,          TEXT_PLAIN),
      Arguments.of(headers2,          MT_NULL,          APPLICATION_JSON),
      Arguments.of(headers3,          MT_NULL,          MT_APP_SCHEMA),
      Arguments.of(headers4,          APPLICATION_JSON, TEXT_PLAIN),
      Arguments.of(headers5,          APPLICATION_JSON, APPLICATION_JSON),
      Arguments.of(headers6,          APPLICATION_JSON, MT_APP_SCHEMA),
      Arguments.of(new HttpHeaders(), MT_NULL,          TEXT_PLAIN),
      Arguments.of(new HttpHeaders(), MT_APP_STAR,      TEXT_PLAIN),
      Arguments.of(new HttpHeaders(), APPLICATION_JSON, APPLICATION_JSON),
      Arguments.of(new HttpHeaders(), MT_APP_SCHEMA,    MT_APP_SCHEMA)
    );
  }

  /**
   * Helper function for parameterized test providing tests for the write internal tests.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - List<Charset> charset (the default charset to use)).
   *     - MediaType contentType (the Content-Type to use).
   *     - boolean writeAcceptCharset (the value of writeAcceptCharset in objectPlainTextConverter).
   */
  private static Stream<Arguments> provideWriteInternal() {
    return Stream.of(
      Arguments.of(AVAILABLE_CHARSETS, APPLICATION_JSON, false),
      Arguments.of(NULL_CHARSETS,      MT_APP_SCHEMA,    false),
      Arguments.of(AVAILABLE_CHARSETS, APPLICATION_JSON, true),
      Arguments.of(NULL_CHARSETS,      MT_APP_SCHEMA,    true),
      Arguments.of(AVAILABLE_CHARSETS, MT_NULL,          false),
      Arguments.of(NULL_CHARSETS,      MT_NULL,          false),
      Arguments.of(AVAILABLE_CHARSETS, MT_NULL,          true),
      Arguments.of(NULL_CHARSETS,      MT_NULL,          true)
    );
  }

}
