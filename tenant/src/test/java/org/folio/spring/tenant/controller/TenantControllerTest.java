package org.folio.spring.tenant.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.folio.spring.tenant.exception.TenantAlreadyExistsException;
import org.folio.spring.tenant.exception.TenantDoesNotExistsException;
import org.folio.spring.tenant.model.request.TenantAttributes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TenantControllerTest {

  @Autowired
  TenantController tenantController;

  TenantAttributes tenantAttributes = new TenantAttributes("1.0.0");

  @Test
  void createAndDelete() throws Exception {
    tenantController.create("a", tenantAttributes);
    assertThrows(TenantAlreadyExistsException.class, () -> tenantController.create("a", tenantAttributes));
    tenantController.create("b", tenantAttributes);
    tenantController.delete("b");
    assertThrows(TenantDoesNotExistsException.class, () -> tenantController.delete("b"));
    assertThrows(TenantDoesNotExistsException.class, () -> tenantController.delete("c"));
    tenantController.delete("a");
  }

}
