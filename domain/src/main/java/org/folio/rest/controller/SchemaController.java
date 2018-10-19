package org.folio.rest.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.folio.rest.controller.exception.SchemaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/schemas")
public class SchemaController {

  @Autowired
  private ResourcePatternResolver resolver;

  @Autowired
  private ObjectMapper mapper;

  @GetMapping
  public Map<String, JsonNode> getSchemas() throws IOException, URISyntaxException {
    Map<String, JsonNode> schemas = new HashMap<>();
    Resource[] resources = resolver.getResources("classpath:ramls/*.json");
    for (Resource resource : resources) {
      String name = resource.getFilename();
      if (resource.isFile()) {
        schemas.put(name, mapper.readValue(resource.getFile(), JsonNode.class));
      } else {
        schemas.put(name, mapper.readValue(resource.getInputStream(), JsonNode.class));
      }
    }
    return schemas;
  }

  @GetMapping("/{name}")
  public JsonNode getSchemaByName(@PathVariable String name) throws JsonParseException, JsonMappingException, IOException {
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
