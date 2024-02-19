package org.folio.spring.test.mock;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.MultiValueMap;

/**
 * A utility intended to assist in mocking MVC requests during testing, focusing on request methods.
 */
public class MockMvcRequest {

  /**
   * Private initializer as per java:S1118.
   */
  private MockMvcRequest() {
  }

  /**
   * Construct the mock request builder.
   *
   * @param path The URI path.
   * @param method The request method to invoke.
   * @param headers The HTTP headers.
   * @param contentType The HTTP Content-Type header.
   * @param accept The HTTP Accept header.
   * @param parameters The query parameters.
   * @param body The payload body.
   *
   * @return The constructed mock request builder.
   *
   * @throws Exception Problems thrown by MockHttpServletRequestBuilder.
   */
  public static MockHttpServletRequestBuilder invokeRequestBuilder(String path, Method method, HttpHeaders headers, String contentType, String accept, MultiValueMap<String, String> parameters, String body) throws Exception {
    MockHttpServletRequestBuilder request = (MockHttpServletRequestBuilder) method.invoke(null, URI.create(path));
    request = appendHeaders(request, headers, contentType, accept);
    request = appendParameters(request, parameters);
    return appendBody(request, body);
  }

  /**
   * Optionally append payload body to the mock request builder.
   *
   * @param request The mock request builder to append to.
   * @param body The payload body.
   *
   * @return The potentially updated mock request builder.
   */
  public static MockHttpServletRequestBuilder appendBody(MockHttpServletRequestBuilder request, String body) {
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
   */
  public static MockHttpServletRequestBuilder appendHeaders(MockHttpServletRequestBuilder request, HttpHeaders headers, String contentType, String accept) {
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

  /**
   * Optionally append query parameters the mock request builder.
   *
   * @param request The mock request builder to append to.
   * @param parameters The map of all of the parameters.
   *
   * @return The potentially updated mock request builder.
   */
  public static MockHttpServletRequestBuilder appendParameters(MockHttpServletRequestBuilder request, MultiValueMap<String, String> parameters) {
    if (parameters == null) {
      return request;
    }

    return request.params(parameters);
  }

  /**
   * Build arguments stream based on a specific set of parameters.
   *
   * @param headers The HTTP headers; this is applied to every single Argument set.
   * @param contentTypes An array of Content-Type request strings.
   * @param accepts An array of Content-Type strings that the response is expected to be in.
   * @param mediaTypes An array of the Content-Type that the response is actually in.
   * @param params An array of query parameters of type (MultiValueMap of Strings) cast to an Object.
   * @param bodys An array of the request body that must exactly match the contentType array length.
   * @param status The response status code returned; this is applied to every single Argument set.
   *
   * @return The stream of arguments.
   */
  public static Stream<Arguments> buildArguments1(HttpHeaders headers, String[] contentTypes, String[] accepts, MediaType[] mediaTypes, Object[] params, String[] bodys, int status) {
    Builder<Arguments> builder = Stream.builder();

    for (int i = 0; i < contentTypes.length; i++) {
      for (String accept : accepts) {
        for (MediaType mediaType : mediaTypes) {
          for (Object param : params) {
            builder.add(Arguments.of(headers, contentTypes[i], accept, mediaType, param, bodys[i], status));
          }
        }
      }
    }

    return builder.build();
  }

  /**
   * Build arguments stream based on a specific set of parameters.
   *
   * @param method The (reflection) request method to call; this is applied to every single Argument set.
   * @param headers The HTTP headers; this is applied to every single Argument set.
   * @param contentTypes An array of Content-Type request strings.
   * @param accepts An array of Content-Type strings that the response is expected to be in.
   * @param mediaTypes An array of the Content-Type that the response is actually in.
   * @param params An array of query parameters of type (MultiValueMap of Strings) cast to an Object.
   * @param bodys An array of the request body that must exactly match the contentType array length.
   * @param status The response status code returned; this is applied to every single Argument set.
   *
   * @return The stream of arguments.
   */
  public static Stream<Arguments> buildArguments2(Method method, HttpHeaders headers, String[] contentTypes, String[] accepts, MediaType[] mediaTypes, Object[] params, String[] bodys, int status) {
    Builder<Arguments> builder = Stream.builder();

    for (int i = 0; i < contentTypes.length; i++) {
      for (String accept : accepts) {
        for (MediaType mediaType : mediaTypes) {
          for (Object param : params) {
            builder.add(Arguments.of(method, headers, contentTypes[i], accept, mediaType, param, bodys[i], status));
          }
        }
      }
    }

    return builder.build();
  }

}
