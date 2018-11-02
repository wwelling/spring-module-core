package org.folio.rest.utility;

import java.util.ArrayList;
import java.util.List;

import org.folio.rest.model.response.Error;
import org.folio.rest.model.response.Errors;
import org.springframework.http.HttpStatus;

public class ErrorUtility {

  public static Errors buildError(Exception exception, HttpStatus status) {
    return buildError(exception.getMessage(), exception.getClass().getSimpleName(), status.toString());
  }

  public static Errors buildError(String message, String type, String code) {
    Error error = new Error();
    error.setMessage(message);
    error.setType(type);
    error.setCode(code);
    List<Error> listOfErrors = new ArrayList<Error>();
    listOfErrors.add(error);
    return buildErrors(listOfErrors);
  }

  public static Errors buildErrors(List<Error> listOfErrors) {
    Errors errors = new Errors();
    errors.setErrors(listOfErrors);
    errors.setTotalRecords(listOfErrors.size());
    return errors;
  }

}