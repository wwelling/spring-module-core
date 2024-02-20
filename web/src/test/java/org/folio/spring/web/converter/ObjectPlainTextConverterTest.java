package org.folio.spring.web.converter;

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
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ObjectPlainTextConverterTest {

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

  @Test
  void addDefaultHeadersWorksTest() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    assertEquals(0, headers.size());

    objectPlainTextConverter.addDefaultHeaders(headers, VALUE, TEXT_PLAIN);
    assertEquals(2, headers.size());
    assertEquals(VALUE.length(), headers.getContentLength());
    assertTrue(compatibleWith(headers.getContentType(), TEXT_PLAIN));
  }

  @Test
  void addDefaultHeadersWorksWithJsonTest() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    assertEquals(0, headers.size());

    objectPlainTextConverter.addDefaultHeaders(headers, VALUE, APPLICATION_JSON);
    assertEquals(2, headers.size());
    assertEquals(VALUE.length(), headers.getContentLength());
    assertTrue(compatibleWith(headers.getContentType(), APPLICATION_JSON));
  }

  @Test
  void writeInternalWorksTest() throws IOException {
    MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
    HttpHeaders headers = new HttpHeaders();
    headers.setAcceptCharset(new ArrayList<>(Charset.availableCharsets().values()));
    assertEquals(1, headers.size());

    setField(outputMessage, "headers", headers);

    objectPlainTextConverter.writeInternal(VALUE, outputMessage);
    assertEquals(VALUE, outputMessage.getBody().toString());
  }

  @Test
  void writeInternalWorksWithNullAcceptCharsetTest() throws IOException {
    MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
    HttpHeaders headers = new HttpHeaders();
    assertEquals(0, headers.size());

    setField(outputMessage, "headers", headers);
    setField(objectPlainTextConverter, "writeAcceptCharset", true);

    objectPlainTextConverter.writeInternal(VALUE, outputMessage);
    assertEquals(VALUE, outputMessage.getBody().toString());
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

}
