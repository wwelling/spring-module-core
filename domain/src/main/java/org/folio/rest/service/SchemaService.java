package org.folio.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.folio.rest.controller.exception.SchemaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SchemaService {

  @Autowired
  private ResourcePatternResolver resolver;

  @Autowired
  private ObjectMapper mapper;

  public List<String> getSchemas() throws IOException {
    List<String> schemas = new ArrayList<>();
    Resource[] resources = resolver.getResources("classpath:ramls/*.json");
    for (Resource resource : resources) {
      String name = resource.getFilename();
      schemas.add(name);
    }
    return schemas;
  }

  public JsonNode getSchemaByName(@PathVariable String name) throws IOException {
    Resource resource = resolver.getResource("classpath:ramls/" + name);
    if (resource.exists()) {
      if (resource.isFile()) {
        return mapper.readValue(resource.getFile(), JsonNode.class);
      } else {
        return mapper.readValue(resource.getInputStream(), JsonNode.class);
      }
    }
    throw new SchemaNotFoundException("Schema " + name + " not found");
  }

}
