package org.folio.spring.tenant.exception;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoTenantExceptionTest {

  @Test
  void noTenantExceptionWorksTest() throws IOException {
    NoTenantException exception = Assertions.assertThrows(NoTenantException.class, () -> {
      throw new NoTenantException(VALUE);
    });

    assertNotNull(exception);
    assertEquals(VALUE, exception.getMessage());
  }
}
