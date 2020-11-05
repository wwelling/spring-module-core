package org.folio.spring.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.folio.spring.controller.exception.SchemaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonSchemasService {

  private static final Pattern REF_MATCH_PATTERN = Pattern.compile("\\\"\\$ref\\\"\\s*:\\s*\\\"(.*?)\\\"");

  private static final String RAMLS_PATH = "ramls/";
  private static final String HASH_TAG = "#";

  @Autowired
  private ResourcePatternResolver resolver;

  @Autowired
  private ObjectMapper mapper;

  public List<String> getSchemas() throws IOException {
    List<String> schemas = new ArrayList<>();
    Resource[] resources = resolver.getResources("classpath:ramls/*.json");
    for (Resource resource : resources) {
      schemas.add(resource.getFilename());
    }
    return schemas;
  }

  public String getSchemaByPath(String path, String okapiUrl) throws IOException {
    Resource resource = resolver.getResource("classpath:ramls/" + path);
    if (resource.exists()) {
      return replaceReferences(mapper.readValue(resource.getInputStream(), JsonNode.class), okapiUrl);
    }
    throw new SchemaNotFoundException("Schema " + path + " not found");
  }

  private String replaceReferences(JsonNode schemaNode, String okapiUrl) {
    String schema = schemaNode.toString();
    Matcher matcher = REF_MATCH_PATTERN.matcher(schema);
    StringBuffer sb = new StringBuffer(schema.length());
    while (matcher.find()) {
      String path = matcher.group(1);
      if (!path.startsWith(HASH_TAG)) {
        if (path.contains(RAMLS_PATH)) {
          path = path.substring(path.lastIndexOf(RAMLS_PATH) + RAMLS_PATH.length());
        }
        matcher.appendReplacement(sb, Matcher.quoteReplacement("\"$ref\":\"" + okapiUrl + "/_/jsonSchemas?path=" + path + "\""));
      }
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

}
