package org.folio.spring.test.mock;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

/**
 * A utility intended to assist in mocking MVC requests during testing, focusing on constants.
 */
public class MockMvcConstant {

  /**
   * Provide application/json Content-Type.
   */
  public static final String APP_JSON = "application/json";

  /**
   * Provide application/raml+yaml Content-Type.
   */
  public static final String APP_RAML = "application/raml+yaml";

  /**
   * Provide "application/*", mainly intended to be used as a wild card in an HTTP Accept header.
   */
  public static final String APP_STAR = "application/*";

  /**
   * Provide application/octet-stream Content-Type.
   */
  public static final String APP_STREAM = "application/octet-stream";

  /**
   * Provide empty JSON array.
   */
  public static final String JSON_ARRAY = "[]";

  /**
   * Provide empty JSON object.
   */
  public static final String JSON_OBJECT = "{}";

  /**
   * Provide OKAPI Tenant HTTP Header.
   */
  public static final String OKAPI_TENANT = "X-Okapi-Tenant";

  /**
   * Provide OKAPI Token HTTP Header.
   */
  public static final String OKAPI_TOKEN = "X-Okapi-Token";

  /**
   * Provide OKAPI URL HTTP Header.
   */
  public static final String OKAPI_URL = "X-Okapi-Url";

  /**
   * Provide a NULL string.
   * 
   * This intended to simplify the code of not having (String) null being used in the parameterized tests.
   */
  public static final String NULL_STR = null;

  /**
   * Provide a string representation of "path", generally intended to be used as a query parameter name.
   */
  public static final String PATH = "path";

  /**
   * Provide a string representation part of a URL path, generally intended to be used as a query parameter value for the path query parameter.
   */
  public static final String PATH_PART = "/path";

  /**
   * Provide text to be used as the body of plain/text Content-Type responses.
   */
  public static final String PLAIN_BODY = "Plain text.";

  /**
   * Provide an asterisk, mainly intended to be used as a wild card in an HTTP Accept header.
   */
  public static final String STAR = "*";

  /**
   * Provide basic text representing "Success", often used in FOLIO responses.
   */
  public static final String SUCCESS = "Success";

  /**
   * Provide text/other Content-Type.
   *
   * This is intended for testing "text/plain" with or without wildcards like "text/*".
   */
  public static final String TEXT_OTHER = "text/other";

  /**
   * Provide text/plain Content-Type.
   */
  public static final String TEXT_PLAIN = "text/plain";

  /**
   * Provide text/* Content-Type.
   */
  public static final String TEXT_STAR = "text/*";

  /**
   * Provide basic HTTP URL.
   */
  public static final String URL = "http://localhost/";

  /**
   * Provide generic string for use as a value.
   */
  public static final String VALUE = "value";

  /**
   * Provide the application/json Content-Type (media type) (using different name to avoid conflicts).
   */
  public static final MediaType MT_APP_JSON = MediaType.APPLICATION_JSON;

  /**
   * Provide the application/raml+yaml Content-Type (media type).
   */
  public static final MediaType MT_APP_RAML = new MediaType("application", "raml+yaml");

  /**
   * Provide the text/plain Content-Type (media type) (using different name to avoid conflicts).
   */
  public static final MediaType MT_TEXT_PLAIN = MediaType.TEXT_PLAIN;

  /**
   * Provide a set of HTTP headers containing the OKAPI Tenant, Token, and URL HTTP Headers and values.
   */
  public static final HttpHeaders OKAPI_HEAD = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_TENANT, List.of(VALUE),
    OKAPI_TOKEN, List.of(VALUE),
    OKAPI_URL, List.of(URL)
  )));

  /**
   * Provide a set of HTTP headers containing the OKAPI Tenant and Token Headers and values.
   */
  public static final HttpHeaders OKAPI_HEAD_NO_URL = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_TENANT, List.of(VALUE),
    OKAPI_TOKEN, List.of(VALUE)
  )));

  /**
   * Provide a set of HTTP headers containing only the OKAPI Tenant Header and value.
   */
  public static final HttpHeaders OKAPI_HEAD_TENANT = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_TENANT, List.of(VALUE)
  )));

  /**
   * Provide a set of HTTP headers containing the OKAPI Token Header and values.
   */
  public static final HttpHeaders OKAPI_HEAD_TOKEN = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_TOKEN, List.of(VALUE)
  )));

  /**
   * Provide a set of HTTP headers containing the OKAPI URL Header and values.
   */
  public static final HttpHeaders OKAPI_HEAD_URL = new HttpHeaders(CollectionUtils.toMultiValueMap(Map.of(
    OKAPI_URL, List.of(URL)
  )));

  /**
   * Provide an empty set of HTTP headers.
   */
  public static final HttpHeaders NO_HEAD = new HttpHeaders();

  /**
   * Provide an empty set of parameters.
   *
   * HttpHeaders provides a cleanly initialized MultiValueMap for use as an empty map.
   */
  public static final MultiValueMap<String, String> NO_PARAM = new HttpHeaders();

  /**
   * Provide a set of parameters containing the path parameter and its values.
   */
  public static final MultiValueMap<String, String> PATH_PARAM = CollectionUtils.toMultiValueMap(Map.of(PATH, List.of(PATH_PART))); 

}
