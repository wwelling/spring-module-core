package org.folio.spring.web.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.folio.spring.web.model.response.Error;
import org.folio.spring.web.model.response.ResponseErrors;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorUtilityTest {

  public static final String MESSAGE = "Mock Message";
  public static final String TYPE = "Mock Type";

  @Test
  void buildErrorTwoParameterWorksTest() {
    IOException exception = new IOException(MESSAGE);
    ResponseErrors errors = ErrorUtility.buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);

    assertEquals(1, errors.getTotalRecords());
    assertEquals(Error.class, errors.getErrors().get(0).getClass());

    Error error = errors.getErrors().get(0);
    assertEquals(MESSAGE, error.getMessage());
    assertEquals(exception.getClass().getSimpleName(), error.getType());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(), error.getCode());
  }

  @Test
  void buildErrorThreeParameterWorksTest() {
    ResponseErrors errors = ErrorUtility.buildError(MESSAGE, TYPE, HttpStatus.INTERNAL_SERVER_ERROR.toString());

    assertEquals(1, errors.getTotalRecords());
    assertEquals(Error.class, errors.getErrors().get(0).getClass());

    Error error = errors.getErrors().get(0);
    assertEquals(MESSAGE, error.getMessage());
    assertEquals(TYPE, error.getType());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(), error.getCode());
  }

  @Test
  void buildErrorsWorksTest() {
    Error error = new Error()
      .withMessage(MESSAGE)
      .withType(TYPE)
      .withCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

    List<Error> errorsList = new ArrayList<>(List.of(error));
    ResponseErrors errors = ErrorUtility.buildErrors(errorsList);

    assertEquals(1, errors.getTotalRecords());
    assertEquals(Error.class, errors.getErrors().get(0).getClass());

    Error err = errors.getErrors().get(0);
    assertEquals(MESSAGE, err.getMessage());
    assertEquals(TYPE, err.getType());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(), err.getCode());
  }

}
