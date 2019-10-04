package org.folio.spring.tenant.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.io.IOException;
import java.sql.SQLException;

import org.folio.spring.tenant.annotation.TenantHeader;
import org.folio.spring.tenant.hibernate.HibernateSchemaService;
import org.folio.spring.tenant.model.request.TenantAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/tenant")
public class TenantController {

  @Autowired
  private HibernateSchemaService hibernateSchemaService;

  @PostMapping
  // @formatter:off
  public ResponseEntity<String> create(
    @TenantHeader String tenant,
    @RequestBody @Validated TenantAttributes attributes
  ) throws SQLException, IOException {
  // @formatter:on
    hibernateSchemaService.createTenant(tenant);
    return new ResponseEntity<String>("Success", CREATED);
  }

  @DeleteMapping
  // @formatter:off
  public ResponseEntity<Void> delete(
      @TenantHeader String tenant
  ) throws SQLException {
  // @formatter:on
    hibernateSchemaService.deleteTenant(tenant);
    return new ResponseEntity<Void>(NO_CONTENT);
  }

}
