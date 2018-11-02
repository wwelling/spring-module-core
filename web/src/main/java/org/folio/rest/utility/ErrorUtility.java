package org.folio.rest.utility;

import java.util.ArrayList;
import java.util.List;

import org.folio.rest.model.response.Error;
import org.folio.rest.model.response.Errors;
import org.springframework.http.HttpStatus;

public class ErrorUtility {

  public static Errors craftErrors(Exception exception, HttpStatus status) {
    return craftErrors(exception.getMessage(), exception.getClass().getSimpleName(), status.toString());
  }

  public static Errors craftErrors(String message, String type, HttpStatus status) {
    return craftErrors(message, type, status.toString());
  }

  public static Errors craftErrors(String message, String type, String code) {
    Errors errors = new Errors();
    Error error = new Error();
    error.setMessage(message);
    error.setType(type);
    error.setCode(code);
    List<Error> errorList = new ArrayList<Error>();
    errorList.add(error);
    return errors;
  }

}