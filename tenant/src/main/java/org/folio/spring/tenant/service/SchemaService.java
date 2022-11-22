package org.folio.spring.tenant.service;

import java.util.regex.Pattern;
import org.folio.spring.tenant.properties.BuildInfoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

  // allowing only a-z A-Z 0-9 and _ prevents SQL injection
  private static final Pattern SCHEMA_REGEXP = Pattern.compile("\\w+");

  @Autowired
  private BuildInfoProperties buildInfoProperties;

  public String getSchema(String tenant) {
    var schema = String.format("%s_%s", tenant, buildInfoProperties.getArtifact()).replace("-", "_");
    if (! SCHEMA_REGEXP.matcher(schema).matches()) {
      throw new IllegalArgumentException("Illegal character in schema name: " + schema);
    }
    return schema;
  }

}
