package org.folio.spring.web.utility;

import java.util.Iterator;
import org.springframework.http.MediaType;

public class RequestHeaderUtility {

  /**
   * Determine if Accept header is missing the supported Content-Type.
   *
   * @param accept The raw HTTP Accept header value.
   * @param contentType The media-type, aka Content-Type that is required.
   *
   * @return
   *   True if the Content-Type is not in the Accept header.
   *   False otherwise.
   */
  public static boolean unsupportedAccept(String accept, MediaType contentType) {
    Iterator<MediaType> iter = MediaType.parseMediaTypes(accept).iterator();

    while (iter.hasNext()) {
      if (contentType.isCompatibleWith(iter.next())) {
        return false;
      }
    }

    return true;
  }
}
