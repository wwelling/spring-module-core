package org.folio.spring.tenant.service;

import java.util.Locale;
import java.util.regex.Pattern;
import org.folio.spring.tenant.properties.BuildInfoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

  // allow list of characters prevents SQL injection
  private static final Pattern SCHEMA_REGEXP = Pattern.compile("[a-z0-9_]+");

  @Autowired
  private BuildInfoProperties buildInfoProperties;

  public String getSchema(String tenant) {
    String schema = String.format("%s_%s", tenant, buildInfoProperties.getArtifact())
        .replace("-", "_").toLowerCase(Locale.ROOT);
    if (! SCHEMA_REGEXP.matcher(schema).matches()) {
      throw new IllegalArgumentException("Illegal character in schema name: " + schema);
    }
    return schema;
  }

}
