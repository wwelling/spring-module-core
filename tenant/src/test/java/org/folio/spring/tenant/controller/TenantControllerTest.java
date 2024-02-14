package org.folio.spring.tenant.controller;

import static org.folio.spring.tenant.utility.mock.MockMvcRequest.APPLICATION_JSON;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.APPLICATION_STREAM;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.JSON_OBJECT;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.NO_HEADERS;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.OKAPI_HEADERS;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.PLAIN_BODY;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.SUCCESS;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.TEXT_PLAIN;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.appendBody;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.appendHeaders;
import static org.folio.spring.tenant.utility.mock.MockMvcRequest.invokeRequestBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.stream.Stream;
import org.folio.spring.tenant.hibernate.HibernateSchemaService;
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
class TenantControllerTest {

  private static final String TENANT_PATH = "/_/tenant";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private HibernateSchemaService hibernateSchemaService;

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForCreate")
  void createPostWorksTest(HttpHeaders headers, String contentType, String accept, String body, int status) throws Exception {
    lenient().doNothing().when(hibernateSchemaService).createTenant(anyString());

    MockHttpServletRequestBuilder request = appendHeaders(post(TENANT_PATH), headers, contentType, accept);

    MvcResult result = mvc.perform(appendBody(request, body))
      .andDo(print()).andExpect(status().is(status)).andReturn();

    if (status == 201) {
      String contentTypeOnly = result.getResponse().getContentType().replaceAll(";.*$", "");

      // @fixme "text/plain" is always returned even if Accept is asking for something different. Is this inteded behavior?
      String acceptContentType = accept != null && !accept.equals(APPLICATION_STREAM) ? accept : TEXT_PLAIN;
 
      assertEquals(acceptContentType, contentTypeOnly);
      assertEquals(SUCCESS, result.getResponse().getContentAsString());
    }
  }

  @ParameterizedTest
  @MethodSource("provideGetPatchPut")
  void createNonPostFailsTest(Method method, HttpHeaders headers, String contentType, String accept, String body, int status) throws Exception {
    mvc.perform(invokeRequestBuilder(TENANT_PATH, method, headers, contentType, accept, body))
      .andDo(print()).andExpect(status().is(status));
  }

  @ParameterizedTest
  @MethodSource("provideHeadersBodyStatusForDelete")
  void deletePostWorksTest(HttpHeaders headers, String contentType, String accept, String body, int status) throws Exception {
    lenient().doNothing().when(hibernateSchemaService).createTenant(anyString());

    MockHttpServletRequestBuilder request = appendHeaders(delete(TENANT_PATH), headers, contentType, accept);

    mvc.perform(appendBody(request, body))
      .andDo(print()).andExpect(status().is(status));
  }

  /**
   * Helper function for parameterized test providing GET, PATCH, and PUT.
   *
   * @return The arguments array.
   *
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private static Stream<Arguments> provideGetPatchPut() throws NoSuchMethodException, SecurityException {
    Method get = MockMvcRequestBuilders.class.getDeclaredMethod("get", URI.class);
    Method patch = MockMvcRequestBuilders.class.getDeclaredMethod("patch", URI.class);
    Method put = MockMvcRequestBuilders.class.getDeclaredMethod("put", URI.class);

    return Stream.of(
      Arguments.of(get, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(get, OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(get, OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(get, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(get, OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(get, OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(get, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(get, OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(get, OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(get, NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(get, NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(get, NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 405),
      Arguments.of(get, NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 405),
      Arguments.of(get, NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 405),
      Arguments.of(get, NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 405),
      Arguments.of(get, NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 405),
      Arguments.of(get, NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 405),
      Arguments.of(get, NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 405),

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
   * @fixme the NO_HEADERS are passing, is the OKAPI Tenant header required?
   *
   * @return The arguments array.
   *
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private static Stream<Arguments> provideHeadersBodyStatusForCreate() throws NoSuchMethodException, SecurityException {
    return Stream.of(
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 201),
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 201),
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 201),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 415),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 415),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 415),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 415),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 415),

      Arguments.of(NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 201),
      Arguments.of(NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 201),
      Arguments.of(NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 201),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 415),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 415),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 415),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 415),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 415),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 415)
    );
  }

  /**
   * Helper function for parameterized test providing tests with headers, body, and status for delete end points.
   *
   * This is intended to be used for when the correct HTTP method is being used in the request.
   *
   * @fixme the NO_HEADERS are passing, is the OKAPI Tenant header required?
   *
   * @return The arguments array.
   *
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private static Stream<Arguments> provideHeadersBodyStatusForDelete() throws NoSuchMethodException, SecurityException {
    return Stream.of(
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 204),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 204),
      Arguments.of(OKAPI_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 204),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 204),
      Arguments.of(OKAPI_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 204),

      Arguments.of(NO_HEADERS, APPLICATION_JSON, APPLICATION_JSON, JSON_OBJECT, 204),
      Arguments.of(NO_HEADERS, APPLICATION_JSON, TEXT_PLAIN, JSON_OBJECT, 204),
      Arguments.of(NO_HEADERS, APPLICATION_JSON, APPLICATION_STREAM, JSON_OBJECT, 204),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, APPLICATION_JSON, PLAIN_BODY, 204),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, TEXT_PLAIN, PLAIN_BODY, 204),
      Arguments.of(NO_HEADERS, TEXT_PLAIN, APPLICATION_STREAM, PLAIN_BODY, 204),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, APPLICATION_JSON, JSON_OBJECT, 204),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, TEXT_PLAIN, JSON_OBJECT, 204),
      Arguments.of(NO_HEADERS, APPLICATION_STREAM, APPLICATION_STREAM, JSON_OBJECT, 204)
    );
  }

}
