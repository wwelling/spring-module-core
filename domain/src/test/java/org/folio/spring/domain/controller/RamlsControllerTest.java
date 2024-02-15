package org.folio.spring.domain.controller;

import static org.folio.spring.domain.utility.mock.MockMvcRequest.APPLICATION_JSON;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.APPLICATION_STREAM;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.JSON_ARRAY;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.JSON_OBJECT;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.NO_HEADERS;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.OKAPI_HEADERS;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.OKAPI_HEADERS_NO_URL;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.PLAIN_BODY;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.SUCCESS;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.TEXT_PLAIN;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.appendBody;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.appendHeaders;
import static org.folio.spring.domain.utility.mock.MockMvcRequest.invokeRequestBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.stream.Stream;
import org.folio.spring.domain.service.RamlsService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RamlsControllerTest {

  private static final String RAMLS_PATH = "/_/ramls";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private RamlsService ramlsService;

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForGet")
  void getWorksTest(HttpHeaders headers, String contentType, String accept, String body, int status) throws Exception {
    lenient().when(ramlsService.getRamlByPath(anyString(), anyString())).thenReturn(SUCCESS);

    MockHttpServletRequestBuilder request = appendHeaders(get(RAMLS_PATH), headers, contentType, accept);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(print()).andExpect(status().is(status)).andReturn();

    if (status == 200) {
      String contentTypeOnly = result.getResponse().getContentType().replaceAll(";.*$", "");

      // @fixme "application/json" is always returned even if Accept is asking for something different. Is this inteded behavior?
      assertEquals(APPLICATION_JSON, contentTypeOnly);
      assertEquals(JSON_ARRAY, result.getResponse().getContentAsString());
    }
  }

  @ParameterizedTest
  @MethodSource("provideDeletePatchPostPut")
  void createNonPostFailsTest(Method method, HttpHeaders headers, String contentType, String accept, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(RAMLS_PATH, method, headers, contentType, accept, body))
      .andDo(print()).andExpect(status().is(status));
  }

  /**
   * Helper function for parameterized test providing DELETE, PATCH, POST, and PUT.
   *
   * @return The arguments array.
   *
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private static Stream<Arguments> provideDeletePatchPostPut() throws NoSuchMethodException, SecurityException {
    Method delete = MockMvcRequestBuilders.class.getDeclaredMethod("delete", URI.class);
    Method patch = MockMvcRequestBuilders.class.getDeclaredMethod("patch", URI.class);
    Method post = MockMvcRequestBuilders.class.getDeclaredMethod("post", URI.class);
    Method put = MockMvcRequestBuilders.class.getDeclaredMethod("put", URI.class);

    return Stream.of(
      Arguments.of(delete, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(delete, OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(delete, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(delete, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(delete, OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(delete, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(delete, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(delete, OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(delete, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(delete, NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(delete, NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(delete, NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(delete, NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(delete, NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(delete, NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(delete, NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(delete, NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(delete, NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),

      Arguments.of(patch, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(patch, OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(patch, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(patch, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(patch, OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(patch, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(patch, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(patch, OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(patch, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(patch, NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(patch, NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(patch, NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(patch, NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(patch, NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(patch, NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(patch, NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(patch, NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(patch, NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),

      Arguments.of(post, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(post, OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(post, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(post, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(post, OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(post, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(post, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(post, OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(post, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(post, NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(post, NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(post, NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(post, NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(post, NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(post, NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(post, NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(post, NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(post, NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),

      Arguments.of(put, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(put, OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(put, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(put, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(put, OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(put, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(put, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(put, OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(put, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(put, NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(put, NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(put, NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(put, NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(put, NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(put, NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(put, NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(put, NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(put, NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405)
    );
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for create end points.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @return The arguments array.
   *
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private static Stream<Arguments> provideHeadersBodyStatusForGet() throws NoSuchMethodException, SecurityException {
    HttpHeaders okapiHeadersWithPath = new HttpHeaders();
    okapiHeadersWithPath.addAll(OKAPI_HEADERS);
    okapiHeadersWithPath.add("path", "/path");

    return Stream.of(
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 200),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 200),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 200),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 200),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 200),

      Arguments.of(okapiHeadersWithPath, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 200),
      Arguments.of(okapiHeadersWithPath, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 200),
      Arguments.of(okapiHeadersWithPath, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 200),
      Arguments.of(okapiHeadersWithPath, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 200),
      Arguments.of(okapiHeadersWithPath, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 200),
      Arguments.of(okapiHeadersWithPath, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 200),
      Arguments.of(okapiHeadersWithPath, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 200),
      Arguments.of(okapiHeadersWithPath, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 200),
      Arguments.of(okapiHeadersWithPath, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 200),

      Arguments.of(OKAPI_HEADERS_NO_URL, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 400),
      Arguments.of(OKAPI_HEADERS_NO_URL, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 400),

      Arguments.of(NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 400),
      Arguments.of(NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 400),
      Arguments.of(NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 400),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 400),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 400),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 400),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 400),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 400),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 400)
    );
  }

}
