package org.folio.spring.domain.controller.exception;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchemaIOExceptionTest {

  @Mock
  Throwable throwable;

  @Test
  void schemaIOExceptionWorksTest() throws IOException {
    SchemaIOException exception = Assertions.assertThrows(SchemaIOException.class, () -> {
      throw new SchemaIOException(VALUE, throwable);
    });

    assertNotNull(exception);
    assertEquals(VALUE, exception.getMessage());
    assertEquals(throwable, exception.getCause());
  }
}
