package org.folio.spring.tenant.hibernate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.folio.spring.tenant.exception.TenantAlreadyExistsException;
import org.folio.spring.tenant.exception.TenantDoesNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HibernateSchemaServiceIT {

  @Autowired
  private HibernateSchemaService hibernateSchemaService;

  @Test
  void testCreateAndDelete() {
    assertDoesNotThrow(() -> hibernateSchemaService.createTenant("a"));
    assertDoesNotThrow(() -> hibernateSchemaService.deleteTenant("a"));
  }

  @Test
  void testCreateAlreadyExists() throws Exception {
    assertDoesNotThrow(() -> hibernateSchemaService.createTenant("a"));
    assertThrows(TenantAlreadyExistsException.class, () -> hibernateSchemaService.createTenant("a"));

    assertDoesNotThrow(() -> hibernateSchemaService.createTenant("b"));
    assertThrows(TenantAlreadyExistsException.class, () -> hibernateSchemaService.createTenant("b"));

    assertDoesNotThrow(() -> hibernateSchemaService.deleteTenant("b"));
    assertDoesNotThrow(() -> hibernateSchemaService.deleteTenant("a"));
  }

  @Test
  void testDeleteDoesNotExists() throws Exception {
    assertDoesNotThrow(() -> hibernateSchemaService.createTenant("a"));
    assertThrows(TenantAlreadyExistsException.class, () -> hibernateSchemaService.createTenant("a"));

    assertDoesNotThrow(() -> hibernateSchemaService.createTenant("b"));
    assertThrows(TenantAlreadyExistsException.class, () -> hibernateSchemaService.createTenant("b"));

    assertDoesNotThrow(() -> hibernateSchemaService.deleteTenant("b"));
    assertDoesNotThrow(() -> hibernateSchemaService.deleteTenant("a"));

    assertThrows(TenantDoesNotExistsException.class, () -> hibernateSchemaService.deleteTenant("b"));
    assertThrows(TenantDoesNotExistsException.class, () -> hibernateSchemaService.deleteTenant("a"));
  }

}
