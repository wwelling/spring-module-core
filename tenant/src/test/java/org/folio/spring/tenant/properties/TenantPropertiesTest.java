package org.folio.spring.tenant.properties;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TenantPropertiesTest {

  private TenantProperties tenantProperties;

  private List<String> domainPackages;

  private List<String> schemaScripts;

  @BeforeEach
  void beforeEach() {
    tenantProperties = new TenantProperties();
    domainPackages = new ArrayList<>();
    schemaScripts = new ArrayList<>();

    domainPackages.add(VALUE);
    schemaScripts.add(VALUE);
  }

  @Test
  void getHeaderNameWorksTest() {
    setField(tenantProperties, "headerName", VALUE);

    assertEquals(VALUE, tenantProperties.getHeaderName());
  }

  @Test
  void setHeaderNameWorksTest() {
    setField(tenantProperties, "headerName", null);

    tenantProperties.setHeaderName(VALUE);
    assertEquals(VALUE, getField(tenantProperties, "headerName"));
  }

  @Test
  void isForceTenantWorksTest() {
    setField(tenantProperties, "forceTenant", true);

    assertEquals(true, tenantProperties.isForceTenant());
  }

  @Test
  void setForceTenantWorksTest() {
    setField(tenantProperties, "forceTenant", false);

    tenantProperties.setForceTenant(true);
    assertEquals(true, getField(tenantProperties, "forceTenant"));
  }

  @Test
  void getDefaultTenantWorksTest() {
    setField(tenantProperties, "defaultTenant", VALUE);

    assertEquals(VALUE, tenantProperties.getDefaultTenant());
  }

  @Test
  void setDefaultTenantWorksTest() {
    setField(tenantProperties, "defaultTenant", null);

    tenantProperties.setDefaultTenant(VALUE);
    assertEquals(VALUE, getField(tenantProperties, "defaultTenant"));
  }

  @Test
  void isInitializeDefaultTenantWorksTest() {
    setField(tenantProperties, "initializeDefaultTenant", true);

    assertEquals(true, tenantProperties.isInitializeDefaultTenant());
  }

  @Test
  void setInitializeDefaultTenantWorksTest() {
    setField(tenantProperties, "initializeDefaultTenant", false);

    tenantProperties.setInitializeDefaultTenant(true);
    assertEquals(true, getField(tenantProperties, "initializeDefaultTenant"));
  }

  @Test
  void isRecreateDefaultTenantWorksTest() {
    setField(tenantProperties, "recreateDefaultTenant", true);

    assertEquals(true, tenantProperties.isRecreateDefaultTenant());
  }

  @Test
  void setRecreateDefaultTenantWorksTest() {
    setField(tenantProperties, "recreateDefaultTenant", false);

    tenantProperties.setRecreateDefaultTenant(true);
    assertEquals(true, getField(tenantProperties, "recreateDefaultTenant"));
  }

  @Test
  void getDomainPackagesWorksTest() {
    setField(tenantProperties, "domainPackages", domainPackages);

    assertEquals(domainPackages, tenantProperties.getDomainPackages());
  }

  @Test
  void setDomainPackagesWorksTest() {
    setField(tenantProperties, "domainPackages", null);

    tenantProperties.setDomainPackages(domainPackages);
    assertEquals(domainPackages, getField(tenantProperties, "domainPackages"));
  }

  @Test
  void getSchemaScriptsWorksTest() {
    setField(tenantProperties, "schemaScripts", schemaScripts);

    assertEquals(schemaScripts, tenantProperties.getSchemaScripts());
  }

  @Test
  void setSchemaScriptsWorksTest() {
    setField(tenantProperties, "schemaScripts", null);

    tenantProperties.setSchemaScripts(schemaScripts);
    assertEquals(schemaScripts, getField(tenantProperties, "schemaScripts"));
  }

}
