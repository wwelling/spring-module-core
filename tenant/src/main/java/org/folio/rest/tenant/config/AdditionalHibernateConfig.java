package org.folio.rest.tenant.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "additional")
public class AdditionalHibernateConfig {

  private List<String> domainPackages = new ArrayList<String>();

  private List<String> schemaScripts = new ArrayList<String>();

  public AdditionalHibernateConfig() {

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
