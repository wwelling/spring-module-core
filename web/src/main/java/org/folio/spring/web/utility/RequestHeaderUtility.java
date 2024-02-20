package org.folio.spring.web.utility;

import java.util.Iterator;
import org.springframework.http.MediaType;

public class RequestHeaderUtility {

  /**
   * Private initializer as per java:S1118.
   */
  private RequestHeaderUtility() {
  }

  /**
   * A Content-Type for "application/*+json" as a MediaType.
   */
  public static final MediaType APP_JSON_PLUS = new MediaType("application", "*+json");

  /**
   * A Content-Type for "application/raml+yaml" as a MediaType.
   */
  public static final MediaType APP_RAML = new MediaType("application", "raml+yaml");

  /**
   * A Content-Type for "application/schema+json" as a MediaType.
   */
  public static final MediaType APP_SCHEMA = new MediaType("application", "schema+json");

  /**
   * Determine if Accept header is missing the supported Content-Type.
   *
   * @param accept The raw HTTP Accept header value.
   * @param contentTypes The Content-Types, AKA media type, that are being tested against.
   *
   * @return
   *   True if the Content-Type is not in the Accept header.
   *   False otherwise or when accept is NULL.
   */
  public static boolean unsupportedAccept(String accept, MediaType ...contentTypes) {
    if (accept == null) {
      return false;
    }

    Iterator<MediaType> iter = MediaType.parseMediaTypes(accept).iterator();

    while (iter.hasNext()) {
      MediaType type = iter.next();

      for (MediaType contentType : contentTypes) {
        if (contentType.isCompatibleWith(type)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Determine if content type is compatible with any of the given content types.
   *
   * @param type The content type to test compatibility with.
   * @param contentTypes The Content-Types, AKA media type, that are being tested against.
   *
   * @return
   *   True if the Content-Type is compatible.
   *   False otherwise or when type is NULL.
   */
  public static boolean compatibleWith(MediaType type, MediaType ...contentTypes) {
    if (type == null) {
      return false;
    }

    for (MediaType contentType : contentTypes) {
      if (contentType.isCompatibleWith(type)) {
        return true;
      }
    }

    return false;
  }
}
