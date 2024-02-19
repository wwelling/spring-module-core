package org.folio.spring.test.mock;

import java.lang.reflect.Method;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * A utility intended to assist in mocking MVC requests during testing, focusing on reflections.
 */
public class MockMvcReflection {

  private static final Logger logger = LoggerFactory.getLogger(MockMvcReflection.class);

  /**
   * Provide method for performing HTTP DELETE in MVC tests. 
   */
  public static final Method DELETE;

  /**
   * Provide method for performing HTTP GET in MVC tests. 
   */
  public static final Method GET;

  /**
   * Provide method for performing HTTP PATCH in MVC tests. 
   */
  public static final Method PATCH;

  /**
   * Provide method for performing HTTP POST in MVC tests. 
   */
  public static final Method POST;

  /**
   * Provide method for performing HTTP PUT in MVC tests. 
   */
  public static final Method PUT;

  /**
   * Properly handle initialization when mock method builder throws exceptions.
   */
  static {
    Method delete = null;
    Method get = null;
    Method patch = null;
    Method post = null;
    Method put = null;

    try {
      delete = MockMvcRequestBuilders.class.getDeclaredMethod("delete", URI.class);
    } catch (Exception e) {
      logger.error("Initialization of static delete method reflection failed.", e);
    }

    try {
      get = MockMvcRequestBuilders.class.getDeclaredMethod("get", URI.class);
    } catch (Exception e) {
      logger.error("Initialization of static get method reflection failed.", e);
    }

    try {
      patch = MockMvcRequestBuilders.class.getDeclaredMethod("patch", URI.class);
    } catch (Exception e) {
      logger.error("Initialization of static patch method reflection failed.", e);
    }

    try {
      post = MockMvcRequestBuilders.class.getDeclaredMethod("post", URI.class);
    } catch (Exception e) {
      logger.error("Initialization of static post method reflection failed.", e);
    }

    try {
      put = MockMvcRequestBuilders.class.getDeclaredMethod("put", URI.class);
    } catch (Exception e) {
      logger.error("Initialization of static put method reflection failed.", e);
    }

    DELETE = delete;
    GET = get;
    PATCH = patch;
    POST = post;
    PUT = put;
  }

}
