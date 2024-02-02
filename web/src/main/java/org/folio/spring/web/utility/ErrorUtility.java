package org.folio.spring.web.utility;

import java.util.ArrayList;
import java.util.List;
import org.folio.spring.web.model.response.Error;
import org.folio.spring.web.model.response.ResponseErrors;
import org.springframework.http.HttpStatus;

public class ErrorUtility {

  private ErrorUtility() {
    // do nothing
  }

  public static ResponseErrors buildError(Exception exception, HttpStatus status) {
    return buildError(exception.getLocalizedMessage(), exception.getClass().getSimpleName(), status.toString());
  }

  public static ResponseErrors buildError(String message, String type, String code) {
    Error error = new Error();
    error.setMessage(message);
    error.setType(type);
    error.setCode(code);
    List<Error> listOfErrors = new ArrayList<>();
    listOfErrors.add(error);
    return buildErrors(listOfErrors);
  }

  public static ResponseErrors buildErrors(List<Error> listOfErrors) {
    ResponseErrors responseErrors = new ResponseErrors();
    responseErrors.setErrors(listOfErrors);
    responseErrors.setTotalRecords(listOfErrors.size());
    return responseErrors;
  }

}
