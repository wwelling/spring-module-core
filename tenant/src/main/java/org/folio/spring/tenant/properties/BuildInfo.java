package org.folio.spring.tenant.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "info.build")
public class BuildInfo {

  private String artifact;

  private String name;

  private String description;

  private String version;

  public BuildInfo() {

  }

  public String getArtifact() {
    return artifact;
  }

  public void setArtifact(String artifact) {
    this.artifact = artifact;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

}
