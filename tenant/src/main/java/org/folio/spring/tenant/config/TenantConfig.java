package org.folio.spring.tenant.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tenant")
public class TenantConfig {

    private String headerName = "X-Okapi-Tenant";

    private boolean forceTenant = true;

    private String defaultTenant = "public";

    private boolean initializeDefaultTenant = false;

    private List<String> domainPackages = new ArrayList<String>();

    private List<String> schemaScripts = new ArrayList<String>();

    public TenantConfig() {

    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public boolean isForceTenant() {
        return forceTenant;
    }

    public void setForceTenant(boolean forceTenant) {
        this.forceTenant = forceTenant;
    }

    public String getDefaultTenant() {
        return defaultTenant;
    }

    public void setDefaultTenant(String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    public boolean isInitializeDefaultTenant() {
        return initializeDefaultTenant;
    }

    public void setInitializeDefaultTenant(boolean initializeDefaultTenant) {
        this.initializeDefaultTenant = initializeDefaultTenant;
    }

    public List<String> getDomainPackages() {
      return domainPackages;
    }
    
    public void setDomainPackages(List<String> domainPackages) {
      this.domainPackages = domainPackages;
    }

    public List<String> getSchemaScripts() {
        return schemaScripts;
    }

    public void setSchemaScripts(List<String> schemaScripts) {
        this.schemaScripts = schemaScripts;
    }

}
