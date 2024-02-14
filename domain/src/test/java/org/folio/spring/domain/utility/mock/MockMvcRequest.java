package org.folio.spring.domain.utility.mock;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.CollectionUtils;

public class MockMvcRequest {

  public static final String APPLICATION_JSON = "application/json";
  public static final String APPLICATION_STREAM = "application/octet-stream";
  public static final String OKAPI_TENANT = "X-Okapi-Tenant";
  public static final String OKAPI_TOKEN = "X-Okapi-Token";
  public static final String OKAPI_URL = "X-Okapi-Url";
  public static final String TEXT_PLAIN = "text/plain";
  public static final String VALUE = "value";
  public static final String JSON_ARRAY = "[]";
  public static final String JSON_OBJECT = "{}";
  public static final String PLAIN_BODY = "Plain text.";
  public static final String SUCCESS = "Success";

  public static final HttpHeaders OKAPI_HEADERS = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_TENANT, List.of(VALUE),
    OKAPI_TOKEN, List.of(VALUE),
    OKAPI_URL, List.of(VALUE)
  )));

  public static final HttpHeaders OKAPI_HEADERS_NO_URL = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_TENANT, List.of(VALUE),
    OKAPI_TOKEN, List.of(VALUE)
  )));

  public static final HttpHeaders NO_HEADERS = new HttpHeaders();

  /**
   * Construct the mock request builder.
   *
   * @param path The URI path.
   * @param method The request method to invoke.
   * @param headers The HTTP headers.
   * @param contentType The HTTP Content-Type header.
   * @param accept The HTTP Accept header.
   * @param body The payload body.
   *
   * @return The constructed mock request builder.
   *
   * @throws Exception
   */
  public static MockHttpServletRequestBuilder invokeRequestBuilder(String path, Method method, HttpHeaders headers, String contentType, String accept, String body) throws Exception {
    MockHttpServletRequestBuilder request = (MockHttpServletRequestBuilder) method.invoke(null, URI.create(path));
    request = appendHeaders(request, headers, contentType, accept);
    return appendBody(request, body);
  }

  /**
   * Optionally append payload body to the mock request builder.
   *
   * @param request The mock request builder to append to.
   * @param body The payload body.
   *
   * @return The potentially updated mock request builder.
   *
   * @throws Exception
   */
  public static MockHttpServletRequestBuilder appendBody(MockHttpServletRequestBuilder request, String body) throws Exception {
    return (body == null) ? request : request.content(body);
  }

  /**
   * Optionally append HTTP headers to the mock request builder.
   *
   * @param request The mock request builder to append to.
   * @param headers The HTTP headers.
   * @param contentType The HTTP Content-Type header.
   * @param accept The HTTP Accept header.
   *
   * @return The potentially updated mock request builder.
   *
   * @throws Exception
   */
  public static MockHttpServletRequestBuilder appendHeaders(MockHttpServletRequestBuilder request, HttpHeaders headers, String contentType, String accept) throws Exception {
    if (headers != null) {
      request = request.headers(headers);
    }

    if (contentType != null) {
      request = request.contentType(contentType);
    }

    if (accept != null) {
      request = request.accept(accept);
    }

    return request;
  }

}
